package fr.uge.gitclout.utilities;

import fr.uge.gitclout.entity.Commiter;
import fr.uge.gitclout.entity.Contribution;
import fr.uge.gitclout.entity.Tag;
import fr.uge.gitclout.service.ContributionService;
import jakarta.validation.constraints.NotNull;
import org.eclipse.jgit.api.BlameCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.diff.*;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

public class Analyzer {
    private final Git git;
    private final List<Tag> tags;
    private final HashSet<Commiter> commiters;
    private final List<Contribution> contributions = new ArrayList<>();
    private final ContributionService contributionService;
    private final HashMap<String, BlameResult> cache = new HashMap<>();
    private final RevWalk revWalk;

    public Analyzer(@NotNull Git git, @NotNull List<Tag> tags,@NotNull HashSet<Commiter> commiters,
                    @NotNull ContributionService contributionService, @NotNull RevWalk revWalk) {
        this.git = git;
        this.tags = tags;
        this.commiters = commiters;
        this.contributionService = contributionService;
        this.revWalk = revWalk;
    }

    // Method to retrieve language from file path
    private Language getLanguageFromPath(String path) {
        var split = path.split("\\.");
        var last = List.of(split).getLast();
        return switch (last) {
            case "java" -> Language.JAVA;
            case "cs" -> Language.CSHARP;
            case "py" -> Language.PYTHON;
            case "ts" -> Language.TYPESCRIPT;
            default -> Language.OTHER;
        };
    }

    // Method to create a DiffFormatter object
    private DiffFormatter createDiffFormater() {
        var df = new DiffFormatter( new ByteArrayOutputStream() );
        df.setRepository(git.getRepository());
        return df;
    }

    // Method to compute differences between two trees
    private List<DiffEntry> diff(@NotNull DiffFormatter df, @NotNull CanonicalTreeParser tags1, @NotNull CanonicalTreeParser tags2) throws IOException {
        return df.scan( tags1, tags2 );
    }

    // Method to create a tree
    private CanonicalTreeParser createTree(ObjectReader reader, ObjectId id, RevWalk revWalk) throws IOException {
        CanonicalTreeParser tree = new CanonicalTreeParser();
        tree.reset(reader, revWalk.parseTree(id));
        return tree;
    }

    // Method to retrieve BlameResult on a file
    private BlameResult blaming(String filePath, int index) throws GitAPIException, IOException {
        if (cache.containsKey(filePath)) {
            return cache.get(filePath);
        }
        BlameCommand blameCommand = new BlameCommand(git.getRepository());
        blameCommand.setFilePath(filePath)
                .setFollowFileRenames(true)
                .setStartCommit(revWalk.parseCommit(tags.get(index).getObjId()));
        var result = blameCommand.call();
        cache.put(filePath, result);
        return result;
    }

    // Method to obtain Commiter from RevCommit
    private Commiter getCommiterFromRevCommit(@NotNull RevCommit commit) {
        return commiters.stream()
                .filter(commiter -> commiter.getName().equals(commit.getAuthorIdent().getName()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(commit.getAuthorIdent().toString()));
    }

    // Method to default contribute to languages in a BlameResult
    private void contributeDefault(@NotNull BlameResult result, @NotNull Language language,
                                   @NotNull HashMap<Commiter, HashMap<Language, Integer>> map) {
        for (var i = 0; i < result.getResultContents().size() - 1; i++) {
            if (result.getSourceAuthor(i).getName().equals("no-author")) {
                continue;
            }
            var commiter = getCommiterFromRevCommit(result.getSourceCommit(i));
            addToMap(map, commiter, language);
        }
    }

    // Method to add an entry to the Contribution Map by Commiter and language
    private void addToMap(@NotNull HashMap<Commiter, HashMap<Language, Integer>> map, @NotNull Commiter commiter,
                          @NotNull Language language) {
        map.putIfAbsent(commiter, new HashMap<>());
        map.computeIfPresent(commiter, (k, m) -> {
            m.putIfAbsent(language, 0);
            m.computeIfPresent(language, (p, v) -> v+1);
            return m;
        });
    }

    // Method to contribute modifications in a BlameResult
    private void contributeModify(@NotNull BlameResult result, @NotNull Language language,
                                  @NotNull HashMap<Commiter, HashMap<Language, Integer>> map, @NotNull EditList editList,
                                  @NotNull String path, @NotNull int index) throws GitAPIException, IOException {
        for (var edit : editList) {
            if (!edit.getType().equals(Edit.Type.DELETE)) {
                for (var i = edit.getBeginB(); i < edit.getEndB() - 1; i++) {
                    if (result.getResultContents().size() < edit.getEndB()) {
                        cache.remove(path);
                        result = blaming(path, index);
                    }
                    if (!result.getSourceAuthor(i).getName().equals("no-author")) {
                        var commiter = getCommiterFromRevCommit(result.getSourceCommit(i));
                        addToMap(map, commiter, language);
                    }
                }
            }
        }
    }

    // Method to unpack the Map and add contributions
    private void unpackMapAndAdd(@NotNull HashMap<Commiter, HashMap<Language, Integer>> map, @NotNull Tag tag) {
        map.forEach((commiter, map2) -> map2.forEach((language, integer) -> contributions
                .add(contributionService.addContribution(commiter, integer, tag, language))));
    }

    // Method for the case where a modification is detected in a DiffEntry
    private void caseModify(@NotNull DiffEntry entry, int index, @NotNull HashMap<Commiter, HashMap<Language, Integer>> map,
                            @NotNull DiffFormatter df) throws GitAPIException, IOException {
        var result = blaming(entry.getNewPath(), index);
        var language = getLanguageFromPath(entry.getNewPath());
        var edit = df.toFileHeader(entry).toEditList();
        contributeModify(result, language, map, edit, entry.getNewPath(), index);
    }

    // Method for the case where no modification is detected in a DiffEntry
    private void caseDefault(DiffEntry entry, int index, HashMap<Commiter, HashMap<Language, Integer>> map) throws GitAPIException, IOException {
        var result = blaming(entry.getNewPath(), index);
        var language = getLanguageFromPath(entry.getNewPath());
        contributeDefault(result, language, map);
    }

    // Method to analyze differences between trees and compute contributions
    private void analyzeTree(@NotNull CanonicalTreeParser tags1, @NotNull CanonicalTreeParser tags2, int index) throws IOException, GitAPIException {
        var df = createDiffFormater();
        var entries = diff(df, tags1, tags2);
        var map = new HashMap<Commiter, HashMap<Language, Integer>>();
        for (var entry : entries) {
            switch (entry.getChangeType()) {
                case MODIFY -> caseModify(entry, index, map, df);
                case ADD, COPY, RENAME -> caseDefault(entry, index, map);
                case DELETE -> blaming(entry.getOldPath(), index);
            }
        }
        unpackMapAndAdd(map, tags.get(index));
    }

    /**
     * Analyzes commit differences to compute contributions.
     *
     * @param revWalk The RevWalk containing the commits to be analyzed.
     * @return A list of computed contributions.
     * @throws GitAPIException In case of error accessing Git API.
     * @throws IOException     In case of input/output error.
     */
    public List<Contribution> analyze(@NotNull RevWalk revWalk) throws GitAPIException, IOException {
        try (var reader = git.getRepository().newObjectReader()) {
            var emptyTree = new CanonicalTreeParser();
            emptyTree.reset();
            var first = createTree(reader, tags.getFirst().getObjId(), revWalk);
            analyzeTree(emptyTree, first, 0);
            for (var i = 0; i < tags.size() - 1; i++) {
                var tags1 = createTree(reader, tags.get(i).getObjId(), revWalk);
                var tags2 = createTree(reader, tags.get(i+1).getObjId(), revWalk);
                analyzeTree(tags1, tags2, i+1);
            }
        }
        return contributions;
    }
}

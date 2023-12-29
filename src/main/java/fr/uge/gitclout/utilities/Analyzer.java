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

    public Analyzer(@NotNull Git git, @NotNull List<Tag> tags,
                    @NotNull HashSet<Commiter> commiters, @NotNull ContributionService contributionService, @NotNull RevWalk revWalk) {
        this.git = git;
        this.tags = tags;
        this.commiters = commiters;
        this.contributionService = contributionService;
        this.revWalk = revWalk;
    }

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

    private List<DiffEntry> diff(CanonicalTreeParser tags1, CanonicalTreeParser tags2) throws IOException {
        var df = new DiffFormatter( new ByteArrayOutputStream() );
        df.setRepository( git.getRepository() );
        return df.scan( tags1, tags2 );
    }

    private CanonicalTreeParser createTree(ObjectReader reader, ObjectId id, RevWalk revWalk) throws IOException {
        CanonicalTreeParser tree = new CanonicalTreeParser();
        tree.reset(reader, revWalk.parseTree(id));
        return tree;
    }

    private BlameResult blaming(String filePath, int index) throws GitAPIException, IOException {
        BlameCommand blameCommand = new BlameCommand(git.getRepository());
        blameCommand.setFilePath(filePath)
                .setStartCommit(revWalk.parseCommit(tags.get(index).getObjId()));
        return blameCommand.call();
    }



    private Commiter getCommiterFromRevCommit(@NotNull RevCommit commit) {
        return commiters.stream()
                .filter(commiter -> commiter.getName().equals(commit.getAuthorIdent().getName()))
                .findFirst()
                .orElseThrow();
    }

    private void contributeDefault(@NotNull BlameResult result, int index, @NotNull Language language,
                                   @NotNull HashMap<Commiter, HashMap<Language, Integer>> map) {
        for (var i = 0; i < result.getResultContents().size(); i++) {
            var commiter = getCommiterFromRevCommit(result.getSourceCommit(i));
            map.putIfAbsent(commiter, new HashMap<>());
            map.computeIfPresent(commiter, (k, m) -> {
                m.putIfAbsent(language, 0);
                m.computeIfPresent(language, (p, v) -> v+1);
                return m;
            });
        }
    }

    private boolean containsLine(@NotNull String line, @NotNull RawText old) {
        for (var i = 0; i < old.size(); i++) {
            if (old.getString(i).equals(line)) {
                return true;
            }
        }
        return false;
    }

    private void addToMap(@NotNull HashMap<Commiter, HashMap<Language, Integer>> map, @NotNull Commiter commiter,
                          @NotNull Language language) {
        map.putIfAbsent(commiter, new HashMap<>());
        map.computeIfPresent(commiter, (k, m) -> {
            m.putIfAbsent(language, 0);
            m.computeIfPresent(language, (p, v) -> v+1);
            return m;
        });
    }

    private void contributeModify(@NotNull BlameResult result, String path, @NotNull Language language,
                                  @NotNull HashMap<Commiter, HashMap<Language, Integer>> map) throws IOException {
        var old = cache.get(path).getResultContents();
        for (var i = 0; i < result.getResultContents().size(); i++) {
            var commiter = getCommiterFromRevCommit(result.getSourceCommit(i));
            if (i < old.size()) {
                if (!result.getResultContents().getString(i).equals(old.getString(i))) {
                    if (!containsLine(result.getResultContents().getString(i), old)) {
                        addToMap(map, commiter, language);
                    }
                }
            }
            else {
                if (!containsLine(result.getResultContents().getString(i), old)) {
                    addToMap(map, commiter, language);
                }
            }
        }
    }

    private void unpackMapAndAdd(@NotNull HashMap<Commiter, HashMap<Language, Integer>> map, @NotNull Tag tag) {
        map.forEach((commiter, map2) -> {
            map2.forEach((language, integer) -> {
                contributions.add(contributionService.addContribution(commiter, integer, tag, language));
            });
        });
    }

    private void caseModify(DiffEntry entry, int index, HashMap<Commiter, HashMap<Language, Integer>> map) throws GitAPIException, IOException {
        var result = blaming(entry.getNewPath(), index);
        var language = getLanguageFromPath(entry.getNewPath());
        contributeModify(result, entry.getNewPath(), language, map);
        cache.put(entry.getNewPath(), result);
    }

    private void caseDefault(DiffEntry entry, int index, HashMap<Commiter, HashMap<Language, Integer>> map) throws GitAPIException, IOException {
        var result = blaming(entry.getNewPath(), index);
        var language = getLanguageFromPath(entry.getNewPath());
        contributeDefault(result, index, language, map);
        cache.put(entry.getNewPath(), result);
    }

    private void analyzeTree(@NotNull CanonicalTreeParser tags1, @NotNull CanonicalTreeParser tags2, int index) throws IOException, GitAPIException {
        var entries = diff(tags1, tags2);
        var map = new HashMap<Commiter, HashMap<Language, Integer>>();
        for (var entry : entries) {
            switch (entry.getChangeType()) {
                case MODIFY -> caseModify(entry, index, map);
                case ADD, COPY, RENAME -> caseDefault(entry, index, map);
                case DELETE -> blaming(entry.getOldPath(), index);
            }
        }
        System.out.println(tags.get(index).getName() + " ------> " + map);
        unpackMapAndAdd(map, tags.get(index));
    }

    public List<Contribution> analyze(@NotNull RevWalk revWalk) throws GitAPIException, IOException {
        try (var reader = git.getRepository().newObjectReader()) {
            var emptyTree = new CanonicalTreeParser();
            emptyTree.reset();
            var first = createTree(reader, tags.get(0).getObjId(), revWalk);
            analyzeTree(emptyTree, first, 0);
            for (var i = 0; i < 1; i++) {
                var tags1 = createTree(reader, tags.get(i).getObjId(), revWalk);
                var tags2 = createTree(reader, tags.get(i+1).getObjId(), revWalk);
                analyzeTree(tags1, tags2, i+1);
            }
        }
        return contributions;
    }
}

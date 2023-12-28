package fr.uge.gitclout.utilities;

import fr.uge.gitclout.entity.Commiter;
import fr.uge.gitclout.entity.Contribution;
import fr.uge.gitclout.entity.Tag;
import fr.uge.gitclout.entity.Repo;
import fr.uge.gitclout.service.ContributionService;
import jakarta.validation.constraints.NotNull;
import org.eclipse.jgit.api.BlameCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Analyzer {
    private final Git git;
    private final List<Tag> tags;
    private final HashSet<Commiter> commiters;
    private final List<Contribution> contributions = new ArrayList<>();
    private final ContributionService contributionService;
    private final HashMap<String, BlameResult> cache = new HashMap<>();

    public Analyzer(@NotNull Git git, @NotNull Repo repo, @NotNull List<Tag> tags,
                    @NotNull HashSet<Commiter> commiters, @NotNull ContributionService contributionService) {
        this.git = git;
        this.tags = tags;
        this.commiters = commiters;
        this.contributionService = contributionService;
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
        DiffFormatter df = new DiffFormatter( new ByteArrayOutputStream() );
        df.setRepository( git.getRepository() );
        return df.scan( tags1, tags2 );
    }

    private CanonicalTreeParser createTree(ObjectReader reader, ObjectId id, RevWalk revWalk) throws IOException {
        CanonicalTreeParser tree = new CanonicalTreeParser();
        tree.reset(reader, revWalk.parseCommit(id).getTree().getId());
        return tree;
    }

    private BlameResult blaming(String filePath) throws GitAPIException {
        BlameCommand blameCommand = new BlameCommand(git.getRepository());
        var elt = cache.get(filePath);
        if (elt != null) {
            return elt;
        }
        blameCommand.setFilePath(filePath)
                .setFollowFileRenames(true);
        return blameCommand.call();
    }

    private Commiter getCommiterFromRevCommit(@NotNull RevCommit commit) {
        return commiters.stream()
                .filter(commiter -> commiter.getName().equals(commit.getAuthorIdent().getName()))
                .findFirst()
                .orElseThrow();
    }

    private boolean isFileADirectory(String filePath){
        var file = new File(filePath);
        return file.isDirectory();
    }

    private void contributeDefault(@NotNull BlameResult result, int index, @NotNull Language language) {
        var map = new HashMap<Commiter, Integer>();
        for (var i = 0; i < result.getResultContents().size(); i++) {
            var commiter = getCommiterFromRevCommit(result.getSourceCommit(i));
            map.putIfAbsent(commiter, 0);
            map.computeIfPresent(commiter, (__, v) -> v + 1);
        }
        map.forEach((k, v) -> contributions.add(contributionService.addContribution(k, v, tags.get(index), language)));
    }

    private void contributeModify(@NotNull BlameResult result1, @NotNull BlameResult result2, int index, @NotNull Language language) {
        var map = new HashMap<Commiter, Integer>();
        for (var i = 0; i < result2.getResultContents().size(); i++) {
            var commiter = getCommiterFromRevCommit(result2.getSourceCommit(i));
            if (!Objects.equals(result2.getResultContents().getString(i), result1.getResultContents().getString(i))) {
                map.putIfAbsent(commiter, 0);
                map.computeIfPresent(commiter, (__, v) -> v + 1);
            }
        }
        map.forEach((k, v) -> contributions.add(contributionService.addContribution(k, v, tags.get(index), language)));
    }

    private void analyzeTree(@NotNull CanonicalTreeParser tags1, @NotNull CanonicalTreeParser tags2, int index) throws IOException, GitAPIException {
        var entries = diff(tags1, tags2);
        for (var entry : entries) {
            switch (entry.getChangeType()) {
                case MODIFY -> {
                    var result1 = blaming(entry.getOldPath());
                    var result2 = blaming(entry.getNewPath());
                    if (result2 == null) { // ne devrait pas arriver !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                         continue;
                    }
                    cache.put(entry.getOldPath(), result1);
                    cache.put(entry.getNewPath(), result2);
                    var language = getLanguageFromPath(entry.getNewPath());
                    contributeModify(result1, result2, index, language);
                }
                case ADD, COPY, RENAME -> {
                    if (isFileADirectory(entry.getNewPath())) {
                        continue;
                    }
                    var result = blaming(entry.getNewPath());
                    if (result == null) { // ne devrait pas arriver
                        continue;
                    }
                    cache.put(entry.getNewPath(), result);
                    var language = getLanguageFromPath(entry.getNewPath());
                    contributeDefault(result, index, language);
                }
                case DELETE -> {
                    var result = blaming(entry.getOldPath());
                    if (result == null) { // ne devrait pas arriver
                        continue;
                    }
                    cache.put(entry.getOldPath(), result);
                }
            }
        }
    }

    public List<Contribution> analyze(@NotNull List<Ref> tags, @NotNull RevWalk revWalk) throws GitAPIException, IOException {
        try (var reader = git.getRepository().newObjectReader()) {
            var emptyTree = new CanonicalTreeParser();
            var first = createTree(reader, tags.get(0).getObjectId(), revWalk);
            analyzeTree(emptyTree, first, 0);
            for (var i = 0; i < tags.size() - 1; i++) {
                var tags1 = createTree(reader, tags.get(i).getObjectId(), revWalk);
                var tags2 = createTree(reader, tags.get(i+1).getObjectId(), revWalk);
                analyzeTree(tags1, tags2, i+1);
            }
        }
        return contributions;
    }
}

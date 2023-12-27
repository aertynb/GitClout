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
import java.io.IOException;
import java.util.*;

public class Analyzer {
    private final Git git;
    private final List<Tag> tags;
    private final HashSet<Commiter> commiters;
    private final List<Contribution> contributions = new ArrayList<>();
    private final ContributionService contributionService;
    private final List<BlameResult> cache = new ArrayList<>();

    public Analyzer(@NotNull Git git, @NotNull Repo repo, @NotNull List<Tag> tags,
                    @NotNull HashSet<Commiter> commiters, @NotNull ContributionService contributionService) {
        this.git = git;
        this.tags = tags;
        this.commiters = commiters;
        this.contributionService = contributionService;
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
        blameCommand.setFilePath(filePath);
        if (!cache.isEmpty()) {
            var elt = cache.getFirst();
            blameCommand.setStartCommit(elt.getSourceCommit(0));
        }
        return blameCommand.call();
    }

    private Tag getTagFromId(@NotNull ObjectId id) {
        for (var tag : tags) {
            if (tag.getObjId() == id) {
                return tag;
            }
        }
        return null;
    }

    private Commiter getCommiterFromRevCommit(@NotNull RevCommit commit) {
        return commiters.stream()
                .filter(commiter -> commiter.getName().equals(commit.getAuthorIdent().getName()))
                .findFirst()
                .orElseThrow();
    }

    private void contribute(@NotNull BlameResult result, @NotNull DiffEntry entry, int index) {
        var map = new HashMap<Commiter, Integer>();
        if (entry.getChangeType() == DiffEntry.ChangeType.MODIFY || entry.getChangeType() == DiffEntry.ChangeType.ADD) {
            for (var i = 0; i < result.getResultContents().size(); i++) {
                var commiter = getCommiterFromRevCommit(result.getSourceCommit(0));
                map.computeIfAbsent(commiter, __ -> 0);
                map.computeIfPresent(commiter, (__, v) -> v + 1);
            }
        }
        map.forEach((k, v) -> contributions.add(contributionService.addContribution(k, v, tags.get(index))));
    }

    private void analyzeTree(@NotNull CanonicalTreeParser tags1, @NotNull CanonicalTreeParser tags2, int index) throws IOException, GitAPIException {
        var entries = diff(tags1, tags2);
        for (var entry : entries) {
            //System.out.println(entry);
            BlameResult result = null;
            switch (entry.getChangeType()) {
                case MODIFY -> {
                    result = blaming(entry.getNewPath());
                }
                case ADD, COPY, RENAME -> result = blaming(entry.getNewPath());
                case DELETE -> result = blaming(entry.getOldPath());
            }
            if (result != null) {
                cache.add(0, result);
                contribute(result, entry, index);
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

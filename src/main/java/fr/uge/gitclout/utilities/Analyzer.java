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
import org.eclipse.jgit.diff.RawText;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.treewalk.TreeWalk;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Analyzer {
    private final Git git;
    private final Repo repo;
    private final List<Tag> tags;
    private final HashSet<Commiter> commiters;
    private final ContributionService contributionService;
    private final List<Contribution> contributions = new ArrayList<>();

    public Analyzer(@NotNull Git git, @NotNull Repo repo, @NotNull List<Tag> tags,
                    @NotNull HashSet<Commiter> commiters, @NotNull ContributionService contributionService) {
        this.git = git;
        this.repo = repo;
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
        var blameResult = blameCommand.call();
        if (blameResult == null) {
            return null;
        }
        return blameCommand.call();
    }

    private boolean isFileInRepository(String filePath) throws IOException {
        try (RevWalk revWalk = new RevWalk(git.getRepository())) {
            ObjectId head = git.getRepository().resolve(Constants.HEAD);
            if (head != null) {
                RevTree tree = revWalk.parseTree(head);
                TreeWalk treeWalk = TreeWalk.forPath(git.getRepository(), filePath, tree);
                return treeWalk != null;
            }
        }
        return false;
    }

    private Tag getTagFromRevCommit(@NotNull RevCommit commit) {
        for (var tag : tags) {
            if (tag.getObjId().equals(commit.getId())) {
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

    private void contribute(BlameResult result, DiffEntry entry) {
        var map = new HashMap<Commiter, Integer>();
        if (entry.getChangeType() == DiffEntry.ChangeType.MODIFY || entry.getChangeType() == DiffEntry.ChangeType.ADD) {
            for (var i = 0; i < result.getResultContents().size(); i++) {
                var commiter = getCommiterFromRevCommit(result.getSourceCommit(0));
                map.computeIfAbsent(commiter, __ -> 0);
                map.computeIfPresent(commiter, (__, v) -> v + 1);
            }
            map.forEach((k, v) -> contributions.add(contributionService.addContribution(k, v)));
        }
    }

    public List<Contribution> analyze(@NotNull List<Ref> tags, @NotNull RevWalk revWalk) throws GitAPIException, IOException {
        try (var reader = git.getRepository().newObjectReader()) {
            for (var i = 0; i < tags.size() - 1; i++) {
                var tags1 = createTree(reader, tags.get(i).getObjectId(), revWalk);
                var tags2 = createTree(reader, tags.get(i+1).getObjectId(), revWalk);
                var entries = diff(tags1, tags2);
                for (var entry : entries) {
                    var result = blaming(entry.getOldPath());
                    if (result != null) {
                        contribute(result, entry);
                    }
                }
            }
        }
        return contributions;
    }
}

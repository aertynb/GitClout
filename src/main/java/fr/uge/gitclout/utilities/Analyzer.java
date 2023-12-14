package fr.uge.gitclout.utilities;

import jakarta.validation.constraints.NotNull;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;

public class Analyzer {
    private final Git git;

    public Analyzer(@NotNull Git git) {
        this.git = git;
    }

    public void analyze(ObjectId id) throws GitAPIException {
        var result = git.blame().setFilePath("file.txt").setStartCommit(id).call();
        System.out.println(result.toString());
    }
}

package fr.uge.gitclout;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.util.io.DisabledOutputStream;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Repository {

    private void rmFiles(File directory) {
        for (var subfile: directory.listFiles()) {
            if (subfile.isDirectory()) {
                rmFiles(subfile);
            }
            subfile.delete();
        }
    }
    public static void main(String[] args) throws GitAPIException, IOException {
        Repository rep = new Repository();
        File file = new File("ressources/repo");
        if(file.exists()) {
            rep.rmFiles(file);
        }
        file.mkdir();
        try ( Git git = Git.cloneRepository()
                .setURI("https://github.com/localsend/localsend.git")
                .setDirectory(file)
                .call();)
        {
            LogCommand log = git.log();
            Iterable<RevCommit> commits = log.call();
            DiffFormatter diffFormatter = new DiffFormatter( DisabledOutputStream.INSTANCE );
            diffFormatter.setRepository(git.getRepository());

            for (RevCommit commit : commits) {
                if (commit.getParentCount() == 0) {
                    break;
                }
                List<DiffEntry> entries = diffFormatter.scan(commit.getParent(0).getTree(), commit.getTree());
                for (var entry : entries) {
                    System.out.println(entry.getChangeType());
                }
                String authorName = commit.getAuthorIdent().getName();
                String authorEmail = commit.getAuthorIdent().getEmailAddress();
                String commitMessage = commit.getFullMessage();


                System.out.println("Author: " + authorName + " (" + authorEmail + ")");
                System.out.println("Commit Message: " + commitMessage);
                System.out.println("------------------------------");
            }
        }
    }
}

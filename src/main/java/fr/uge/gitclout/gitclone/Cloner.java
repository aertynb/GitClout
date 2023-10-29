package fr.uge.gitclout.gitclone;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.PersonIdent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Cloner {
    final private Git git;
    final private LogCommand log;

    public Cloner(String URI, File directory) {
        Objects.requireNonNull(URI);
        Objects.requireNonNull(directory);
        if(directory.exists()) {
            rmFiles(directory);
        }
        git = initRepo(URI, directory);
        log = git.log();
    }

    private void rmFiles(File directory) {
        Objects.requireNonNull(directory);
        for (var subfile: Objects.requireNonNull(directory.listFiles())) {
            if (subfile.isDirectory()) {
                rmFiles(subfile);
            }
            if (!subfile.delete()) {
                throw new IllegalArgumentException();
            }
        }
    }

    public List<PersonIdent> getAllCommiter() throws GitAPIException {
        List<PersonIdent> list = new ArrayList<>();
        var commits = log.call();
        for (var commit : commits) {
            var person = commit.getAuthorIdent();
            if (!list.contains(person)) {
                list.add(person);
            }
        }
        return list;
    }

    private Git initRepo(String URI, File directory) {
        try { return Git.cloneRepository()
                .setURI(URI)
                .setDirectory(directory)
                .call();
        } catch (GitAPIException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /*public static void main(String[] args) throws GitAPIException, IOException {
        var rep = new Repository("https://github.com/localsend/localsend.git", new File("ressources/repo"));
        System.out.println(rep.getAllCommiter());
        /*Repository rep = new Repository();
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
    }*/
}

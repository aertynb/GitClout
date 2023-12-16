package fr.uge.gitclout.utilities;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.util.Objects;

public class Cloner {

    static public Git initRepository(String URI) throws GitAPIException {
        Objects.requireNonNull(URI);
        var array = URI.split("/");
        var name = array[array.length - 2]; // git name
        File directory = new File("ressources/repo/" + name);
        if (directory.exists()) {
            rmFiles(directory);
        }
        return Git.cloneRepository()
                .setDirectory(directory)
                .setURI(URI)
                .setBare(true)
                .call();
    }

    static public void rmFiles(File directory) {
        Objects.requireNonNull(directory);
        for (var subfile : Objects.requireNonNull(directory.listFiles())) {
            if (subfile.isDirectory()) {
                rmFiles(subfile);
            }
                if (!subfile.delete()) {
                throw new IllegalArgumentException();
            }
        }
    }
}
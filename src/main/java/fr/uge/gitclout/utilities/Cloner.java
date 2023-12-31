package fr.uge.gitclout.utilities;

import jakarta.validation.constraints.NotNull;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Utility class for cloning Git repositories and removing files/directories.
 */
public class Cloner {

    /**
     * Initializes a Git repository by cloning it from a given URI.
     *
     * @param URI The URI of the Git repository to clone.
     * @return A Git object representing the cloned repository.
     * @throws GitAPIException If an error occurs during Git operations.
     */
    static public Git initRepository(@NotNull String URI) throws GitAPIException, IOException {
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

    /**
     * Recursively removes files and directories in the specified directory.
     *
     * @param directory The directory whose contents need to be removed.
     */
    static public void rmFiles(@NotNull File directory) throws IOException {
        for (var subfile : Objects.requireNonNull(directory.listFiles())) {
            if (subfile.isDirectory()) {
                rmFiles(subfile); // Recursively remove subdirectory contents
            }
            subfile.delete();
        }
    }
}

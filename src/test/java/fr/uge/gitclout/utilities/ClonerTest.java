package fr.uge.gitclout.utilities;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ClonerTest {
    private Git git;
    private static final String REPO_URI = "https://github.com/Clement-Muth/criterion-unit-test-c";

    @BeforeEach
    public void setUp() throws GitAPIException {
        git = Cloner.initRepository(REPO_URI);
    }

    @AfterEach
    public void tearDown() {
        if (git != null) {
            git.close();
        }
    }

    @Test
    public void testInitRepository() {
        assertNotNull(git, "Git repository shouldn't be null");
        assertTrue(git.getRepository().getDirectory().exists(), "Repository directory should exist");
    }

    @Test
    public void testRmFiles() {
        var directory = git.getRepository().getDirectory().getParentFile();
        assertTrue(directory.exists());
        Cloner.rmFiles(directory);
        var files = directory.listFiles();
        assertNotNull(files);
        assertEquals(1, files.length);
    }

}
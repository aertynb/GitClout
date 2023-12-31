package fr.uge.gitclout.entity;

import fr.uge.gitclout.utilities.Cloner;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Constants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RepoTest {
    private Git git;
    private static final String REPO_URI = "https://gitlab.ow2.org/authzforce/xacml-json-model";

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
    public void testGetName() {
        var repo = new Repo("test");
        assertEquals("test", repo.getName());
    }

    @Test
    public void testSetAndGetCommiters() {
        var repo = new Repo("test");
        var listCommiters = new ArrayList<Commiter>();
        listCommiters.add(new Commiter("Jacque", "jacque179@gmail.com", repo));
        listCommiters.add(new Commiter("Jacque", "jacque179@gmail.com", repo));
        listCommiters.add(new Commiter("Sam", "sam734@gmail.com", repo));
        repo.setCommiters(listCommiters);
        assertAll(
                () -> assertNotNull(repo.getCommiters()),
                () -> assertEquals(3, repo.getCommiters().size()),
                () -> assertEquals("Sam", repo.getCommiters().getLast().getName())
        );
    }

    @Test
    public void testSetAndGetCommits() {
        var repo = new Repo("test");
        var commiter = new Commiter("Jacque", "jacque179@gmail.com", repo);
        var listCommits = new ArrayList<Commit>();
        listCommits.add(new Commit("Initialisation", commiter, repo));
        listCommits.add(new Commit("Ajout des dépendances", commiter, repo));
        listCommits.add(new Commit("Mise à jour", commiter, repo));
        listCommits.add(new Commit("Finalisation", commiter, repo));
        repo.setCommits(listCommits);
        assertAll(
                () -> assertNotNull(repo.getCommits()),
                () -> assertEquals(4, repo.getCommits().size())
        );
    }

    @Test
    public void testGetTags() {
        var repo = new Repo("test");
        try {
            var ref = git.getRepository().getRefDatabase().getRefsByPrefix(Constants.R_TAGS);
            var listTags = new ArrayList<Tag>();
            listTags.add(new Tag("tag1", ref.get(0).getObjectId(), repo));
            listTags.add(new Tag("tag2", ref.get(1).getObjectId(), repo));
            listTags.add(new Tag("tag3", ref.get(2).getObjectId(), repo));
            listTags.add(new Tag("tag4", ref.get(3).getObjectId(), repo));
            repo.setTags(listTags);
            assertAll(
                    () -> assertNull(repo.getCommiters()),
                    () -> assertEquals(4, repo.getTags().size()),
                    () -> assertEquals("tag2", repo.getTags().get(1).getName())
            );
        } catch(IOException e) {
            throw new AssertionError(e);
        }
    }


}

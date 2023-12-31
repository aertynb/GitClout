package fr.uge.gitclout.entity;

import fr.uge.gitclout.utilities.Cloner;
import fr.uge.gitclout.utilities.Language;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Constants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TagTest {
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
        var repo = new Repo("asm");
        try {
            var ref = git.getRepository().getRefDatabase().getRefsByPrefix(Constants.R_TAGS).getFirst();
            var tag = new Tag("tag/1.0", ref.getObjectId(), repo);
            assertEquals(tag.getName(), "tag/1.0");
        } catch(IOException e) {
            throw new AssertionError(e);
        }
    }

    @Test
    public void testGetObjId() {
        var repo = new Repo("asm");
        try {
            var ref = git.getRepository().getRefDatabase().getRefsByPrefix(Constants.R_TAGS).getFirst();
            var tag = new Tag(ref.getName(), ref.getObjectId(), repo);
            assertNotNull(tag.getObjId());
        } catch(IOException e) {
            throw new AssertionError(e);
        }
    }

    @Test
    public void testAddAndGetContributions() {
        var repo = new Repo("asm");
        var commiter = new Commiter("Bob", "bob999@gmail.com", repo);
        try {
            var ref = git.getRepository().getRefDatabase().getRefsByPrefix(Constants.R_TAGS).getFirst();
            var tag = new Tag(ref.getName(), ref.getObjectId(), repo);
            var contribution = new Contribution(commiter, 10, tag, Language.JAVA);
            var contribution2 = new Contribution(commiter, 20, tag, Language.TYPESCRIPT);
            tag.addContributions(contribution);
            tag.addContributions(contribution2);
            assertAll(
                    () -> assertNotNull(tag.getContributions()),
                    () -> assertEquals(2, tag.getContributions().size())
            );
        } catch(IOException e) {
            throw new AssertionError(e);
        }
    }
}

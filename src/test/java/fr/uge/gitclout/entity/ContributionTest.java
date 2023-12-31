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
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

@SpringBootTest
public class ContributionTest {
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
    private final Repo repo = new Repo("test");

    @Test
    public void testSetAndGetCommiterAndTagAndAddedLinesAndLanguage() {
        var commiter = new Commiter("Paul", "paul021@gmail.com", repo);
        var commiter2 = new Commiter("Sam", "sam734@gmail.com", repo);
        try {
            var ref = git.getRepository().getRefDatabase().getRefsByPrefix(Constants.R_TAGS);
            var tag = new Tag("tag1", ref.getFirst().getObjectId(), repo);
            var tag2 = new Tag("tag2", ref.get(1).getObjectId(), repo);
            var contribution = new Contribution(commiter, 5, tag, Language.PYTHON);
            var sameContribution = new Contribution(commiter, 5, tag, Language.PYTHON);;
            sameContribution.setCommiter(commiter2);
            sameContribution.setTag(tag2);
            sameContribution.setAddedLines(7);
            assertAll(
                    () -> assertEquals(commiter, contribution.getCommiter()),
                    () -> assertEquals(tag, contribution.getTag()),
                    () -> assertEquals(5, contribution.getAddedLines()),
                    () -> assertEquals(Language.PYTHON, contribution.getLanguage()),
                    () -> assertNotEquals(contribution, sameContribution),
                    () -> assertEquals(12, sameContribution.getAddedLines())
            );
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }
}

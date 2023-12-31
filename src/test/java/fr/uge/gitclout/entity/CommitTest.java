package fr.uge.gitclout.entity;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CommitTest {
    @Test
    public void testGetCommiter() {
        var repo = new Repo("asm");
        var commit = new Commit("C'est fait", new Commiter("Jacque", "jacque179@gmail.com", repo), repo);
        assertNotNull(commit.getCommiter());
    }

    @Test
    public void testSetCommiter() {
        var repo = new Repo("asm");
        var commiter = new Commiter("Jacque", "jacque179@gmail.com", repo);
        var commit = new Commit("C'est fait", commiter, repo);
        commit.setCommiter(new Commiter("Nicolas", "nicolas94@gmail.com", repo));
        assertAll(
                () -> assertNotNull(commit.getCommiter()),
                () -> assertNotEquals(commit.getCommiter(), commiter)
        );
    }

    @Test
    public void testEquals() {
        var repo = new Repo("asm");
        var commiter = new Commiter("Jacque", "jacque179@gmail.com", repo);
        var commit = new Commit("C'est fini", commiter, repo);
        var commit2 = new Commit("C'est fini", commiter, repo);
        var commit3 = new Commit("C'est pas fini", commiter, repo);
        System.out.println(commit);
        System.out.println(commit3);
        assertAll(
                () -> assertEquals(commit, commit2),
                () -> assertNotEquals(commit, commiter),
                () -> assertNotEquals(commit, commit3)
        );
    }

    @Test
    public void testHashCode() {
        var repo = new Repo("asm");
        var commiter = new Commiter("Jacque", "jacque179@gmail.com", repo);
        var commit = new Commit("C'est fini", commiter, repo);
        var commit2 = new Commit("C'est fini", commiter, repo);
        assertAll(
                () -> assertEquals(commit.hashCode(), commit.hashCode()),
                () -> assertEquals(commit2.hashCode(), commit.hashCode()),
                () -> assertEquals(commit, commit2),
                () -> assertEquals(commit.hashCode(), commit2.hashCode())
        );
    }

    @Test
    public void testString() {
        var repo = new Repo("asm");
        var commiter = new Commiter("Jacque", "jacque179@gmail.com", repo);
        var commit = new Commit("C'est fait", commiter, repo);
        assertEquals("Commit{id=null, message='C'est fait', commiter=" + commiter + '}', commit.toString());
    }

}

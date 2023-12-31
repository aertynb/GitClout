package fr.uge.gitclout.entity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CommiterTest {

    private final Repo repo = new Repo("test");

    @Test
    public void testGetName() {
        var commiter = new Commiter("Jacque", "jacque179@gmail.com", repo);
        assertEquals(commiter.getName(), "Jacque");
    }

    @Test
    public void testGetEmail() {
        var commiter = new Commiter("Jacque", "jacque179@gmail.com", repo);
        assertEquals(commiter.getEmail(), "jacque179@gmail.com");
    }

    @Test
    public void setRepositoryTest() {
        var commiter = new Commiter("Jacque", "jacque179@gmail.com", repo);
        var newRepo = new Repo("test2");
        commiter.setRepository(newRepo);
        assertNotEquals(commiter.getRepository(), repo);
    }

    @Test
    public void testEquals() {
        var commiter = new Commiter("Jacque", "jacque179@gmail.com", repo);
        var commiter2 = new Commiter("Jacque", "jacque179@gmail.com", repo);
        var commiter3 = new Commiter("Sam", "sam734@gmail.com", repo);
        assertAll(
                () -> assertEquals(commiter, commiter2),
                () -> assertEquals(commiter2, commiter),
                () -> assertNotEquals(commiter, commiter3),
                () -> assertNotEquals(commiter3, commiter)
        );
    }

    @Test
    public void testHashCode() {
        var commiter = new Commiter("Jacque", "jacque179@gmail.com", repo);
        var commiter2 = new Commiter("Jacque", "jacque179@gmail.com", repo);
        var commiter3 = new Commiter("Sam", "sam734@gmail.com", repo);
        assertAll(
                () -> assertEquals(commiter.hashCode(), commiter2.hashCode()),
                () -> assertEquals(commiter2.hashCode(), commiter.hashCode()),
                () -> assertNotEquals(commiter.hashCode(), commiter3.hashCode()),
                () -> assertNotEquals(commiter3.hashCode(), commiter.hashCode())
        );
    }
}
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class RepositoryTest {
    private Repository repo1;
    private Repository repo2;

    /**
     * NOTE: The following test suite assumes that getRepoHead(), commit(), and size()
     *       are implemented correctly.
     */

    @BeforeEach
    public void setUp() {
        repo1 = new Repository("repo1");
        repo2 = new Repository("repo2");
        Repository.Commit.resetIds();
    }

    @Test
    @DisplayName("Test getHistory()")
    public void getHistory() {
        // Initialize commit messages
        String[] commitMessages = new String[]{"Initial commit.", "Updated method documentation.",
                                                "Removed unnecessary object creation."};

        // Commit the commit messages to repo1
        for (int i = 0; i < commitMessages.length; i++) {
            String commitMessage = commitMessages[i];
            repo1.commit(commitMessage);

            // Assert that the current commit id is at the repository's head
            // We know our ids increment from 0, meaning we can just use i as our id
            assertEquals("" + i, repo1.getRepoHead());
        }

        assertEquals(repo1.getRepoSize(), commitMessages.length);

        // This is the method we are testing for. First, we'll obtain the 2 most recent commits
        // that have been made to repo1.
        String repositoryHistory = repo1.getHistory(2);
        String[] commits = repositoryHistory.split("\n");

        // Verify that getHistory() only returned 2 commits.
        assertEquals(commits.length, 2);

        // Verify that the 2 commits have the correct commit message and commit id
        for (int i = 0; i < commits.length; i++) {
            String commit = commits[i];

            // Old commit messages/ids are on the left and the more recent commit messages/ids are
            // on the right so need to traverse from right to left to ensure that 
            // getHistory() returned the 2 most recent commits.
            int backwardsIndex = (commitMessages.length - 1) - i;
            String commitMessage = commitMessages[backwardsIndex];

            assertTrue(commit.contains(commitMessage));
            assertTrue(commit.contains("" + backwardsIndex));
        }
    }

    @Test
    @DisplayName("Test drop() (empty case)")
    public void testDropEmpty() {
        assertFalse(repo1.drop("123"));
    }

    @Test
    @DisplayName("Test drop() (front case)")
    public void testDropFront() {
        assertEquals(repo1.getRepoSize(), 0);
        // Initialize commit messages
        String[] commitMessages = new String[]{"First commit.", "Added unit tests."};

        // Commit to repo1 - ID = "0"
        repo1.commit(commitMessages[0]);

        // Commit to repo2 - ID = "1"
        repo2.commit(commitMessages[1]);

        // Assert that repo1 successfully dropped "0"
        assertTrue(repo1.drop("0"));
        assertEquals(repo1.getRepoSize(), 0);
        
        // Assert that repo2 does not drop "0" but drops "1"
        // (Note that the commit ID increments regardless of the repository!)
        assertFalse(repo2.drop("0"));
        assertTrue(repo2.drop("1"));
        assertEquals(repo2.getRepoSize(), 0);
    }

    /**
     * SYNCHRONIZE JUNIT TESTS START HERE
     */

    @Test
    @DisplayName("Test synchronize() (empty case 1)")
    public void testSynchronizeThisEmpty() throws InterruptedException {
        // Initialize commit messages
        String[] commitMessages = new String[]{"Initial commit.", "Updated method documentation.",
                                                "Removed unnecessary object creation."};
        // Commit the commit messages to repo2
        for (int i = 0; i < commitMessages.length; i++) {
            String commitMessage = commitMessages[i];
            repo2.commit(commitMessage);
            Thread.sleep(1);
        }
        repo1.synchronize(repo2);
        // Assert that repo1 has size 3
        assertEquals(repo1.getRepoSize(), 3);

        // Assert that repo2 is empty
        assertEquals(repo2.getRepoSize(), 0);

        // Assert that repo1 has the correct head
        assertTrue(repo1.toString().contains("Removed unnecessary object creation."));
    }

    @Test
    @DisplayName("Test synchronize() (empty case 2)")
    public void testSynchronizeOtherEmpty() throws InterruptedException {
        // Initialize commit messages
        String[] commitMessages = new String[]{"Initial commit.", "Updated method documentation.",
                                                "Removed unnecessary object creation."};
        // Commit the commit messages to repo1
        for (int i = 0; i < commitMessages.length; i++) {
            String commitMessage = commitMessages[i];
            repo1.commit(commitMessage);
            Thread.sleep(1);
        }
        repo1.synchronize(repo2);
        // Assert that repo1 has size 3
        assertEquals(repo1.getRepoSize(), 3);

        // Assert that repo2 is empty
        assertEquals(repo2.getRepoSize(), 0);

        // Assert that repo1 has the correct head
        assertTrue(repo1.toString().contains("Removed unnecessary object creation."));
    }

    @Test
    @DisplayName("Test synchronize() (end case))")
    public void testSynchronizeThisLarger() throws InterruptedException {
        // Initialize smaller list of commit messages
        String[] smallerCommitMessages = new String[]{"Other initial commit.", 
                                                "Other updated method documentation.", 
                                                "Other removed unnecessary object creation."};
        // Initialize larger list of commit messages
        String[] largerCommitMessages = new String[]{"This initial commit.", 
                                                "This updated method documentation.",
                                                "This removed unnecessary object creation.",
                                                "This edit README.",
                                                "This upload README."};
        // Commit the larger commit message list to repo1
        for (int i = 0; i < largerCommitMessages.length; i++) {
            String commitMessage = largerCommitMessages[i];
            repo1.commit(commitMessage);
            Thread.sleep(1);
        }
        // Commit the smaller commit message list to repo2
        for (int i = 0; i < smallerCommitMessages.length; i++) {
            String commitMessage = smallerCommitMessages[i];
            repo2.commit(commitMessage);
            Thread.sleep(1);
        }
        repo1.synchronize(repo2);
        // Assert that repo1 has size 8
        assertEquals(repo1.getRepoSize(), 8);

        // Assert that repo2 is empty
        assertEquals(repo2.getRepoSize(), 0);

        // Assert that repo1 has the correct head
        assertTrue(repo1.toString().contains("Other removed unnecessary object creation."));
    }

    @Test
    @DisplayName("Test synchronize() (end case)")
    public void testSynchronizeOtherLarger() throws InterruptedException {
        // Initialize smaller list of commit messages
        String[] smallerCommitMessages = new String[]{"This initial commit.", 
                                                "This updated method documentation.",
                                                "This removed unnecessary object creation."};
        // Initialize larger list of commit messages
        String[] largerCommitMessages = new String[]{"Other initial commit.", 
                                                "Other updated method documentation.",
                                                "Other removed unnecessary object creation.", 
                                                "Other edit README.",
                                                "Other upload README."};
        // Commit the smaller commit message list to repo1
        for (int i = 0; i < smallerCommitMessages.length; i++) {
            String commitMessage = smallerCommitMessages[i];
            repo1.commit(commitMessage);
            Thread.sleep(1);
        }
        // Commit the larger commit message list to repo2
        for (int i = 0; i < largerCommitMessages.length; i++) {
            String commitMessage = largerCommitMessages[i];
            repo2.commit(commitMessage);
            Thread.sleep(1);
        }
        repo1.synchronize(repo2);
        // Assert that repo1 has size 8
        assertEquals(repo1.getRepoSize(), 8);

        // Assert that repo2 is empty
        assertEquals(repo2.getRepoSize(), 0);

        // Assert that repo1 has the correct head
        assertTrue(repo1.toString().contains("Other upload README."));
    }
    
    @TestS
    @DisplayName("Test synchronize() (front case)")
    public void testSynchronizeThisHeadLargerTimeStamp() throws InterruptedException {
        // Initialize list of commits to be committed earlier
        String[] earlierCommitMessages = new String[]{"Other initial commit.", 
                                                    "Other updated method documentation.",
                                                    "Other removed unnecessary object creation."};
        // Initialize list of commits to be committed later
        String[] laterCommitMessages = new String[]{"This initial commit.", 
                                                    "This updated method documentation.",
                                                    "This removed unnecessary object creation."};
        // Commit the earlier commit message list to repo2
        for (int i = 0; i < earlierCommitMessages.length; i++) {
            String commitMessage = earlierCommitMessages[i];
            repo2.commit(commitMessage);
            Thread.sleep(1);
        }
        // Commit the later commit message list to repo1
        for (int i = 0; i < laterCommitMessages.length; i++) {
            String commitMessage = laterCommitMessages[i];
            repo1.commit(commitMessage);
            Thread.sleep(1);
        }
        repo1.synchronize(repo2);
        // Assert that repo1 has size 8
        assertEquals(repo1.getRepoSize(), 6);

        // Assert that repo2 is empty
        assertEquals(repo2.getRepoSize(), 0);

        // Assert that repo1 has the correct head
        assertTrue(repo1.toString().contains("This removed unnecessary object creation."));
    }
    
    @Test
    @DisplayName("Test synchronize() (front case)")
    public void testSynchronizeOtherHeadLargerTimeStamp() throws InterruptedException {
        // Initialize list of commits to be committed earlier
        String[] earlierCommitMessages = new String[]{"This initial commit.", 
                                                    "This updated method documentation.",
                                                    "This removed unnecessary object creation."};
        // Initialize list of commits to be committed later
        String[] laterCommitMessages = new String[]{"Other initial commit.", 
                                                    "Other updated method documentation.",
                                                    "Other removed unnecessary object creation."};
        // Commit the earlier commit message list to repo1
        for (int i = 0; i < earlierCommitMessages.length; i++) {
            String commitMessage = earlierCommitMessages[i];
            repo1.commit(commitMessage);
            Thread.sleep(1);
        }
        // Commit the later commit message list to repo2
        for (int i = 0; i < laterCommitMessages.length; i++) {
            String commitMessage = laterCommitMessages[i];
            repo2.commit(commitMessage);
            Thread.sleep(1);
        }
        repo1.synchronize(repo2);
        // Assert that repo1 has size 8
        assertEquals(repo1.getRepoSize(), 6);

        // Assert that repo2 is empty
        assertEquals(repo2.getRepoSize(), 0);

        // Assert that repo1 has the correct head
        assertTrue(repo1.toString().contains("Other removed unnecessary object creation."));
    }
}

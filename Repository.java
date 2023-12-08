// Jeffery Zhang
// TA: Jake Page
// CSE 123
// Due: November 1st, 2023
// P1: Mini Git

// A class to represent a simple collection of changes
// to documents, starting with the most recent 
// and ending with the oldest change.

import java.util.*;
import java.text.SimpleDateFormat;

public class Repository {
	private Commit front;
	private String name;
    
	// (B) Constructs a new Repository - a set of documents
	// and their histories - using a String name (P) as a parameter.
	// Throws an IllegalArgumentException (E) if the given name
	// is null or empty.
	public Repository(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Invalid Name: Empty or null");
        }
		front = null;
		this.name = name;
	}
	
    // Creates a new commit in the repository (B), given a
    // representative message for the commit (P).
    // Returns the ID of the created commit (R).
	public String commit(String message) {
		if (front == null) {
            front = new Commit(message);
        } else {
            Commit temp = front;
            front = new Commit(message, temp);
        }
        return front.id;
	}

    // Returns the ID of the current head of this repository (B, R).
    // Returns null if no commits (R).
	public String getRepoHead() {
        if (front == null) {
            return null;
        } else {
            return front.id;
        }
	}
	
    // Returns a string representation of the repository,
    // including its name and the head ID (B, R). If no commits were
    // made, the string indicates as such.
	public String toString() {
        if (front == null) {
            return name + " - No commits";
        }
		return name + " - Current head: " + front.toString();
	}
	
    // Given a targetId (P), checks if there exists a commit
    // with the given ID (B). Returns true if this is the case,
    // false if not (R).
	public boolean contains(String targetId) {
		Commit curr = front;
        while (curr != null) {
            if (curr.id.equals(targetId)) {
                return true;
            }
            curr = curr.past;
        }
        return false;
	}
	
    // Returns the size of the repository,
    // based on how many commits there are (B, R).
	public int getRepoSize() {
		int size = 0;
        Commit curr = front;
        while (curr != null) {
            size++;
            curr = curr.past;
        }
        return size;
	}

    // Takes in an int n parameter (P) and returns a string consisting 
    // of the string representations of the most recent n commits 
    // in this repository, or all of them if n is larger than the repository
    // size. **Returns an empty String if current Repo has no commits** (B, R). 
    // Throws an IllegalArgumentException identifier if n is negative or equals 0 (E).
	public String getHistory(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Invalid Number: Must be a non-negative number.");
        }
		String history = "";
        Commit curr = front;
        while (curr != null && n != 0) {
            history += curr.toString() + "\n";
            n--;
            curr = curr.past;
        }
        return history;
	}
	
    // Removes the first commit with ID targetId from this repository (B), 
    // given the String targetId parameter (P). Returns true if the 
    // commit was successfully dropped, and false if there is no
    // commit that matches the given ID in the repository (R).
	public boolean drop(String targetId) {
        if (front != null) {
            if (front.id.equals(targetId)) {
                front = front.past;
                return true;
            } else {
                Commit curr = front;
                while (curr.past != null) {
                    if (curr.past.id.equals(targetId)) {
                        curr.past = curr.past.past;
                        return true;
                    } else {
                        curr = curr.past;
                    }
                }
            }
        }
        return false;
	}
    
	// Takes all the commits in the other repository (the method
    // takes the Other repository as a parameter) (P) and moves them 
    // into this repository, combining the two repository histories
    // such that chronological order is preserved (B).
    public void synchronize(Repository other) {
        if (front == null) {
            front = other.front;
            other.front = null;
        } else if (other.front != null) {
            if (front.timeStamp < other.front.timeStamp) {
                Commit temp = other.front;
                other.front = other.front.past;
                temp.past = front;
                front = temp;
            }
            Commit curr = front;
            while (curr.past != null && other.front != null) {
                if (curr.past.timeStamp < other.front.timeStamp) {
                    Commit temp = other.front;
                    other.front = other.front.past;
                    temp.past = curr.past;
                    curr.past = temp;
                }
                curr = curr.past;
            }
            if (other.front != null) {
                curr.past = other.front;
                other.front = null;
            }
        }    
    }
	
    /**
     * DO NOT MODIFY
     * A class that represents a single commit in the repository.
     * Commits are characterized by an identifier, a commit message,
     * and the time that the commit was made. A commit also stores
     * a reference to the immediately previous commit if it exists.
     *
     * Staff Note: You may notice that the comments in this 
     * class openly mention the fields of the class. This is fine 
     * because the fields of the Commit class are public. In general, 
     * be careful about revealing implementation details!
     */
    public class Commit {

        private static int currentCommitID;

        /**
         * The time, in milliseconds, at which this commit was created.
         */
        public final long timeStamp;

        /**
         * A unique identifier for this commit.
         */
        public final String id;

        /**
         * A message describing the changes made in this commit.
         */
        public final String message;

        /**
         * A reference to the previous commit, if it exists. Otherwise, null.
         */
        public Commit past;

        /**
         * Constructs a commit object. The unique identifier and timestamp
         * are automatically generated.
         * @param message A message describing the changes made in this commit.
         * @param past A reference to the commit made immediately before this
         *             commit.
         */
        public Commit(String message, Commit past) {
            this.id = "" + currentCommitID++;
            this.message = message;
            this.timeStamp = System.currentTimeMillis();
            this.past = past;
        }

        /**
         * Constructs a commit object with no previous commit. The unique
         * identifier and timestamp are automatically generated.
         * @param message A message describing the changes made in this commit.
         */
        public Commit(String message) {
            this(message, null);
        }

        /**
         * Returns a string representation of this commit. The string
         * representation consists of this commit's unique identifier,
         * timestamp, and message, in the following form:
         *      "[identifier] at [timestamp]: [message]"
         * @return The string representation of this collection.
         */
        @Override
        public String toString() {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
            Date date = new Date(timeStamp);

            return id + " at " + formatter.format(date) + ": " + message;
        }

        /**
        * Resets the IDs of the commit nodes such that they reset to 0.
        * Primarily for testing purposes.
        */
        public static void resetIds() {
            Commit.currentCommitID = 0;
        }
    }
}


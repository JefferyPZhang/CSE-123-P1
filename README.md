# Programming Assignment 1: Mini-Git

**Assignment Spec: **

"In our system, as in Git, a set of documents and their histories are referred to as a repository. Each revision within a repository is referred to as a commit. You will implement a class called Repository that supports a subset of the operations supported by real Git repositories. (We will not be dealing with features such as branching or remote repositories. We will assume histories are fairly linear and mostly take place in a single, local repository.)"

**Required Operations**
Your Repository class must include the following methods:

public Repository(String name)
Create a new, empty repository with the specified name

If the name is null or empty, throw an IllegalArgumentException

public String getRepoHead()
Return the ID of the current head of this repository.

If the head is null, return null

public int getRepoSize()
Return the number of commits in the repository

public String toString()
Return a string representation of this repository in the following format:

<name> - Current head: <head>

<head> should be the result of calling toString() on the head commit.

If there are no commits in this repository, instead return <name> - No commits

public boolean contains(String targetId)
Return true if the commit with ID targetId is in the repository, false if not.

public String getHistory(int n)
Return a string consisting of the String representations of the most recent n commits in this repository, with the most recent first. Commits should be separated by a newline (\n) character.

If there are fewer than n commits in this repository, return them all.

If there are no commits in this repository, return the empty string.

If n is non-positive, throw an IllegalArgumentException.

public String commit(String message)
Create a new commit with the given message, add it to this repository.

The new commit should become the new head of this repository, preserving the history behind it.

Return the ID of the new commit.

public boolean drop(String targetId)
Remove the commit with ID targetId from this repository, maintaining the rest of the history.

Returns true if the commit was successfully dropped, and false if there is no commit that matches the given ID in the repository.

public void synchronize(Repository other)
Takes all the commits in the other repository and moves them into this repository, combining the two repository histories such that chronological order is preserved. That is, after executing this method, this repository should contain all commits that were from this and other, and the commits should be ordered in timestamp order from most recent to least recent.

If the other repository is empty, this repository should remain unchanged.

If this repository is empty, all commits in the other repository should be moved into this repository.

At the end of this method's execution, other should be an empty repository in all cases.

You should not construct any new Commit objects to implement this method. You may however create as many references as you like.

package com.jetbrains.git;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class GitTest {
    @Test
    void commitCreationTest() {
        Git git = new Git();
        Tree tree = git.tree();
        Commit first_commit = git.commit(tree, "message", "author");

        assertNull(first_commit.parent);
        assertEquals(first_commit, git.head);

    }

    @Test
    void commitHierarchyTest() {
        Git git = new Git();
        Tree first_tree = git.tree();
        Commit first_commit = git.commit(first_tree, "first message", "first author");

        Tree second_tree = git.tree();
        second_tree.add(first_tree);
        Commit second_commit = git.commit(second_tree, "second message", "second author");

        assertEquals(first_commit, second_commit.parent);
        assertEquals(second_commit, git.head);
    }

    @Test
    void gitLogTest() {
        Git git = new Git();
        Tree first_tree = git.tree();
        Commit first_commit = git.commit(first_tree, "first message", "first author");

        Tree second_tree = git.tree();
        second_tree.add(first_tree);
        Commit second_commit = git.commit(second_tree, "second message", "second author");

        assertNotEquals(first_commit, second_commit);
        git.log();
    }


    @Test
    void searchByHashTest() {
        Git git = new Git();
        Tree tree = git.tree();
        Commit commit = git.commit(tree, "", "");
        String hash = commit.hash();

        assertEquals(commit, git.findByHash(hash));
    }


    @Test
    void searchByMessageTest() {
        Git git = new Git();
        Tree tree = git.tree();

        String message = "some message";
        Commit commit = git.commit(tree, message, "");

        assertEquals(commit, git.findByMessage(message));
    }

    @Test
    void searchByAuthorTest() {
        Git git = new Git();
        Tree tree = git.tree();

        String author = "some author";
        Commit commit = git.commit(tree, "", author);

        assertEquals(commit, git.findByAuthor(author));
    }

    @Test
    void blobCreationTest() {
        Git git = new Git();
        String data = "some data";
        Blob blob1 = git.blob(data);
        Blob blob2 = git.blob(data);

        assertEquals(blob1, blob2);
        assertEquals(1, git.blobs.size());
    }

    @Test
    void treeCreationTest() {
        Git git = new Git();
        Tree tree = git.tree();
        String hash_before = tree.hash();

        assertEquals(tree.getSize(), 0);

        String data = "some data";
        Blob blob1 = git.blob(data);
        tree.add(blob1);

        assertNotEquals(hash_before, tree.hash());
        assertEquals(1, tree.getSize());

    }

    @Test
    void treeThrowExceptionTest() {
        Git git = new Git();

        Blob blob1 = git.blob("data1");
        Tree tree = git.tree();
        tree.add(blob1);

        Commit first_commit = git.commit(tree, "first commit", "author");

        Blob blob2 = git.blob("data2");

        assertTrue(first_commit.mainTree.isLocked());
        assertThrows(IllegalStateException.class, () -> tree.add(blob2));
        assertThrows(IllegalStateException.class, () -> git.commit(tree, "second commit", "author"));

    }

    @Test
    void treeRemoveTest() {
        Git git = new Git();

        Blob blob1 = git.blob("data1");
        Tree tree = git.tree();
        tree.add(blob1);
        String hash = tree.hash();

        Blob blob2 = git.blob("data2");
        tree.add(blob2);

        assertNotEquals(hash, tree.hash());

        tree.remove(blob2);

        assertEquals(hash, tree.hash());
    }

    @Test
    void treeRemoveExceptionTest() {
        Git git = new Git();

        Blob blob1 = git.blob("data1");
        Tree tree = git.tree();
        tree.add(blob1);

        Blob blob2 = git.blob("data2");

        assertThrows(IllegalStateException.class, () -> tree.remove(blob2));

        Commit commit = git.commit(tree, "", "");

        assertThrows(IllegalStateException.class, () -> tree.remove(blob1));
        assertEquals(tree, commit.mainTree);

    }

    @Test
    void searchByAuthorExceptionTest() {
        Git git = new Git();
        Tree tree = git.tree();

        String wrong_author = "some author";
        Commit commit = git.commit(tree, "", "another author");

        assertNotEquals(commit.author, wrong_author);
        assertThrows(IllegalArgumentException.class, () -> git.findByAuthor(wrong_author));
    }

    @Test
    void searchByMessageExceptionTest() {
        Git git = new Git();
        Tree tree = git.tree();

        String wrong_message = "some message";
        Commit commit = git.commit(tree, "another message", "");

        assertNotEquals(commit.message, wrong_message);
        assertThrows(IllegalArgumentException.class, () -> git.findByMessage(wrong_message));
    }

    @Test
    void searchByHashExceptionTest() {
        Git git = new Git();
        Tree tree = git.tree();
        Commit commit = git.commit(tree, "", "");

        String wrong_hash = "hash";

        assertNotEquals(commit.hash, wrong_hash);
        assertThrows(IllegalArgumentException.class, () -> git.findByHash(wrong_hash));

    }

}

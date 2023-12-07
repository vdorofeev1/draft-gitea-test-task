package com.jetbrains.git;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.HashMap;
import java.util.Map;

public class Git {
    Commit head;
    protected final Map<String, Blob> blobs;

    public Git() {
        this.head = null;
        this.blobs = new HashMap<>();
    }

    public Commit commit(Tree tree, String message, String author) {
        if (tree.isNotLocked()) {
            Commit newCommit = new Commit(tree, message, author, head);
            head = newCommit;
            tree.lock();
            return newCommit;
        }
        else {
            throw new IllegalStateException("Cannot create commits out of locked tree");
        }
    }

    public void log() {
        Commit current = head;
        while (current != null) {
            current.printContents();
            current = current.parent;
        }
    }

    public Commit findByHash(String hash) {
        Commit current = head;
        while (current != null && !current.hash.equals(hash)) {
            current = current.parent;
        }

        if (current == null) {
            throw new IllegalArgumentException("Commit with hash '" + hash + "' not found.");
        }
        return current;
    }

    public Commit findByMessage(String message) {
        Commit current = head;
        while (current != null && !current.message.equals(message)) {
            current = current.parent;
        }

        if (current == null) {
            throw new IllegalArgumentException("Commit with message '" + message + "' not found.");
        }
        return current;
    }

    public Commit findByAuthor(String author) {
        Commit current = head;
        while (current != null && !current.author.equals(author)) {
            current = current.parent;
        }

        if (current == null) {
            throw new IllegalArgumentException("Commit with author '" + author + "' not found.");
        }
        return current;
    }

    public Tree tree() {
        return new Tree();
    }

    public Blob blob(String data) {
        String hash = Git.sha1(data);
        if (blobs.containsKey(hash)) {
            return blobs.get(hash);
        } else {
            Blob newBlob = new Blob(data, hash);
            blobs.put(hash, newBlob);
            return newBlob;
        }
    }

    protected static String sha1(String data) {
        return DigestUtils.sha1Hex(data.getBytes());
    }

}

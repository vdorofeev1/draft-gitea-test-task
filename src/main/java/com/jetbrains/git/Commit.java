package com.jetbrains.git;

import java.time.LocalDateTime;

public final class Commit implements Hashable{

    final Commit parent;
    final Tree mainTree;
    final String author;
    final String message;
    private final LocalDateTime commitTime;
    final String hash;

    Commit(Tree tree, String message, String author, Commit parent) {
        this.parent = parent;
        this.mainTree = tree;
        this.message = message;
        this.author = author;
        this.commitTime = getCurrentDateTime();
        this.hash = calculateHash();
    }

    private String calculateHash() {
        String dataToHash = mainTree.hash() +
                message +
                author +
                commitTime;

        return Git.sha1(dataToHash);

    }

    private LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }

    @Override
    public String hash() {
        return hash;
    }

    public void printContents() {
        System.out.println("\n" + "hash: " + hash);
        System.out.println("tree: " + mainTree.hash());
        System.out.println("author: " + author);
        System.out.println("message: " + message);
        System.out.println("parent: " + (parent != null ? parent.hash : "null") + "\n");

    }
}

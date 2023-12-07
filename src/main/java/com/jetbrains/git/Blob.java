package com.jetbrains.git;

public final class Blob implements Hashable {
    private final String data;
    private final String hash;

    Blob(String data, String hash) {
        this.data = data;
        this.hash = hash;
    }

    public Blob(String data) {
        this.data = data;
        this.hash = calculateHash(data);
    }

    private String calculateHash(String data) {
        return Git.sha1(data);
    }
    @Override
    public String hash() {
        return hash;
    }

}

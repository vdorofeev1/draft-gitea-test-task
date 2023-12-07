package com.jetbrains.git;

import java.util.ArrayList;

public final class Tree implements Hashable{
    private final ArrayList<Hashable> objects;
    private String hash;
    private boolean locked;

    Tree() {
        this.objects = new ArrayList<>();
        this.hash = calculateHash();
        this.locked = false;
    }

    void add(Hashable obj) {
        if (!locked) {
            objects.add(obj);
            hash = calculateHash();
        }
        else {
            throw new IllegalStateException("Cannot add objects to a locked Tree");
        }
    }

    void remove(Hashable obj) {
        if (!locked) {
            if (objects.contains(obj)) {
                objects.remove(obj);
                hash = calculateHash();
            }
            else {
                throw new IllegalStateException("Tree does not contain this objects");
            }
        }
        else {
            throw new IllegalStateException("Cannot remove objects from a locked tree");
        }
    }

    int getSize() {
        return objects.size();
    }

    boolean isNotLocked() {
        return !locked;
    }

    boolean isLocked() {
        return locked;
    }

    void lock() {
        locked = true;
    }


    private String calculateHash() {
        StringBuilder dataToHash = new StringBuilder();
        for (Hashable entity: objects) {
            dataToHash.append(entity.hash());
        }
        return Git.sha1(dataToHash.toString());
    }

    @Override
    public String hash() {
        return hash;
    }
}

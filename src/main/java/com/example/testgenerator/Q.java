package com.example.testgenerator;

public class Q {
    private int id;
    private String name;
    private Q parent;
    private Q cloneOf = null;
    private static int cloneNum = 1;

    public Q(int id, String name, Q parent) {
        this.id = id;
        this.name = name;
        this.parent = parent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Q getParent() {
        return parent;
    }

    public void setParent(Q parent) {
        this.parent = parent;
    }

    public Q getOrigin() {
        return cloneOf;
    }

    public void setCloneOf(Q origin) {
        this.cloneOf = origin;
    }

    public boolean isClone(Q question) {
        if (question == cloneOf) {
            return true;
        }
        return false;
    }

    public Q makeClone() {
        int newID = this.id + cloneNum;
        cloneNum++;
        Q newClone = new Q(newID, this.name, this.parent);
        newClone.setCloneOf(this);
        return newClone;
    }

    @Override
    public String toString() {
        return name;
    }
}

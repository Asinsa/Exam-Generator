package com.example.testgenerator;

public class Q {
    private int id;
    private String name;
    private Q parent;

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

    @Override
    public String toString() {
        return name;
    }
}

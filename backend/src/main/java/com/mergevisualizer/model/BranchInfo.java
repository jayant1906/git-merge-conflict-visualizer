package com.mergevisualizer.model;

public class BranchInfo {
    private String name;
    private boolean current;

    public BranchInfo() {
    }

    public BranchInfo(String name, boolean current) {
        this.name = name;
        this.current = current;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }

    @Override
    public String toString() {
        return "BranchInfo{" +
                "name='" + name + '\'' +
                ", current=" + current +
                '}';
    }
}

package com.mergevisualizer.model;

public class Conflict {
    private String filePath;
    private String currentContent;
    private String incomingContent;
    private int currentStartLine;
    private int incomingStartLine;

    public Conflict() {
    }

    public Conflict(String filePath, String currentContent, String incomingContent) {
        this.filePath = filePath;
        this.currentContent = currentContent;
        this.incomingContent = incomingContent;
    }

    public Conflict(String filePath, String currentContent, String incomingContent,
                    int currentStartLine, int incomingStartLine) {
        this.filePath = filePath;
        this.currentContent = currentContent;
        this.incomingContent = incomingContent;
        this.currentStartLine = currentStartLine;
        this.incomingStartLine = incomingStartLine;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getCurrentContent() {
        return currentContent;
    }

    public void setCurrentContent(String currentContent) {
        this.currentContent = currentContent;
    }

    public String getIncomingContent() {
        return incomingContent;
    }

    public void setIncomingContent(String incomingContent) {
        this.incomingContent = incomingContent;
    }

    public int getCurrentStartLine() {
        return currentStartLine;
    }

    public void setCurrentStartLine(int currentStartLine) {
        this.currentStartLine = currentStartLine;
    }

    public int getIncomingStartLine() {
        return incomingStartLine;
    }

    public void setIncomingStartLine(int incomingStartLine) {
        this.incomingStartLine = incomingStartLine;
    }

    @Override
    public String toString() {
        return "Conflict{" +
                "filePath='" + filePath + '\'' +
                ", currentContent='" + currentContent + '\'' +
                ", incomingContent='" + incomingContent + '\'' +
                ", currentStartLine=" + currentStartLine +
                ", incomingStartLine=" + incomingStartLine +
                '}';
    }
}

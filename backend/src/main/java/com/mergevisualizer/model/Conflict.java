package com.mergevisualizer.model;

public class Conflict {
    private String filePath;
    private String currentContent;
    private String incomingContent;

    public Conflict() {
    }

    public Conflict(String filePath, String currentContent, String incomingContent) {
        this.filePath = filePath;
        this.currentContent = currentContent;
        this.incomingContent = incomingContent;
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

    @Override
    public String toString() {
        return "Conflict{" +
                "filePath='" + filePath + '\'' +
                ", currentContent='" + currentContent + '\'' +
                ", incomingContent='" + incomingContent + '\'' +
                '}';
    }
}

package com.mergevisualizer.dto;

public class MergeRequest {
    private String repositoryId;
    private String sourceBranch;
    private String targetBranch;

    public MergeRequest() {
    }

    public String getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
    }

    public String getSourceBranch() {
        return sourceBranch;
    }

    public void setSourceBranch(String sourceBranch) {
        this.sourceBranch = sourceBranch;
    }

    public String getTargetBranch() {
        return targetBranch;
    }

    public void setTargetBranch(String targetBranch) {
        this.targetBranch = targetBranch;
    }

    @Override
    public String toString() {
        return "MergeRequest{" +
                "repositoryId='" + repositoryId + '\'' +
                ", sourceBranch='" + sourceBranch + '\'' +
                ", targetBranch='" + targetBranch + '\'' +
                '}';
    }
}

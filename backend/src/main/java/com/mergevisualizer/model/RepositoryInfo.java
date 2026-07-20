package com.mergevisualizer.model;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RepositoryInfo {
    private String repoName;
    private String repoPath;
    private boolean uploadedSuccessfully;
    private String message;
    private String uId;
    private int fileCount;

    public RepositoryInfo(){
    }

    public RepositoryInfo(String name, String path, boolean success, String msg){
        this.repoName = name;
        this.repoPath = path;
        this.uploadedSuccessfully = success;
        this.message = msg;
        this.uId = UUID.randomUUID().toString();
        this.fileCount = 0;   
    }

    public String getRepoName() {
        return repoName;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public String getRepoPath() {
        return repoPath;
    }

    public void setRepoPath(String repoPath) {
        this.repoPath = repoPath;
    }

    public boolean isUploadedSuccessfully() {
        return uploadedSuccessfully;
    }

    public void setUploadedSuccessfully(boolean uploadedSuccessfully) {
        this.uploadedSuccessfully = uploadedSuccessfully;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getuId() {
        return uId;
    }

    public int getFileCount() {
        return fileCount;
    }

    public void setFileCount(int fileCount) {
        this.fileCount = fileCount;
    }

    @Override
    public String toString() {
        return "RepositoryInfo{" +
                "repoName='" + repoName + '\'' +
                ", repoPath='" + repoPath + '\'' +
                ", uploadedSuccessfully=" + uploadedSuccessfully +
                ", message='" + message + '\'' +
                ", uId='" + uId + '\'' +
                ", fileCount=" + fileCount +
                '}';
    }
}

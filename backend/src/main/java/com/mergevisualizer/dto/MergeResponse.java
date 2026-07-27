package com.mergevisualizer.dto;

import java.util.List;

public class MergeResponse {
    private boolean successful;
    private boolean hasConflicts;
    private String message;
    private List<String> conflictFiles;

    public MergeResponse() {
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public boolean getHasConflicts() {
        return hasConflicts;
    }

    public void setHasConflicts(boolean hasConflicts) {
        this.hasConflicts = hasConflicts;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getConflictFiles() {
        return conflictFiles;
    }

    public void setConflictFiles(List<String> conflictFiles) {
        this.conflictFiles = conflictFiles;
    }

    @Override
    public String toString() {
        return "MergeResponse{" +
                "successful=" + successful +
                ", hasConflicts=" + hasConflicts +
                ", message='" + message + '\'' +
                ", conflictFiles=" + conflictFiles +
                '}';
    }
}

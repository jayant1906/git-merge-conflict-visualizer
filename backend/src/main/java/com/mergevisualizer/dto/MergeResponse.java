package com.mergevisualizer.dto;

import com.mergevisualizer.model.Conflict;

import java.util.List;

public class MergeResponse {
    private boolean successful;
    private boolean hasConflicts;
    private String message;
    private List<String> conflictFiles;
    private List<Conflict> conflicts;

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

    public List<Conflict> getConflicts() {
        return conflicts;
    }

    public void setConflicts(List<Conflict> conflicts) {
        this.conflicts = conflicts;
    }

    @Override
    public String toString() {
        return "MergeResponse{" +
                "successful=" + successful +
                ", hasConflicts=" + hasConflicts +
                ", message='" + message + '\'' +
                ", conflictFiles=" + conflictFiles +
                ", conflicts=" + conflicts +
                '}';
    }
}

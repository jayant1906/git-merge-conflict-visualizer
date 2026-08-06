package com.mergevisualizer.service;

import com.mergevisualizer.model.Conflict;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConflictService {
    private final GitService gitService;

    public ConflictService(GitService gitService) {
        this.gitService = gitService;
    }

    public List<Conflict> getConflicts(String repositoryId, List<String> conflictFiles) {
        Path repositoryPath = Path.of("uploads", repositoryId, "extracted");

        try {
            Path repoRoot = gitService.findRepositoryRoot(repositoryPath);
            return getConflicts(repoRoot, conflictFiles);
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read conflict files", exception);
        }
    }

    public List<Conflict> getConflicts(Path repoRoot, List<String> conflictFiles) throws IOException {
        List<Conflict> conflicts = new ArrayList<>();

        for (String conflictFile : conflictFiles) {
            Path conflictFilePath = repoRoot.resolve(conflictFile);
            List<String> lines = Files.readAllLines(conflictFilePath);

            StringBuilder currentContent = new StringBuilder();
            StringBuilder incomingContent = new StringBuilder();
            String section = "none";
            int currentStartLine = 0;
            int incomingStartLine = 0;
            int originalLineNumber = 0;

            for (String line : lines) {
                if (line.startsWith("<<<<<<<")) {
                    section = "current";
                    currentContent = new StringBuilder();
                    incomingContent = new StringBuilder();
                    currentStartLine = originalLineNumber + 1;
                    incomingStartLine = currentStartLine;
                } else if (line.startsWith("=======")) {
                    section = "incoming";
                } else if (line.startsWith(">>>>>>>")) {
                    section = "none";
                    Conflict conflict = new Conflict(conflictFile, currentContent.toString(),
                            incomingContent.toString(), currentStartLine, incomingStartLine);
                    conflicts.add(conflict);
                } else if (section.equals("current")) {
                    currentContent.append(line).append("\n");
                    originalLineNumber++;
                } else if (section.equals("incoming")) {
                    incomingContent.append(line).append("\n");
                } else {
                    originalLineNumber++;
                }
            }
        }

        return conflicts;
    }
}

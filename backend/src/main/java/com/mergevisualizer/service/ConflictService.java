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

        List<Conflict> conflicts = new ArrayList<>();

        try {
            Path repoRoot = gitService.findRepositoryRoot(repositoryPath);

            for (String conflictFile : conflictFiles) {
                Path conflictFilePath = repoRoot.resolve(conflictFile);
                List<String> lines = Files.readAllLines(conflictFilePath);

                StringBuilder currentContent = new StringBuilder();
                StringBuilder incomingContent = new StringBuilder();

                String section = "none";


                for (String line: lines){
                    if (line.startsWith("<<<<<<<")) {
                        section = "current";
                        currentContent = new StringBuilder();
                        incomingContent = new StringBuilder();

                    } else if (line.startsWith("=======")) {
                        section = "incoming";

                    } else if (line.startsWith(">>>>>>>")) {
                        section = "none";
                        Conflict conflict = new Conflict(conflictFile, currentContent.toString(), 
                        incomingContent.toString());
                        conflicts.add(conflict);
                    } else if (section.equals("current")){
                        currentContent.append(line).append("\n");
                    } else if (section.equals("incoming")){
                        incomingContent.append(line).append("\n");
                    }
                }
            }
            return conflicts;
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read conflict files", exception);
        }
    }
}

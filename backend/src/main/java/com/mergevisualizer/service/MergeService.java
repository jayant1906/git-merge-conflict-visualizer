package com.mergevisualizer.service;

import com.mergevisualizer.dto.MergeRequest;
import com.mergevisualizer.dto.MergeResponse;
import com.mergevisualizer.model.Conflict;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.lib.ObjectId;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MergeService {

    private final GitService gitService;
    private final ConflictService conflictService;

    public MergeService(GitService gitService, ConflictService conflictService) {
        this.gitService = gitService;
        this.conflictService = conflictService;
    }

    public MergeResponse mergeBranches(MergeRequest request) {
        String repoId = request.getRepositoryId();
        Path repositoryPath = Path.of("uploads", repoId, "extracted");

        try {
            Path repoRoot = gitService.findRepositoryRoot(repositoryPath);

            try (Git git = Git.open(repoRoot.toFile())) {
                boolean shouldReset = false;

                try {
                    String target = request.getTargetBranch();
                    String source = request.getSourceBranch();

                    boolean targetBranchExistsLocally = git.branchList().call().stream()
                            .anyMatch(ref -> ref.getName().equals("refs/heads/" + target));
                    if (!targetBranchExistsLocally) {
                        throw new IllegalArgumentException("Target Branch does not exist locally: " + target);
                    }

                    Status status = git.status().call();
                    if (!status.isClean()) {
                        throw new IllegalStateException("Repository has uncommitted changes");
                    }

                    git.checkout().setName(target).call();
                    ObjectId sourceCommit = git.getRepository().resolve(source);
                    if (sourceCommit == null) {
                        return createResponse(false, false, "Source branch does not exist: " + source, List.of());
                    }

                    MergeResult result = git.merge().include(sourceCommit).setCommit(false).call();
                    shouldReset = true;
                    MergeResult.MergeStatus mergeStatus = result.getMergeStatus();
                    if (mergeStatus.isSuccessful()) {
                        return createResponse(true, false, "Successful Merge", List.of());
                    }
                    if (mergeStatus == MergeResult.MergeStatus.CONFLICTING) {
                        Map<String, int[][]> conflicts = result.getConflicts();
                        List<String> conflictFiles = new ArrayList<>();
                        if (conflicts != null) {
                            conflictFiles.addAll(conflicts.keySet());
                        }
                        List<Conflict> parsedConflicts = conflictService.getConflicts(repoRoot, conflictFiles);
                        return createResponse(false, true, "Merge has conflicts", conflictFiles, parsedConflicts);
                    }

                    return createResponse(false, false, "Merge failed: " + mergeStatus, List.of());
                } finally {
                    if (shouldReset) {
                        git.reset().setMode(ResetCommand.ResetType.HARD).call();
                    }
                }
            }
        } catch (Exception exception) {
            return createResponse(false, false, exception.getMessage(), List.of());
        }
    }

    private MergeResponse createResponse(boolean successful, boolean hasConflicts, String message, List<String> conflictFiles) {
        return createResponse(successful, hasConflicts, message, conflictFiles, List.of());
    }

    private MergeResponse createResponse(boolean successful, boolean hasConflicts, String message,
                                         List<String> conflictFiles, List<Conflict> conflicts) {
        MergeResponse response = new MergeResponse();
        response.setSuccessful(successful);
        response.setHasConflicts(hasConflicts);
        response.setMessage(message);
        response.setConflictFiles(conflictFiles);
        response.setConflicts(conflicts);
        return response;
    }
}

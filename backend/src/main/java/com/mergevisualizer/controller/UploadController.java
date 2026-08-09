package com.mergevisualizer.controller;

import com.mergevisualizer.model.BranchInfo;
import com.mergevisualizer.model.RepositoryInfo;
import com.mergevisualizer.service.GitService;
import com.mergevisualizer.service.UploadService;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {
        "http://localhost:5173",
        "http://127.0.0.1:5173",
        "https://git-merge-conflict-visualizer-frontend.onrender.com"
})
public class UploadController {

    private final UploadService uploadService;
    private final GitService gitService;

    public UploadController(UploadService uploadService, GitService gitService) {
        this.uploadService = uploadService;
        this.gitService = gitService;
    }

    @PostMapping("/upload")
    public RepositoryInfo uploadRepository(@RequestParam("file") MultipartFile file) throws IOException {
        return uploadService.uploadRepository(file);
    }

    @GetMapping("/repositories/{repositoryId}/branches")
    public List<BranchInfo> getBranches(@PathVariable String repositoryId) throws IOException, GitAPIException {
        Path repositoryPath = Path.of("uploads", repositoryId, "extracted");
        Path repoRoot = gitService.findRepositoryRoot(repositoryPath);
        return gitService.getBranches(repoRoot);
    }
}

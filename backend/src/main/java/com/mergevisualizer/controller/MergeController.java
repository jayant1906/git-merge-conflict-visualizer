package com.mergevisualizer.controller;

import com.mergevisualizer.dto.MergeRequest;
import com.mergevisualizer.dto.MergeResponse;
import com.mergevisualizer.service.MergeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {
        "http://localhost:5173",
        "http://127.0.0.1:5173",
        "https://git-merge-conflict-visualizer-frontend.onrender.com"
})
public class MergeController {

    private final MergeService mergeService;

    public MergeController(MergeService mergeService) {
        this.mergeService = mergeService;
    }

    @PostMapping("/merge")
    public ResponseEntity<MergeResponse> mergeBranches(@RequestBody MergeRequest request) {
        MergeResponse response = mergeService.mergeBranches(request);

        if (response.isSuccessful() || response.getHasConflicts()) {
            return ResponseEntity.ok(response);
        }

        if (isBadMergeRequest(response.getMessage())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    private boolean isBadMergeRequest(String message) {
        if (message == null) {
            return false;
        }

        String lowerMessage = message.toLowerCase();
        return lowerMessage.contains("branch does not exist")
                || lowerMessage.contains("no git repository")
                || lowerMessage.contains("uncommitted changes")
                || lowerMessage.contains("is required")
                || lowerMessage.contains("choose two different branches");
    }
}

package com.mergevisualizer.controller;

import com.mergevisualizer.dto.MergeRequest;
import com.mergevisualizer.dto.MergeResponse;
import com.mergevisualizer.service.MergeService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"})
public class MergeController {

    private final MergeService mergeService;

    public MergeController(MergeService mergeService) {
        this.mergeService = mergeService;
    }

    @PostMapping("/merge")
    public MergeResponse mergeBranches(@RequestBody MergeRequest request) {
        return null;
    }
}

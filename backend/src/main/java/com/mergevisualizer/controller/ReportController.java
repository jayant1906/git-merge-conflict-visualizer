package com.mergevisualizer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mergevisualizer.dto.MergeResponse;
import com.mergevisualizer.service.ReportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {
        "http://localhost:5173",
        "http://127.0.0.1:5173",
        "https://git-merge-conflict-visualizer-frontend.onrender.com"
})
public class ReportController {

    private final ReportService reportService;
    private final ObjectMapper objectMapper;

    public ReportController(ReportService reportService, ObjectMapper objectMapper) {
        this.reportService = reportService;
        this.objectMapper = objectMapper;
    }

    @PostMapping(value = "/report", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> generateReport(@RequestBody Map<String, Object> request) {
        String repositoryId = String.valueOf(request.get("repositoryId"));
        String sourceBranch = String.valueOf(request.get("sourceBranch"));
        String targetBranch = String.valueOf(request.get("targetBranch"));
        MergeResponse mergeResponse = objectMapper.convertValue(request.get("mergeResult"), MergeResponse.class);

        if (mergeResponse == null) {
            throw new IllegalArgumentException("Merge result is required to generate a report.");
        }

        String reportHtml = reportService.generateReportHtml(repositoryId, sourceBranch, targetBranch, mergeResponse);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.html")
                .contentType(MediaType.TEXT_HTML)
                .body(reportHtml);
    }
}

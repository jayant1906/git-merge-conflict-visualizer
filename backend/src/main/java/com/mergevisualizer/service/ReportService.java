package com.mergevisualizer.service;

import com.mergevisualizer.dto.MergeResponse;
import com.mergevisualizer.model.Conflict;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ReportService {

    private final ResourceLoader resourceLoader;
    private final String reportTemplatePath;

    public ReportService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
        this.reportTemplatePath = "classpath:templates/report.html";
    }

    public String generateReportHtml(String repositoryId, String sourceBranch, String targetBranch, MergeResponse mergeResponse) {
        String template = loadTemplate();
        String mergeStatus = mergeResponse.isSuccessful() ? "Successful Merge" : "Merge Conflict";
        String generatedAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        return template
                .replace("{{repositoryName}}", "Uploaded Repository")
                .replace("{{repositoryId}}", escapeHtml(repositoryId))
                .replace("{{targetBranch}}", escapeHtml(targetBranch))
                .replace("{{sourceBranch}}", escapeHtml(sourceBranch))
                .replace("{{statusClass}}", getStatusClass(mergeResponse))
                .replace("{{mergeStatus}}", escapeHtml(mergeStatus))
                .replace("{{generatedAt}}", escapeHtml(generatedAt))
                .replace("{{message}}", escapeHtml(mergeResponse.getMessage()))
                .replace("{{conflictFiles}}", buildConflictFilesHtml(mergeResponse.getConflictFiles()))
                .replace("{{conflictDetails}}", buildConflictDetailsHtml(mergeResponse.getConflicts()));
    }

    private String loadTemplate() {
        Resource resource = resourceLoader.getResource(reportTemplatePath);
        try (InputStream is = resource.getInputStream()){
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException exception){
            throw new RuntimeException("Failed to load report template", exception);
        }
    }

    private String buildConflictFilesHtml(List<String> conflictFiles) {
        if (conflictFiles == null || conflictFiles.isEmpty()) {
            return "<li>No conflict files found.</li>";
        }
        StringBuilder sb = new StringBuilder();
        for (String conflict : conflictFiles) {
            sb.append("<li>")
            .append(escapeHtml(conflict))
            .append("</li>");
        }
        return sb.toString();
    }

    private String buildConflictDetailsHtml(List<Conflict> conflicts) {
        if (conflicts == null || conflicts.isEmpty()) {
            return "<p>No conflicts found.</p>";
        }

        StringBuilder sb = new StringBuilder();

        for (Conflict conflict : conflicts) {
            sb.append("<article class=\"conflict-card\">")
                    .append("<h3>")
                    .append(escapeHtml(conflict.getFilePath()))
                    .append("</h3>")
                    .append("<div class=\"diff-grid\">")
                    .append("<div class=\"diff-column\">")
                    .append("<h4>Current Branch</h4>")
                    .append("<div class=\"diff-code\">")
                    .append(buildNumberedLinesHtml(conflict.getCurrentContent(), conflict.getCurrentStartLine()))
                    .append("</div>")
                    .append("</div>")
                    .append("<div class=\"diff-column\">")
                    .append("<h4>Incoming Branch</h4>")
                    .append("<div class=\"diff-code\">")
                    .append(buildNumberedLinesHtml(conflict.getIncomingContent(), conflict.getIncomingStartLine()))
                    .append("</div>")
                    .append("</div>")
                    .append("</div>")
                    .append("</article>");
        }

        return sb.toString();
    }

    private String buildNumberedLinesHtml(String content, int startLine) {
        String safeContent = content == null ? "" : content.replaceAll("\\R$", "");
        String[] lines = safeContent.split("\\R", -1);
        int lineNumber = startLine > 0 ? startLine : 1;
        StringBuilder sb = new StringBuilder();

        for (String line : lines) {
            sb.append("<div class=\"diff-line\">")
                    .append("<span class=\"diff-line-number\">")
                    .append(lineNumber)
                    .append("</span>")
                    .append("<code>")
                    .append(escapeHtml(line.isEmpty() ? " " : line))
                    .append("</code>")
                    .append("</div>");
            lineNumber++;
        }

        return sb.toString();
    }

    private String escapeHtml(String value) {
        if (value == null) {
            return "";
        }

        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    private String getStatusClass(MergeResponse mergeResponse) {
        if (!mergeResponse.isSuccessful()){
            return "status-conflict";
        }
        return "status-success";
    }
}

package com.mergevisualizer.service;

import com.mergevisualizer.model.RepositoryInfo;
import com.mergevisualizer.util.ZipExtractor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class UploadService {

    public RepositoryInfo uploadRepository(MultipartFile uploadedZip) throws IOException {
        if(uploadedZip.isEmpty() || !uploadedZip.getOriginalFilename().endsWith(".zip")){
            throw new IllegalArgumentException("Invalid file. Please upload a .zip file.");
        }
        String uniqueId = UUID.randomUUID().toString();
        Path uploadFolder = Files.createDirectories(Path.of("uploads", uniqueId));
        Path zipPath = uploadFolder.resolve(uploadedZip.getOriginalFilename());
        uploadedZip.transferTo(zipPath);

        Path extractedFolder = Files.createDirectories(Path.of("uploads", uniqueId, "extracted"));
        ZipExtractor.extract(zipPath, extractedFolder);
        int fileCount = countExtractedFiles(extractedFolder);
        
        RepositoryInfo newRepo = new RepositoryInfo(uploadedZip.getOriginalFilename(), extractedFolder.toString(), true, 
            "Successful", uniqueId);
        newRepo.setFileCount(fileCount);
        return newRepo;
    }

    private int countExtractedFiles(Path extractedFolder) throws IOException {
        if (extractedFolder == null) {
            throw new IllegalArgumentException("Extracted folder cannot be null.");
        }
        if (!Files.exists(extractedFolder)) {
            throw new IOException("Extracted folder does not exist: " + extractedFolder);
        }
        if (!Files.isDirectory(extractedFolder)) {
            throw new IOException("Extracted path is not a directory: " + extractedFolder);
        }
        try (Stream<Path> paths = Files.walk(extractedFolder)) {
            return (int) paths
                .filter(Files::isRegularFile)
                .filter(path -> !isMacMetadataFile(path))
                .count();
        }
    }

    private boolean isMacMetadataFile(Path path) {
        String fileName = path.getFileName().toString();

        return fileName.equals(".DS_Store")
            || fileName.startsWith("._")
            || path.toString().contains("__MACOSX");
    }
}

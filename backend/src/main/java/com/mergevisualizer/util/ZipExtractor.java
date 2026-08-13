package com.mergevisualizer.util;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ZipExtractor{
    private ZipExtractor(){

    }

    public static void extract(Path zipfile, Path destinationFolder) throws IOException{
        File destDirectory = destinationFolder.toFile();
        if (!destDirectory.isDirectory() && !destDirectory.mkdirs()) {
            throw new IOException("Failed to create directory " + destDirectory);
        }
        byte[] buffer = new byte[1024];
        boolean extractedEntry = false;

        try (InputStream inputStream = Files.newInputStream(zipfile);
             ZipArchiveInputStream zis = new ZipArchiveInputStream(
                 new BufferedInputStream(inputStream),
                 "UTF-8",
                 true,
                 true
            )) {
            ZipArchiveEntry zipEntry;
            while((zipEntry = zis.getNextEntry()) != null){
                if (!zis.canReadEntryData(zipEntry)) {
                    throw new IOException("Unsupported ZIP entry: " + zipEntry.getName());
                }
                extractedEntry = true;
                File newFile = new File(destDirectory, zipEntry.getName());
                String destDirPath = destDirectory.getCanonicalPath();
                String newFilePath = newFile.getCanonicalPath();
                if (!newFilePath.startsWith(destDirPath + File.separator)) {
                    throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
                }
                if(zipEntry.isDirectory()){
                    if (!newFile.isDirectory() && !newFile.mkdirs()) {
                        throw new IOException("Failed to create directory " + newFile);
                    }
                } else{
                    File parent = newFile.getParentFile();
                    if(!parent.isDirectory() && !parent.mkdirs()){
                        throw new IOException("Failed to create directory " + parent);
                    }

                    // writing files content
                    try (FileOutputStream fos = new FileOutputStream(newFile)) {
                        int len;
                        while((len = zis.read(buffer)) > 0){
                            fos.write(buffer, 0, len);
                        }
                    }
                }
            }
        }

        if (!extractedEntry) {
            throw new IOException("Invalid ZIP file. Please upload a valid .zip archive.");
        }
    }
}

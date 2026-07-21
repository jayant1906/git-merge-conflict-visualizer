package com.mergevisualizer.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public final class ZipExtractor{
    private ZipExtractor(){

    }

    public static void extract(Path zipfile, Path destinationFolder) throws IOException{
        File destDirectory = destinationFolder.toFile();
        if (!destDirectory.isDirectory() && !destDirectory.mkdirs()) {
            throw new IOException("Failed to create directory " + destDirectory);
        }
        byte[] buffer = new byte[1024];
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipfile.toFile()))) {
            ZipEntry zipEntry = zis.getNextEntry();
            while(zipEntry != null){
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
                zipEntry = zis.getNextEntry();
            }
        }
    }
}

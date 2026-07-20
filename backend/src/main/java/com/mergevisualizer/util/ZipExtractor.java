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
    private ZipExtractor{

    }

    public static extract(Path zipfile, Path destinationFolder){
        File destDirectory = new File(destinationFolder);
        if(!destDirectory){
            destDirectory.mkdir();
        }
        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(new FileInputStream(zipfile));
        ZipEntry zipEntry = zis.getNextEntry();
        while(zipEntry != NULL){
            File newFile = newFile(destDirectory, zipEntry)
        }
    }
}
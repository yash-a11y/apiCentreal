package com.example.apiCentreal.pageRanking;

import java.io.*;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

public class FileUtil {

    // Save each page content to a separate file
    public static void savePageToFile(String url, String content,String fileName) throws IOException {

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName))) {
            writer.write(content);
        }
    }

    // Load page content from a file, return null if the file does not exist
    public static String loadPageFromFile(String url,String fileName) throws IOException {

        if (!Files.exists(Paths.get(fileName))) return null;

        return new String(Files.readAllBytes(Paths.get(fileName)));
    }

   }

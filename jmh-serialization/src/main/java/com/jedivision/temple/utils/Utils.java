package com.jedivision.temple.utils;

import com.jedivision.temple.configuration.Application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

public class Utils {
    public static String readFileToString(String jsonPath) throws IOException {
        ClassLoader classLoader = Application.class.getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource(jsonPath)).getFile());

        // using BufferReader to read the file from resource
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        String ls = System.getProperty("line.separator");
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append(ls);
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        reader.close();
        return stringBuilder.toString();
    }
}

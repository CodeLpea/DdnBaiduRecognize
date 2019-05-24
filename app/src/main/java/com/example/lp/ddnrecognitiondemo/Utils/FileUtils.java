package com.example.lp.ddnrecognitiondemo.Utils;

import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileUtils {
    public static String file2String(File file) {
        byte[] buffer = new byte[0];
        try {
            FileInputStream inputFile = new FileInputStream(file);
            buffer = new byte[(int) file.length()];
            inputFile.read(buffer);
            inputFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Base64.encodeToString(buffer, Base64.DEFAULT);
    }
}

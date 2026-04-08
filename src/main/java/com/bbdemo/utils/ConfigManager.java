package com.bbdemo.utils;

import java.io.FileInputStream;
import java.util.Properties;

public class ConfigManager {
    private static final Properties prop = new Properties();

    static {
        try {
            FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/config.properties");;
            prop.load(fis);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config.properties");
        }
    }

    public static String get(String key) {
        return System.getProperty(key, prop.getProperty(key));
    }
}

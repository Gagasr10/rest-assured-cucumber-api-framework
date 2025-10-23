package com.example.api.config;

import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final Properties props = new Properties();

    static {
        try {
            try (InputStream in = Config.class.getClassLoader().getResourceAsStream("config-common.properties")) {
                if (in != null) props.load(in);
            }
            String env = System.getProperty("env", System.getenv().getOrDefault("ENV", "dev")).toLowerCase();
            String envFile = "config-" + env + ".properties";
            try (InputStream in = Config.class.getClassLoader().getResourceAsStream(envFile)) {
                if (in != null) props.load(in);
                else System.err.println("[Config] WARNING: env file not found: " + envFile + " — using defaults.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config", e);
        }
    }

    public static String get(String key) {
        return props.getProperty(key);
    }
}

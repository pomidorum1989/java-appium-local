package io.dorum.utils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

@Log4j2
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PropertiesUtils {
    private static final Properties PROJECT_CONFIG_PROPERTIES;
    private static final Properties IOS_PROPERTIES;
    private static final Properties ANDROID_PROPERTIES;

    static {
        PROJECT_CONFIG_PROPERTIES = loadFile("config.properties");
        IOS_PROPERTIES = loadFile("android-locators.properties");
        ANDROID_PROPERTIES = loadFile("ios-locators.properties");
    }

    public static Properties loadFile(String fileName) {
        Properties properties = new Properties();
        try (InputStream input = PropertiesUtils.class.getClassLoader().getResourceAsStream(fileName)) {
            if (Objects.isNull(input)) {
                log.error("Unable to find the file: {}", fileName);
                throw new IOException("Unable to load " + fileName);
            }
            properties.load(input);
        } catch (IOException ex) {
            log.error("Unable to load {}", fileName);
        }
        return properties;
    }

    public static String getAppiumServerUrl() {
        return PROJECT_CONFIG_PROPERTIES.getProperty("appium.server.url");
    }

    public static String getAppPath(String platform) {
        return switch (platform.toLowerCase()) {
            case "ios" -> IOS_PROPERTIES.getProperty("ios.app.path");
            case "android" -> ANDROID_PROPERTIES.getProperty("android.app.path");
            default -> {
                log.warn("Platform not recognized: {}", platform);
                yield null;
            }
        };
    }

    public static String getLocator(String key, String platform) {
        return switch (platform.toLowerCase()) {
            case "ios" -> IOS_PROPERTIES.getProperty(key);
            case "android" -> ANDROID_PROPERTIES.getProperty(key);
            default -> {
                log.warn("Platform not recognized: {}", platform);
                yield null;
            }
        };
    }
}

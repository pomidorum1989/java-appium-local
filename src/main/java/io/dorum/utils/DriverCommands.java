package io.dorum.utils;

import io.appium.java_client.android.AndroidStartScreenRecordingOptions;
import io.appium.java_client.android.AndroidStopScreenRecordingOptions;
import io.appium.java_client.ios.IOSStartScreenRecordingOptions;
import io.appium.java_client.ios.IOSStopScreenRecordingOptions;
import io.appium.java_client.screenrecording.CanRecordScreen;
import io.appium.java_client.screenrecording.ScreenRecordingUploadOptions;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.FileOutputStream;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

@Log4j2
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DriverCommands {
    public static void takeScreenShot(String fileName) {
        try {
            TakesScreenshot scrShot = DriverFactory.getAppiumDriver();
            File srcFile = scrShot.getScreenshotAs(OutputType.FILE);
            File destFile = new File(String.format(System.getProperty("user.dir") + "/build/screenshots/%s.png", fileName));
            FileUtils.copyFile(srcFile, destFile);
            log.info("Screenshot was created {}", fileName);
        } catch (IOException e) {
            log.error("Unable to take screenshot {}\n{}", fileName, e.getMessage());
        }
    }

    public static String getPlatform() {
        return DriverFactory.getAppiumDriver().getCapabilities().getPlatformName().name().toLowerCase();
    }

    public static String getDeviceName() {
        return DriverFactory.getAppiumDriver().getCapabilities().getCapability("deviceName").toString();
    }

    public static void startVideoRecording() {
        String platform = getPlatform().toLowerCase();
        switch (platform) {
            case "android":
                ((CanRecordScreen) DriverFactory.getAppiumDriver()).startRecordingScreen(
                        new AndroidStartScreenRecordingOptions());
                break;
            case "ios":
                ((CanRecordScreen) DriverFactory.getAppiumDriver()).startRecordingScreen(
                        new IOSStartScreenRecordingOptions());
                break;
            default:
                throw new IllegalArgumentException("Unsupported platform: " + platform);
        }
        log.info("Started {} video recording", platform);
    }

    public static void stopVideoRecording(String recordingName) {
        String platform = getPlatform().toLowerCase();
        String base64String = switch (platform) {
            case "android" -> ((CanRecordScreen) DriverFactory.getAppiumDriver()).stopRecordingScreen(
                    new AndroidStopScreenRecordingOptions().withUploadOptions(
                            ScreenRecordingUploadOptions.uploadOptions()));
            case "ios" -> ((CanRecordScreen) DriverFactory.getAppiumDriver()).stopRecordingScreen(
                    new IOSStopScreenRecordingOptions().withUploadOptions(
                            ScreenRecordingUploadOptions.uploadOptions()));
            default -> throw new IllegalArgumentException("Unsupported platform: " + platform);
        };
        decodeBase64ToFile(base64String, recordingName);

    }

    public static File decodeBase64ToFile(String base64String, String fileName){
        byte[] data = Base64.getDecoder().decode(base64String);
        File destFile = new File(String.format(System.getProperty("user.dir") + "/build/%s.mp4", fileName));
        try (FileOutputStream fos = new FileOutputStream(destFile)) {
            fos.write(data);
        } catch (IOException e) {
            log.error("Unable to create video recording {}\n{}", fileName, e.getMessage());
        }
        return destFile;
    }
}

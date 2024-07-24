package io.dorum.utils;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.ScreenOrientation;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;

@Log4j2
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DriverFactory {

    private static final ThreadLocal<AppiumDriver> THREAD_LOCAL = new ThreadLocal<>();

    public static void createDriver(String platform, String deviceName, String udId, int port) {
        long currentThread = Thread.currentThread().threadId();
        if (Objects.isNull(THREAD_LOCAL.get())) {
            switch (platform.toLowerCase()) {
                case "android":
                    THREAD_LOCAL.set(createAndroidDriver(deviceName, udId, port));
                    log.info("Android driver {} created for device: {}, port: {}", currentThread, deviceName, port);
                    break;
                case "ios":
                    THREAD_LOCAL.set(createIOSDriver(deviceName, udId, port));
                    log.info("iOS driver {} created for device: {}, port: {}", currentThread, deviceName, port);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid platform: " + platform);
            }
        }
    }

    private static AppiumDriver createAndroidDriver(String deviceName, String udId, int port) {
        UiAutomator2Options options = new UiAutomator2Options()
                .setAutomationName("uiautomator2")
                .setPlatformName("Android")
                .setPlatformVersion("12")
                .setAvd(udId)
//                .setUdid(udId)
                .setDeviceName(deviceName)
                .setSystemPort(port)
                .setOrientation(ScreenOrientation.PORTRAIT)
                .setNoReset(false)
//                .setMjpegServerPort()
//                .setChromedriverPort()
//                .skipServerInstallation()
//                .skipDeviceInitialization()
                .setApp(PropertiesUtils.getAppPath("android"));
        return new AndroidDriver(getAppiumServerUrl(), options);
    }

    private static AppiumDriver createIOSDriver(String deviceName, String udId, int port) {
        XCUITestOptions options = new XCUITestOptions()
                .setAutomationName("XCUITest")
                .setPlatformName("IOS")
                .setPlatformVersion("17.5")
//                .setUdid(udId)
                .setDeviceName(deviceName)
                .setWdaLocalPort(port)
                .setSafariAllowPopups(true)
                .withBrowserName("Safari")
                .setOrientation(ScreenOrientation.PORTRAIT)
                .setNoReset(false);
//                .setApp(PropertiesUtils.getAppPath("ios"));
        return new IOSDriver(getAppiumServerUrl(), options);
    }

    private static URL getAppiumServerUrl() {
        try {
            return new URI(PropertiesUtils.getAppiumServerUrl()).toURL();
        } catch (MalformedURLException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static AppiumDriver getAppiumDriver() {
        return THREAD_LOCAL.get();
    }

    public static AndroidDriver getAndroidDriver() {
        return (AndroidDriver) THREAD_LOCAL.get();
    }

    public static IOSDriver getIOSDriver() {
        return (IOSDriver) THREAD_LOCAL.get();
    }

    public static void removeDriver() {
        long currentThread = Thread.currentThread().threadId();
        AppiumDriver driver = THREAD_LOCAL.get();
        if (Objects.nonNull(driver)) {
            driver.quit();
            THREAD_LOCAL.remove();
            log.info("Driver {} removed for current thread", currentThread);
        } else {
            log.warn("No driver {} found for current thread to remove", currentThread);
        }
    }
}
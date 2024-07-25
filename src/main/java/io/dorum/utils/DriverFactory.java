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

    public static void createDriver(String platform, String deviceName, String udId, int port, int chromeDriverPort, int mjpegPort) {
        long currentThread = Thread.currentThread().threadId();
        if (Objects.isNull(THREAD_LOCAL.get())) {
            switch (platform.toLowerCase()) {
                case "android":
                    THREAD_LOCAL.set(createAndroidDriver(deviceName, udId, port, chromeDriverPort, mjpegPort));
                    log.info("Android driver {} created for device: {}, system port: {}, chrome driver port {}, " +
                                    "mjpeg port {}", currentThread, deviceName, port, chromeDriverPort, mjpegPort);
                    break;
                case "ios":
                    THREAD_LOCAL.set(createIOSDriver(deviceName, udId, port, mjpegPort));
                    log.info("iOS driver {} created for device: {}, wda port: {}, mjpeg port {}",
                            currentThread, deviceName, port, mjpegPort);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid platform: " + platform);
            }
        }
    }

    private static AppiumDriver createAndroidDriver(String deviceName, String udId, int systemPort, int chromeDriverPort, int mjpegPort) {
        UiAutomator2Options options = new UiAutomator2Options()
                .setAutomationName("uiautomator2")
                .setPlatformName("Android")
                .setPlatformVersion("15")
                .setAvd(deviceName)
//                .setUdid(udId)
                .setDeviceName(deviceName)
                .setSystemPort(systemPort)
                .setOrientation(ScreenOrientation.PORTRAIT)
                .setNoReset(false)
                .setMjpegServerPort(mjpegPort)
                .withBrowserName("Chrome")
//                .skipServerInstallation()
//                .skipDeviceInitialization()
//                .setApp(PropertiesUtils.getAppPath("android")).
                .setChromedriverPort(chromeDriverPort);
        return new AndroidDriver(getAppiumServerUrl(), options);
    }

    private static AppiumDriver createIOSDriver(String deviceName, String udId, int wdaPort, int mjpegPort) {
        XCUITestOptions options = new XCUITestOptions()
                .setAutomationName("XCUITest")
                .setPlatformName("IOS")
                .setPlatformVersion("17.5")
//                .setUdid(udId)
                .setDeviceName(deviceName)
                .setWdaLocalPort(wdaPort)
                .setSafariAllowPopups(true)
                .withBrowserName("Safari")
                .setOrientation(ScreenOrientation.PORTRAIT)
                .setNoReset(false)
//                .setApp(PropertiesUtils.getAppPath("ios")).
                .setMjpegServerPort(mjpegPort);
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
package io.dorum.tests;

import io.dorum.utils.AppiumServer;
import io.dorum.utils.DriverCommands;
import io.dorum.utils.DriverFactory;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.ThreadContext;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Log4j2
public class BaseTest {

    @BeforeSuite
    public void beforeSuite() {
        ThreadContext.put("threadName", String.valueOf(Thread.currentThread().threadId()));
        AppiumServer.startServer(4723);
    }

    @BeforeTest
    @Parameters({"platform", "deviceName", "udid", "port", "chromeDriverPort", "mjpegPort"})
    public void beforeTest(String platform, String deviceName, String udid, String port, @Optional("9515")
    String chromeDriverPort, String mjpegPort) {
        DriverFactory.createDriver(platform, deviceName, udid, Integer.parseInt(port),
                Integer.parseInt(chromeDriverPort), Integer.parseInt(mjpegPort));
    }

    @BeforeMethod
    public void beforeMethod(Method method) {
        log.info("Method started: {}", method.getName());
        DriverCommands.startVideoRecording();
    }

    @AfterMethod
    public void afterMethod(Method method, ITestResult result) {
        String localDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yy-HH-mm-ss"));
        if (!result.isSuccess()) {
           DriverCommands.takeScreenShot("failed-" + method.getName() + "-" + localDateTime);
        }
        DriverCommands.stopVideoRecording(method.getName() + "-" + localDateTime);
        log.info("Method finished: {}", method.getName());
    }

    @AfterTest(alwaysRun = true)
    public void afterTest() {
        DriverFactory.removeDriver();
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        AppiumServer.stopServer();
        ThreadContext.clearAll();
    }
}

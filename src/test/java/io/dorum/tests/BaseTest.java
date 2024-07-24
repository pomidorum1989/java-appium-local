package io.dorum.tests;

import io.dorum.utils.AppiumServer;
import io.dorum.utils.DriverFactory;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;

@Log4j2
public class BaseTest {

    @BeforeSuite
    public void beforeSuite() throws FileNotFoundException {
        AppiumServer.startServer(4723);
    }

    @BeforeTest
    @Parameters({"platform", "deviceName", "udid", "wdaLocalPort"})
    public void beforeTest(String platform, String deviceName, String udid, String wdaLocalPort) {
        DriverFactory.createDriver(platform, deviceName, udid, Integer.parseInt(wdaLocalPort));
    }

    @BeforeMethod
    public void beforeMethod(Method method) {
        log.info("Method started: {}", method.getName());
    }

    @AfterMethod
    public void afterMethod(Method method, ITestResult result) {
        if (!result.isSuccess()) {
            takeScreenShot("failed-" + method.getName());
        }
        log.info("Method finished: {}", method.getName());
    }

    @AfterTest(alwaysRun = true)
    public void afterTest() {
        DriverFactory.removeDriver();
        AppiumServer.stopServer();
    }

    protected void takeScreenShot(String fileName) {
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
}

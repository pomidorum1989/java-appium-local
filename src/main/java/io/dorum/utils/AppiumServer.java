package io.dorum.utils;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Objects;

@Log4j2
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AppiumServer {
    private static AppiumDriverLocalService service;

    public static void startServer(int port) throws FileNotFoundException {
        AppiumServiceBuilder builder = new AppiumServiceBuilder()
                .usingPort(port)
                .withArgument(GeneralServerFlag.SESSION_OVERRIDE)
                .withArgument(GeneralServerFlag.LOG_LEVEL, "warn")
                .withLogFile(new File("./build/AppiumServerLogs.txt"));
        service = AppiumDriverLocalService.buildService(builder);
        service.start();
        service.clearOutPutStreams();
        log.info("Appium server started: {}", service.getUrl());
    }

    public static void stopServer() {
        if (Objects.nonNull(service) && service.isRunning()) {
            service.stop();
            log.info("Appium server stopped: {}", service.getUrl());
        }
    }
}
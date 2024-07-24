package io.dorum.pages;

import io.dorum.utils.DriverFactory;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class BasePage {

    public void openUrl(String url) {
        DriverFactory.getAppiumDriver().get(url);
        log.info("Url {} was opened", url);
    }
}

package io.dorum.pages;

import io.dorum.utils.DriverFactory;
import io.dorum.utils.PropertiesUtils;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

@Log4j2
public class LoginPage extends BasePage {

    private final By usernameField = By.id(PropertiesUtils.getLocator("login.user.name"));
    private final By passwordField = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMessage = By.xpath("//h3[@data-test='error']");
    private final By logo = By.xpath("//div[@class = 'login_logo']");

    public void enterUsername(String username) {
        log.info("Entering username: {}", username);
        DriverFactory.getAppiumDriver().findElement(usernameField).sendKeys(username);
    }

    public void enterPassword(String password) {
        log.info("Entering password");
        DriverFactory.getAppiumDriver().findElement(passwordField).sendKeys(password);
    }

    public void clickLogin() {
        log.info("Clicking login button");
        DriverFactory.getAppiumDriver().findElement(loginButton).click();
    }

    public void login(String username, String password) {
        log.info("Logging in with username: {}", username);
        enterUsername(username);
        enterPassword(password);
        clickLogin();
        getErrorMessageText();
    }

    public String getErrorMessageText() {
        try {
            WebElement webElement = DriverFactory.getAppiumDriver().findElement(errorMessage);
            if (webElement.isDisplayed()) {
                String text = webElement.getText();
                log.info("Error message: {}", text);
                return text;
            }
        } catch (NoSuchElementException e) {
            log.warn("Error message didn't appear");
        }
        return "";
    }

    public String getLogoText() {
        return  DriverFactory.getAppiumDriver().findElement(logo).getText();
    }
}

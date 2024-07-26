package io.dorum.pages;

import io.dorum.utils.WaitUtils;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;

@Log4j2
public class ProductPage extends BasePage {

    private final By cartIcon = By.id("shopping_cart_container");
    private final By menuBtn = By.id("react-burger-menu-btn");
    private final By logOutBtn = By.id("logout_sidebar_link");

    public boolean isCartIconVisible() {
        return WaitUtils.waitForElementToBeVisible(cartIcon).isDisplayed();
    }

    public void clickMenuBtn() {
        WaitUtils.waitForElementToBeClickable(menuBtn).click();
        log.info("Clicked menu button");
    }

    public void clickLogOutBtn() {
        WaitUtils.waitForElementToBeClickable(logOutBtn).click();
        log.info("Clicked logout button");
    }
}

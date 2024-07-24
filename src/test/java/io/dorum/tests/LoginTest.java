package io.dorum.tests;

import io.dorum.pages.LoginPage;
import lombok.extern.log4j.Log4j2;
import org.testng.annotations.Test;

@Log4j2
public class LoginTest extends BaseTest {

    @Test(description = "iOS test")
    public void loginTest() {
        LoginPage page = new LoginPage();
        page.openUrl("http://www.google.com");
        takeScreenShot("1");
    }
}

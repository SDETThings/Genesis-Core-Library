package WEBHelper;

import org.openqa.selenium.WebDriver;

@FunctionalInterface
public interface WebDriverManager {
    WebDriver setUpWebDriver(String browserName);
    static WebDriverManager getDriverProvider() {
        return (browserName) -> BrowserManager.getBrowserManagerInstance(browserName).getDriver();
    }
    default void quitBrowser()
    {
        BrowserManager.quitBrowser();
    }
}

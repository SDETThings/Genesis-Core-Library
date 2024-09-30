package WEBHelper;

import org.openqa.selenium.WebDriver;

@FunctionalInterface
public interface WebDriverManager {
    WebDriver setUpWebDriver(String browserName,boolean isHeadless);
    static WebDriverManager getDriverProvider() {
        return (browserName,isHeadless) -> BrowserManager.getBrowserManagerInstance(browserName,isHeadless).getDriver();
    }
    default void quitBrowser()
    {
        BrowserManager.quitBrowser();
    }
}

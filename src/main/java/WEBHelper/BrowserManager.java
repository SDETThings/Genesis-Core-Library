package WEBHelper;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class BrowserManager {
    private static volatile BrowserManager webDriverManagerInstance;
    private static ThreadLocal<WebDriver> tlDriver= new ThreadLocal<>();
    private BrowserManager(){
        if(webDriverManagerInstance!=null){
            throw new IllegalArgumentException("Cannot create the object of this private constructor");
        }
    }
    private void setTlDriver(String browserName){
        switch (browserName) {
            case "Chrome" -> tlDriver.set(new ChromeDriver());
            case "Firefox" -> tlDriver.set(new FirefoxDriver());
            case "Edge" -> tlDriver.set(new EdgeDriver());
            default -> throw new IllegalArgumentException("Unsupported browser: " + browserName);
        }
    }
    public static BrowserManager getBrowserManagerInstance(String browserName) {
        if(webDriverManagerInstance==null){
            synchronized (BrowserManager.class){
                if(webDriverManagerInstance==null){
                    webDriverManagerInstance = new BrowserManager();
                }
            }
        }
        if(tlDriver.get()==null){
            webDriverManagerInstance.setTlDriver(browserName);
        }
        return webDriverManagerInstance;
    }
    public WebDriver getDriver(){
        return tlDriver.get();
    }
    public static void quitBrowser() {
        if(tlDriver.get()!=null){
            tlDriver.get().quit();
            tlDriver.remove();
        }
    }
}

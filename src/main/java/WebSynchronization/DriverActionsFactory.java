package WebSynchronization;

import org.openqa.selenium.support.ui.ExpectedConditions;

public class DriverActionsFactory {
    private static ThreadLocal<DriverActions> instance = ThreadLocal.withInitial(() -> new DriverActions(
            (driver, element, timeoutSeconds, pollingIntervalSeconds) -> {
                DriverWaits.initializeWait(driver, timeoutSeconds, pollingIntervalSeconds);
                // Use DriverWaits to wait for element to be visible
                return DriverWaits.getWait().until(ExpectedConditions.visibilityOf(element)) != null;
            }
    ));

    // Get the singleton instance for each thread
    public static DriverActions getInstance() {
        return instance.get();
    }
}

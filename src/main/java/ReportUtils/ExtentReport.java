package ReportUtils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * ExtentReport class contains the configuration for extent reports
 */
public class ExtentReport {
    public static ExtentSparkReporter spark;
    public static ExtentReports extent;
    /**
     * BuildExtentReport method is used to build extent report configurations.
     * @param fullName This is the file path of the extentReport to be generated.
     * @param documentTitle This is title of the document.
     * @param reportName This is name of the report inside the fle
     * @param timeStampFormat This is the timestamp format inside the file.
     * @return Returns a ExtentReports object.
     */
    public synchronized ExtentReports BuildExtentReport(String fullName,String documentTitle, String reportName,String timeStampFormat) {
        extent = new ExtentReports();
        spark = new ExtentSparkReporter(fullName);
        spark.config().setTheme(Theme.DARK);
        spark.config().setDocumentTitle(documentTitle);
        spark.config().setReportName(reportName);
        spark.config().setTimeStampFormat(timeStampFormat);
        extent.attachReporter(spark);
        return extent;
    }
    /**
     * This Method is used to generate extent report name with timestamp.
     * @param pattern This is the timestamp format inside the file.
     * @return Returns report name as string
     */
    public static String getReportNameWithTimeStamp(String pattern ) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        String timeStamp = now.format(formatter);
        String reportName = "ExtentReport"+timeStamp+".html";
        return reportName;
}

}

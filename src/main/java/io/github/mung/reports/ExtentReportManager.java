package io.github.mung.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import io.github.mung.constants.FrameworkConstants;
import io.github.mung.drivers.DriverManager;
import io.github.mung.enums.AuthorType;
import io.github.mung.enums.Category;
import io.github.mung.utils.*;

import io.github.mung.utils.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.util.Objects;

public class ExtentReportManager {
    private static ExtentReports extentReports;
    private static String link = "";

    public synchronized static ExtentReports getExtentReports() {
        ExtentSparkReporter reporter = new ExtentSparkReporter("./extent-reports/extent-report.html");
        reporter.config().setDocumentTitle("Test Automation");
        reporter.config().setEncoding("utf-8");
        reporter.config().setReportName("Test Automation - Extent Report");
        reporter.config().setTheme(Theme.DARK);
        extentReports.attachReporter(reporter);
        extentReports.setSystemInfo("Author", "Mung Thai");

        return extentReports;
    }


    public static void initReports() {
        if (Objects.isNull(extentReports)) {
            extentReports = new ExtentReports();

            if (FrameworkConstants.OVERRIDE_REPORTS.trim().equals(FrameworkConstants.NO)) {
                LogUtils.info("OVERRIDE EXTENT REPORTS = " + FrameworkConstants.OVERRIDE_REPORTS);
                link = FrameworkConstants.EXTENT_REPORT_FOLDER_PATH + File.separator + DateUtils.getCurrentDateTimeCustom("_") + "_" + FrameworkConstants.EXTENT_REPORT_FILE_NAME;
                LogUtils.info("Link Extent Report: " + link);
            } else {
                LogUtils.info("OVERRIDE EXTENT REPORTS = " + FrameworkConstants.OVERRIDE_REPORTS);
                link = FrameworkConstants.EXTENT_REPORT_FILE_PATH;
                LogUtils.info("Link Extent Report: " + link);
            }

//            ExtentPDFReporter pdf = new ExtentPDFReporter("reports/ExtentReports/PdfReport.pdf");
//            try {
//                pdf.loadJSONConfig(new File("src/test/resources/pdf-config.json"));
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//            extentReports.attachReporter(pdf);

            ExtentSparkReporter spark = new ExtentSparkReporter(link);
            extentReports.attachReporter(spark);
            spark.config().setTheme(Theme.STANDARD);
            spark.config().setDocumentTitle(FrameworkConstants.REPORT_TITLE);
            spark.config().setReportName(FrameworkConstants.REPORT_TITLE);
            extentReports.setSystemInfo("Framework Name", FrameworkConstants.REPORT_TITLE);
            extentReports.setSystemInfo("Author", FrameworkConstants.AUTHOR);

            LogUtils.info("Extent Reports is installed.");
        }
    }

    public static void flushReports() {
        if (Objects.nonNull(extentReports)) {
            extentReports.flush();
        }
        ExtentTestManager.unload();
        ReportUtils.openReports(link);
    }

    public static void createTest(String testCaseName) {
        ExtentTestManager.setExtentTest(extentReports.createTest(IconUtils.getBrowserIcon() + " " + testCaseName));
        //ExtentTestManager.setExtentTest(extentReports.createTest(testCaseName));
    }

    public static void createTest(String testCaseName, String description) {
        ExtentTestManager.setExtentTest(extentReports.createTest(testCaseName, description));
    }

    public static void removeTest(String testCaseName) {
        extentReports.removeTest(testCaseName);
    }

    /**
     * Adds the screenshot.
     *
     * @param message the message
     */
    public static void addScreenShot(String message) {
        String base64Image = "data:image/png;base64," + ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BASE64);

        //Base64 from Screenshot of Selenium
        ExtentTestManager.getExtentTest().log(Status.INFO, MediaEntityBuilder.createScreenCaptureFromBase64String(base64Image).build());

        //File Path from Screenshot of Java
        //ExtentTestManager.getExtentTest().log(Status.INFO, MediaEntityBuilder.createScreenCaptureFromPath(String.valueOf(CaptureHelpers.getScreenshotFile(message))).build());

    }

    /**
     * Adds the screenshot.
     *
     * @param status  the status
     * @param message the message
     */
    public static void addScreenShot(Status status, String message) {
        String base64Image = "data:image/png;base64," + ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BASE64);

        //Base64 from Screenshot of Selenium
        ExtentTestManager.getExtentTest().log(status, MediaEntityBuilder.createScreenCaptureFromBase64String(base64Image).build());

        //File Path from Screenshot of Java
        //ExtentTestManager.getExtentTest().log(status, MediaEntityBuilder.createScreenCaptureFromPath(CaptureHelpers.getScreenshotAbsolutePath(message)).build());

    }


    synchronized public static void addAuthors(AuthorType[] authors) {
        if (authors == null) {
            ExtentTestManager.getExtentTest().assignAuthor("Mung Tester");
        } else {
            for (AuthorType author : authors) {
                ExtentTestManager.getExtentTest().assignAuthor(author.toString());
            }
        }
    }


    // public static void addCategories(String[] categories) {
    synchronized public static void addCategories(Category[] categories) {
        if (categories == null) {
            ExtentTestManager.getExtentTest().assignCategory("REGRESSION");
        } else {
            // for (String category : categories) {
            for (Category category : categories) {
                ExtentTestManager.getExtentTest().assignCategory(category.toString());
            }
        }
    }

    synchronized public static void addDevices() {
        ExtentTestManager.getExtentTest().assignDevice(BrowserInfoUtils.getBrowserInfo());
//		ExtentReportManager.getExtentTest()
//				.assignDevice(BrowserIconUtils.getBrowserIcon() + " : " + BrowserInfoUtils.getBrowserInfo());
    }

    public static void logMessage(String message) {
        ExtentTestManager.getExtentTest().log(Status.INFO, message);
    }

    public static void logMessage(Status status, String message) {
        ExtentTestManager.getExtentTest().log(status, message);
    }

    public static void logMessage(Status status, Object message) {
        ExtentTestManager.getExtentTest().log(status, (Throwable) message);
    }

    public static void pass(String message) {
        //LogUtils.info("ExtentReportManager class: " + ExtentTestManager.getExtentTest());
        ExtentTestManager.getExtentTest().pass(message);
    }

    public static void pass(Markup message) {
        ExtentTestManager.getExtentTest().pass(message);
    }

    public static void fail(String message) {
        ExtentTestManager.getExtentTest().fail(message);
    }

    public static void fail(Object message) {
        ExtentTestManager.getExtentTest().fail((String) message);
    }

    public static void fail(Markup message) {
        ExtentTestManager.getExtentTest().fail(message);
    }

    public static void skip(String message) {
        ExtentTestManager.getExtentTest().skip(message);
    }

    public static void skip(Markup message) {
        ExtentTestManager.getExtentTest().skip(message);
    }

    public static void info(Markup message) {
        ExtentTestManager.getExtentTest().info(message);
    }

    public static void info(String message) {
        ExtentTestManager.getExtentTest().info(message);
    }

    public static void warning(String message) {
        ExtentTestManager.getExtentTest().log(Status.WARNING, message);
    }
}
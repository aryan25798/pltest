package com.example.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.File;

/**
 * ReportManager: Simple singleton manager for ExtentReports.
 * Creates and saves the HTML report at test-output/ExecutionReport.html.
 */
public class ReportManager {

    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> testThread = new ThreadLocal<>();

    public static synchronized ExtentReports getInstance() {
        if (extent == null) {
            File dir = new File("test-output");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File reportFile = new File(dir, "ExecutionReport.html");
            ExtentSparkReporter spark = new ExtentSparkReporter(reportFile);
            spark.config().setDocumentTitle("ProjectLens Test Report");
            spark.config().setReportName("Test Execution Report");
            spark.config().setTheme(Theme.STANDARD);

            extent = new ExtentReports();
            extent.attachReporter(spark);
            extent.setSystemInfo("Application", "ProjectLens");
            extent.setSystemInfo("Browser", ConfigReader.getProperty("browser").toUpperCase());
            extent.setSystemInfo("OS", System.getProperty("os.name"));
        }
        return extent;
    }

    public static ExtentTest getTest() {
        return testThread.get();
    }

    public static void setTest(ExtentTest test) {
        testThread.set(test);
    }

    public static synchronized void flush() {
        if (extent != null) {
            extent.flush();
        }
    }
}

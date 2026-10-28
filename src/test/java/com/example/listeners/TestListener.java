package com.example.listeners;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.example.utils.DriverManager;
import com.example.utils.ReportManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * TestListener: Listens to test execution events and logs them to ExtentReports.
 * - On Start: creates test entry
 * - On Success: marks test as passed
 * - On Failure: marks test as failed and captures screenshot
 * - On Skipped: marks test as skipped
 * - On Finish: flushes (writes) report to test-output/ExecutionReport.html
 */
public class TestListener implements ITestListener {

    @Override
    public void onStart(ITestContext context) {
        ReportManager.getInstance();
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getDescription();
        if (testName == null || testName.isEmpty()) {
            testName = result.getMethod().getMethodName();
        }

        ExtentTest test = ReportManager.getInstance().createTest(testName);
        ReportManager.setTest(test);

        // Group tests by module name in report tabs
        String className = result.getTestClass().getRealClass().getSimpleName();
        test.assignCategory(formatModuleName(className));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentTest test = ReportManager.getTest();
        if (test != null) {
            test.pass("Test passed successfully");
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest test = ReportManager.getTest();
        if (test != null) {
            test.fail(result.getThrowable());

            // Capture screenshot on test failure
            try {
                WebDriver driver = DriverManager.getDriver();
                if (driver != null) {
                    String base64 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
                    test.fail("Failure Screenshot",
                            MediaEntityBuilder.createScreenCaptureFromBase64String(base64).build());
                }
            } catch (Exception ignored) {}
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTest test = ReportManager.getTest();
        if (test != null) {
            test.skip("Test skipped: " + result.getThrowable());
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        ReportManager.flush();
    }

    private String formatModuleName(String className) {
        switch (className) {
            case "LoginTest": return "Login Module";
            case "TrainerReviewTest": return "Trainer Review Module";
            case "FormValidationTest": return "Form Validation Module";
            case "NotificationTest": return "Notification Module";
            case "ReviseSubmissionTest": return "Revise Submission Module";
            case "FormDataUpdationTest": return "Form Data Updation Module";
            default: return "Tests";
        }
    }
}

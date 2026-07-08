package com.crd.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;

public class ExtentReportManager implements ITestListener {

    // Simple tracking objects
    private static ExtentReports extent;
    private static ExtentTest currentTest;

    @Override
    public void onStart(ITestContext context) {

        String folderPath = System.getProperty("user.dir") + "/target/extent-reports/";
        String filePath = folderPath + "Dashboard.html";

        File dir = new File(folderPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        ExtentSparkReporter spark = new ExtentSparkReporter(filePath);
        extent = new ExtentReports();
        extent.attachReporter(spark);
    }

    @Override
    public void onTestStart(ITestResult result) {
        // 3. Create a clean row in the report whenever a test begins
        String description = result.getMethod().getDescription();
        currentTest = extent.createTest(result.getMethod().getMethodName(), description);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        currentTest.pass("Passed.");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        currentTest.fail(result.getThrowable()); // Automatically print error logs
    }

    @Override
    public void onFinish(ITestContext context) {
        if (extent != null) {
            extent.flush(); // 4. Close the HTML stream and save the file to disk
        }
    }
}

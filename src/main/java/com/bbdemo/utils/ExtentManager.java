package com.bbdemo.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ExtentManager {

    private static ExtentReports extent;
    private static final Logger log = LoggerManager.getLogger(ExtentManager.class);
    private static final ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    public static ExtentReports getReporter() {
        if (extent == null) {
            String reportPath = System.getProperty("user.dir") + "/src/test/resources/reports/TestReport.html";
            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
            spark.config().setReportName("Automation Test Report");
            spark.config().setDocumentTitle("BBDemo Report");
            spark.config().setTheme(Theme.DARK);

            extent = new ExtentReports();
            extent.attachReporter(spark);
            // Adding system information
            extent.setSystemInfo("Operating System", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
            extent.setSystemInfo("User Name", System.getProperty("user.name"));
        }
        return extent;
    }

    // Start the Test
    public static ExtentTest startTest(String testName) {
        ExtentTest extentTest = getReporter().createTest(testName);
        test.set(extentTest);
        return extentTest;
    }

    //End a Test
    public static void endTest() {
        getReporter().flush();
    }

    //Get Current Thread's test
    public static ExtentTest getTest() {
        return test.get();
    }

    //Method to get the name of the current test
    public static String getTestName() {
        ExtentTest currentTest = getTest();
        if(currentTest!=null) {
            return currentTest.getModel().getName();
        }
        else {
            return "No test is currently active for this thread";
        }
    }

    //Log a step
    public static void logStep(String logMessage) {
        getTest().info(logMessage);
    }

    //Log a step validation with screenshot
    public static void logStepWithScreenshot(WebDriver driver, String logMessage, String screenShotMessage) {
        getTest().pass(logMessage);
        attachScreenshot(driver,screenShotMessage);
    }

    //Log a Failure
    public static void logFailure(WebDriver driver, String logMessage, String screenShotMessage) {
        String colorMessage = String.format("<span style='color:red;'>%s</span>", logMessage);
        getTest().fail(colorMessage);
        attachScreenshot(driver,screenShotMessage);
    }

    //Log a skip
    public static void logSkip(String logMessage) {
        String colorMessage = String.format("<span style='color:orange;'>%s</span>", logMessage);
        getTest().skip(colorMessage);
    }

    //Take a screenshot with date and time in the file
    public static String takeScreenshot(WebDriver driver, String screenshotName) {
        TakesScreenshot ts = (TakesScreenshot)driver;
        File src = ts.getScreenshotAs(OutputType.FILE);
        //Format date and Time for file name
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());

        //Saving the screenshot to a file
        String destPath = System.getProperty("user.dir") + "/src/test/resources/screenshots/"+screenshotName+"_"+timeStamp+".png";

        File finalPath = new File(destPath);
        try {
            FileUtils.copyFile(src, finalPath);
        } catch (IOException e) {
            getTest().fail("Error creating screenshot file. Check logs for details.");
            log.error("Error creating screenshot file at {}. {}", destPath, e.getMessage());
        }
        //Convert screenshot to Base64 fir embedding in the Report
        return convertToBase64(src);
    }

    //Convert screenshot to Base64 format
    public static String convertToBase64(File screenShotFile) {
        String base64Format="";
        //Read the file content into a byte array
        try {
            byte[] fileContent = FileUtils.readFileToByteArray(screenShotFile);
            base64Format = Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException e) {
            getTest().fail("Error converting file to base64. Check logs for details.");
            log.error("Error converting file to base64. {}", e.getMessage());
        }
        return base64Format;
    }

    //Attach screenshot to report using Base64
    public synchronized static void attachScreenshot(WebDriver driver, String message) {
        try {
            String screenShotBase64 = takeScreenshot(driver,getTestName());
            getTest().info(message, MediaEntityBuilder.createScreenCaptureFromBase64String(screenShotBase64).build());
        } catch (Exception e) {
            getTest().fail("Error while attaching screenshot. Check logs for details.");
            log.error("Error while attaching screenshot with message: {}. {}", message, e.getMessage());
        }
    }
}

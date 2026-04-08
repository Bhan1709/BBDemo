package com.bbdemo.utils;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import java.time.Duration;

public class DriverFactory {
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static final Logger log = LoggerManager.getLogger(DriverFactory.class);

    public static void initDriver(){
        BrowserFactory browserFactory = BrowserFactory.valueOf(ConfigManager.get("browser").toUpperCase());
        Execution execution = Execution.valueOf(ConfigManager.get("execution").toUpperCase());
        int implicitWait = Integer.parseInt(ConfigManager.get("implicitWait"));

        log.info("Initializing driver. Browser: {}, Execution: {}", browserFactory, execution);
        try{
            WebDriver driverInstance = switch (execution) {
                case LOCAL -> browserFactory.createDriver();
            };
            driverInstance.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
            driverInstance.manage().window().maximize();

            driver.set(driverInstance);
            log.info("WebDriver initialized successfully");
        } catch (Exception e) {
            log.error("WebDriver initialization failed. {}", e.getMessage());
            throw new RuntimeException(e);
        }

    }

    public static WebDriver getDriver() {
        if (driver.get() == null) {
            log.error("WebDriver is not initialized");
            throw new IllegalStateException("WebDriver is not initialized");
        }
        return driver.get();
    }

    public static void quitDriver() {
        if (driver.get() != null) {
            try{
                driver.get().quit();
            } catch (Exception e) {
                log.error("unable to quit the driver. {}" , e.getMessage());
            }
            driver.remove();
            log.info("WebDriver instance is closed.");
        }
    }

    enum Execution {
        LOCAL
    }
}

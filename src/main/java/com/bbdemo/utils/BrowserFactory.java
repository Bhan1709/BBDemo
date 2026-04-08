package com.bbdemo.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.AbstractDriverOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

public enum BrowserFactory {
    CHROME {
        @Override
        public WebDriver createDriver() {
            return new ChromeDriver(getOptions());
        }

        @Override
        public ChromeOptions getOptions() {
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments(START_MAXIMIZED);
            chromeOptions.addArguments(CHROME_HEADLESS);
            chromeOptions.addArguments("--disable-infobars");
            chromeOptions.addArguments("--disable-gpu"); // Disable GPU for headless mode
            //chromeOptions.addArguments("--window-size=1920,1080"); // Set window size
            chromeOptions.addArguments("--disable-notifications"); // Disable browser notifications
            chromeOptions.addArguments("--no-sandbox"); // Required for some CI environments like Jenkins
            chromeOptions.addArguments("--disable-dev-shm-usage"); // Resolve issues in resource-limited environments

            return chromeOptions;
        }
    }, FIREFOX {
        @Override
        public WebDriver createDriver() {
            return new FirefoxDriver(getOptions());
        }

        @Override
        public FirefoxOptions getOptions() {
            FirefoxOptions firefoxOptions = new FirefoxOptions();
            firefoxOptions.addArguments(START_MAXIMIZED);
            firefoxOptions.addArguments(GENERIC_HEADLESS);

            return firefoxOptions;
        }
    }, EDGE {
        @Override
        public WebDriver createDriver() {
            return new EdgeDriver(getOptions());
        }

        @Override
        public EdgeOptions getOptions() {
            EdgeOptions edgeOptions = new EdgeOptions();
            edgeOptions.addArguments(START_MAXIMIZED);
            edgeOptions.addArguments(GENERIC_HEADLESS);

            return edgeOptions;
        }
    }, SAFARI {
        @Override
        public WebDriver createDriver() {
            return new SafariDriver(getOptions());
        }

        @Override
        public SafariOptions getOptions() {
            SafariOptions safariOptions = new SafariOptions();
            safariOptions.setAutomaticInspection(false);

            return safariOptions;
        }
    };

    private static final String START_MAXIMIZED = "--start-maximized";
    public static final String CHROME_HEADLESS = "--headless=new";
    public static final String GENERIC_HEADLESS = "-headless";

    public abstract WebDriver createDriver();

    public abstract AbstractDriverOptions<?> getOptions();
}

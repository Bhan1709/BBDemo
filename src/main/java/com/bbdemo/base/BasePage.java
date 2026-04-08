package com.bbdemo.base;

import com.bbdemo.utils.ConfigManager;
import com.bbdemo.utils.LoggerManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;
    private static final Logger log = LoggerManager.getLogger(BasePage.class);

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(Integer.parseInt(ConfigManager.get("explicitWait"))));
    }

    // Method to click an element
    public void click(By by) {
        String elementDescription = getElementDescription(by);
        try {
            applyBorder(by,"green");
            waitForElementToBeClickable(by);
            driver.findElement(by).click();
            log.info("clicked an element --> {}", elementDescription);
        } catch (Exception e) {
            applyBorder(by,"red");
            log.error("unable to click element. {}", e.getMessage());
        }
    }

    // Method to enter text into an input field
    public void enterText(By by, String value) {
        try {
            waitForElementToBeVisible(by);
            applyBorder(by,"green");
            WebElement element = driver.findElement(by);
            element.clear();
            element.sendKeys(value);
            log.info("Entered text on {} --> {}", getElementDescription(by), value);
        } catch (Exception e) {
            applyBorder(by,"red");
            log.error("Unable to enter the value. {}", e.getMessage());
        }
    }

    // Method to get text from an input field
    public String getText(By by) {
        try {
            waitForElementToBeVisible(by);
            applyBorder(by,"green");
            return driver.findElement(by).getText();
        } catch (Exception e) {
            applyBorder(by,"red");
            log.error("Unable to get the text: {}", e.getMessage());
            return "";
        }
    }

    //=====================Dropdown============================
    // Method to select a dropdown by visible text
    public void selectByVisibleText(By by, String value) {
        try {
            WebElement element = driver.findElement(by);
            new Select(element).selectByVisibleText(value);
            applyBorder(by, "green");
            log.info("Selected dropdown value: {} ", value);
        } catch (Exception e) {
            applyBorder(by, "red");
            log.error("Unable to select dropdown value: {}. {}", value, e.getMessage());
        }
    }

    // Method to select a dropdown by index
    public void selectByIndex(By by, int index) {
        try {
            WebElement element = driver.findElement(by);
            new Select(element).selectByIndex(index);
            applyBorder(by, "green");
            log.info("Selected dropdown value by index: {}", index);
        } catch (Exception e) {
            applyBorder(by, "red");
            log.error("Unable to select dropdown by index: {}. {}",index, e);
        }
    }

    // Method to get all options from a dropdown
    public List<String> getDropdownOptions(By by) {
        List<String> optionsList = new ArrayList<>();
        try {
            WebElement dropdownElement = driver.findElement(by);
            Select select = new Select(dropdownElement);
            for (WebElement option : select.getOptions()) {
                optionsList.add(option.getText());
            }
            applyBorder(by, "green");
            log.info("Retrieved dropdown options for {}", getElementDescription(by));
        } catch (Exception e) {
            applyBorder(by, "red");
            log.error("Unable to get dropdown options. {} ", e.getMessage());
        }
        return optionsList;
    }

    //=======================Waits==============================
    // Wait for the page to load
    public void waitForPageLoad(int timeOutInSec) {
        try {
            wait.withTimeout(Duration.ofSeconds(timeOutInSec)).until(WebDriver -> Objects.equals(((JavascriptExecutor) WebDriver)
                    .executeScript("return document.readyState"), "complete"));
            log.info("Page loaded successfully.");
        } catch (Exception e) {
            log.error("Page did not load within {} seconds. Exception: {}", timeOutInSec, e.getMessage());
        }
    }

    private void waitForElementToBeClickable(By by) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(by));
        } catch (Exception e) {
            log.error("Element is not clickable: {}", e.getMessage());
        }
    }

    // Wait for Element to be Visible
    private void waitForElementToBeVisible(By by) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (Exception e) {
            log.error("Element is not visible: {}", e.getMessage());
        }
    }

    /* ======================Helpers============================= */

    // Method to get the description of an element using By locator
    public String getElementDescription(By locator) {
        // Check for null driver or locator to avoid NullPointerException
        if (driver == null) {
            return "Driver is not initialized.";
        }
        if (locator == null) {
            return "Locator is null.";
        }

        try {
            // Find the element using the locator
            WebElement element = driver.findElement(locator);

            // Get element attributes
            String name = element.getDomProperty("name");
            String id = element.getDomProperty("id");
            String text = element.getText();
            String className = element.getDomProperty("class");
            String placeholder = element.getDomProperty("placeholder");

            // Return a description based on available attributes
            if (isNotEmpty(name)) {
                return "Element with name: " + name;
            } else if (isNotEmpty(id)) {
                return "Element with ID: " + id;
            } else if (isNotEmpty(text)) {
                return "Element with text: " + truncate(text);
            } else if (isNotEmpty(className)) {
                return "Element with class: " + className;
            } else if (isNotEmpty(placeholder)) {
                return "Element with placeholder: " + placeholder;
            } else {
                return "Element located using: " + locator.toString();
            }
        } catch (Exception e) {
            // Log exception for debugging
            String err = "Unable to describe element due to error:" + e.getMessage();
            log.error(err);
            return err;
        }
    }

    // Utility method to check if a string is not null or empty
    private boolean isNotEmpty(String value) {
        return value != null && !value.isEmpty();
    }

    // Utility method to truncate long strings
    private String truncate(String value) {
        if (value == null || value.length() <= 50) {
            return value;
        }
        return value.substring(0, 50) + "...";
    }

    //Utility Method to Border an element
    public void applyBorder(By by,String color) {
        try {
            //Locate the element
            WebElement element = driver.findElement(by);
            //Apply the border
            String script = "arguments[0].style.border='3px solid "+color+"'";
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript(script, element);
            log.info("Applied the border with color {} to element: {}", color, getElementDescription(by));
        } catch (Exception e) {
            log.warn("Failed to apply the border to an element: {}. {}", getElementDescription(by),e.getMessage());
        }
    }
}

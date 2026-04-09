package com.bbdemo.pages;

import com.bbdemo.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage extends BasePage {

    private final String URL = "https://www.bestbuy.ca/";
    private final By DISCLAIMER_CLOSE_BUTTON = By.cssSelector("button[aria-label='Close']");

    private final By SEARCHBAR = By.cssSelector("input[data-testid='search-input']");
    private final By SEARCH_BUTTON = By.cssSelector("button[data-testid='search-submit']");
    private final By LOGO = By.cssSelector("a[data-automation='x-logo-link']");

    private final By STORES_LINK = By.cssSelector("a[data-testid='stores-link']");
    private final By FAVOURITES_LINK = By.cssSelector("a[data-testid='favourite-indicator-in-global-header']");
    private final By ACCOUNT_LINK = By.cssSelector("a[data-automation='sign-in-link']");
    private final By CART_LINK = By.cssSelector("a[data-automation='x-basket']");


    public HomePage(WebDriver driver) {
        super(driver);
    }

    public void loadPage(){
        driver.get(URL);
        waitForPageLoad(3);
        click(DISCLAIMER_CLOSE_BUTTON);
    }

    public String getSearchedPageTitle(String product) {
        return driver.findElement(By.xpath(String.format("//h1[contains(text(), '%s')]", product))).getText();
    }

    public void searchForProducts(String product) {
        enterText(SEARCHBAR, product);
        click(SEARCH_BUTTON);
        waitForPageLoad(5);
    }

    public boolean isLogoDisplayed(){
        return isDisplayed(LOGO);
    }

    public boolean isStoresDisplayed(){
        return isDisplayed(STORES_LINK);
    }

    public boolean isFavouritesDisplayed() {
        return isDisplayed(FAVOURITES_LINK);
    }

    public boolean isAccountDisplayed() {
        return isDisplayed(ACCOUNT_LINK);
    }

    public boolean isCartDisplayed() {
        return isDisplayed(CART_LINK);
    }
}



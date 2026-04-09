package com.bbdemo.test;

import com.bbdemo.base.BaseTest;
import com.bbdemo.pages.HomePage;
import com.bbdemo.utils.DriverFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.bbdemo.utils.SoftAssertManager.assertTrue;

public class HomeTest extends BaseTest {

    private HomePage page;

    @BeforeMethod
    public void setup(){
        page = new HomePage(DriverFactory.getDriver());
        page.loadPage();
    }

    @Test
    public void verifyPageLoad(){
        assertTrue(page.isLogoDisplayed(), "Logo is not visible.");
        assertTrue(page.isStoresDisplayed(), "Link to Stores is not visible");
        assertTrue(page.isCartDisplayed(), "Link to Cart is not visible");
        assertTrue(page.isAccountDisplayed(), "Link to My Best Buy Account is not visible");
    }
}

package com.bbdemo.base;

import com.bbdemo.utils.ConfigManager;
import com.bbdemo.utils.DriverFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class BaseTest {
    @BeforeMethod
    public void setUp() {
        DriverFactory.initDriver();

        DriverFactory.getDriver().get(ConfigManager.get("url_base"));
        staticWait();
    }

    @AfterMethod
    public void tearDown() {
        DriverFactory.quitDriver();
    }

    private void staticWait() {
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(2));
    }
}

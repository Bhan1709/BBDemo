package com.bbdemo.base;

import com.bbdemo.utils.ConfigManager;
import com.bbdemo.utils.DriverFactory;
import com.bbdemo.utils.SoftAssertManager;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class BaseTest {
    @BeforeMethod
    public void baseSetUp() {
        DriverFactory.initDriver();
        SoftAssertManager.initSoftAssert();
    }

    @AfterMethod
    public void baseTearDown() {
        SoftAssertManager.assertAll();
        DriverFactory.quitDriver();
    }


}

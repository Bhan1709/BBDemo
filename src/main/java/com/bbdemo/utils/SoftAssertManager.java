package com.bbdemo.utils;

import org.testng.asserts.SoftAssert;

public class SoftAssertManager {
    private static final ThreadLocal<SoftAssert> softAssert = new ThreadLocal<>();

    public static void initSoftAssert() {
        softAssert.set(new SoftAssert());
    }

    public static SoftAssert get() {
        return softAssert.get();
    }

    public static void assertTrue(boolean condition, String message) {
        get().assertTrue(condition, message);
    }

    public static void assertFalse(boolean condition, String message) {
        get().assertFalse(condition, message);
    }

    public static void assertEquals(Object actual, Object expected, String message) {
        get().assertEquals(actual, expected, message);
    }

    public static void assertAll() {
        get().assertAll();
        softAssert.remove();
    }
}

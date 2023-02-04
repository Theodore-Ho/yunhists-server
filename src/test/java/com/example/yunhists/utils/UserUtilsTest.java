package com.example.yunhists.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class UserUtilsTest {

    @Test
    public void testAllMethodsInUserUtils() {
        assertFalse(UserUtils.validateEmail("aaa"));
        assertFalse(UserUtils.validateUsername("a"));
        assertFalse(UserUtils.validatePassword("aaa"));
        assertFalse(UserUtils.validateConfirmPassword("aaa", "a"));
    }

}
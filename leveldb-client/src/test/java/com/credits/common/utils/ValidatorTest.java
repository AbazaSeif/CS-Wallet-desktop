package com.credits.common.utils;

import com.credits.common.exception.CreditsCommonException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValidatorTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidatorTest.class);

    @Test
    public void testValidateLong() {
        String value = "1";
        try {
            Validator.validateLong(value);
            LOGGER.info("Validated successfully");
        } catch (CreditsCommonException e) {
            LOGGER.error(String.format("Validation error: %s", e.getMessage()));
        }
    }

    @Test
    public void testValidateInteger() {
        String value = "12";
        try {
            Validator.validateInteger(value);
            LOGGER.info("Validated successfully");
        } catch (CreditsCommonException e) {
            LOGGER.error(String.format("Validation error: %s", e.getMessage()));
        }
    }

    @Test
    public void testValidateDouble() {
        String value = "12.2";
        try {
            Validator.validateDouble(value);
            LOGGER.info("Validated successfully");
        } catch (CreditsCommonException e) {
            LOGGER.error(String.format("Validation error: %s", e.getMessage()));
        }
    }
}

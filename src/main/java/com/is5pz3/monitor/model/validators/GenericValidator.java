package com.is5pz3.monitor.model.validators;

import com.is5pz3.monitor.exceptions.BadRequestException;

import java.util.regex.Pattern;

import static java.lang.String.valueOf;

public class GenericValidator {

    <T> T validateValue(T value, String unit_regex, String message) {
        if (!matchesPattern(unit_regex, value)) {
            throw new BadRequestException(message);
        }
        return value;
    }

    <T> String createMessage(String valueName, T value, String regex) {
        return valueName + value + " does not match pattern: " + regex;
    }

    private <T> boolean matchesPattern(String regex, T value) {
        if (value == null || valueOf(value).isBlank()) {
            return false;
        }
        return Pattern.compile(regex)
                .matcher(valueOf(value))
                .matches();
    }
}

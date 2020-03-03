package com.unloadbrain.assignment.travix.busyflights.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IATACodeValidator implements ConstraintValidator<IATACode, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        // TODO: Read IATA Code from a file here and validate here.

        return true;
    }
}
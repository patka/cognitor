package org.cognitor.server.platform.validation;

import org.apache.commons.beanutils.PropertyUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ValidationException;

/**
 * User: patrick
 * Date: 12.12.12
 */
public class FieldEqualValidator implements ConstraintValidator<FieldEqual, Object> {
    private String fieldName;
    private String verificationFieldName;
    @Override
    public void initialize(FieldEqual constraintAnnotation) {
        fieldName = constraintAnnotation.field();
        verificationFieldName = constraintAnnotation.verificationField();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }


        Object field1 = getFieldValue(value, fieldName);
        Object field2 = getFieldValue(value, verificationFieldName);

        boolean result;
        if (field1 == null && field2 == null) {
            result =  true;
        } else if (field1 == null ^ field2 == null) {
            result = false;
        } else {
            result = field1.equals(field2);
        }
        if (!result) {
            String defaultMessage = context.getDefaultConstraintMessageTemplate();
            context.buildConstraintViolationWithTemplate(defaultMessage).addNode(fieldName).addConstraintViolation();
        }
        return result;
    }

    private Object getFieldValue(Object source, String fieldName) {
        try {
            return PropertyUtils.getProperty(source, fieldName);
        } catch (Exception exception) {
            throw new ValidationException("Error retrieving value for field " +
                    fieldName + " on object " + source.toString(), exception);
        }
    }
}

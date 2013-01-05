package org.cognitor.server.platform.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Checks if two fields provided are equal. The equals() method
 * is used to perform the check if both arguments are not null.
 *
 * If both fields are null the fields are considered equal to not
 * fail on a nullable field.
 *
 * @author Patrick Kranz
 */
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FieldEqualValidator.class)
@Documented
public @interface FieldEqual {
    String message() default "{org.cognitor.constraints.fieldequal}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String field();

    String verificationField();
}

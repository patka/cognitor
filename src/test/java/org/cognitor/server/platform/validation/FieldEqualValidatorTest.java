package org.cognitor.server.platform.validation;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.validation.ConstraintValidatorContext;
import javax.validation.ValidationException;

import static org.mockito.Mockito.*;

/**
 * User: patrick
 * Date: 12.12.12
 */
@RunWith(MockitoJUnitRunner.class)
public class FieldEqualValidatorTest {

    @Mock
    private FieldEqual fieldEqualMock;

    @Mock
    private ConstraintValidatorContext contextMock;

    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder builderMock;

    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderDefinedContext nodeContextMock;

    private FieldEqualValidator validator;

    @Before
    public void setup() {
        String defaultMessage = "{fieldequal}";
        when(contextMock.getDefaultConstraintMessageTemplate()).thenReturn(defaultMessage);
        when(contextMock.buildConstraintViolationWithTemplate(defaultMessage)).thenReturn(builderMock);
        when(builderMock.addNode("field")).thenReturn(nodeContextMock);
        when(fieldEqualMock.field()).thenReturn("field");
        when(fieldEqualMock.verificationField()).thenReturn("verifyField");
        validator = new FieldEqualValidator();
        validator.initialize(fieldEqualMock);
    }

    @Test
    public void shouldReturnTrueWhenNullToValidateGiven() {
        Assert.assertTrue(validator.isValid(null, contextMock));
        verify(nodeContextMock, never()).addConstraintViolation();
    }

    @Test
    public void shouldReturnTrueWhenBothFieldsNullGiven() {
        Assert.assertTrue(validator.isValid(new TestObject(null, null), contextMock));
        verify(nodeContextMock, never()).addConstraintViolation();
    }

    @Test
    public void shouldReturnFalseWhenOnlyFirstFieldNullGiven() {
        Assert.assertFalse(validator.isValid(new TestObject(null, "test"), contextMock));
        verify(nodeContextMock, times(1)).addConstraintViolation();
    }

    @Test
    public void shouldReturnFalseWhenOnlySecondFieldNullGiven() {
        Assert.assertFalse(validator.isValid(new TestObject("test", null), contextMock));
        verify(nodeContextMock, times(1)).addConstraintViolation();
    }

    @Test
    public void shouldReturnTrueWhenBothFieldsEqualGiven() {
        Assert.assertTrue(validator.isValid(new TestObject("test", "test"), contextMock));
        verify(nodeContextMock, never()).addConstraintViolation();
    }

    @Test
    public void shouldReturnFalseWhenDifferentValuesGiven() {
        Assert.assertFalse(validator.isValid(new TestObject("test", "alsoTest"), contextMock));
        verify(nodeContextMock, times(1)).addConstraintViolation();
    }

    @Test(expected = ValidationException.class)
    public void shouldThrowExceptionWhenWrongFieldNamesGiven() {
        when(fieldEqualMock.field()).thenReturn("wrongField");
        when(fieldEqualMock.verificationField()).thenReturn("evenMoreWrongField");
        validator.initialize(fieldEqualMock);

        validator.isValid(new TestObject("test", "alsoTest"), contextMock);
    }

}

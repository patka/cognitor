package org.cognitor.server.platform.validation;

/**
 * @author Patrick Kranz
 */
public class TestObject {
    private String field;
    private String verifyField;

    public TestObject(String fieldValue, String verifyFieldValue) {
        this.field = fieldValue;
        this.verifyField = verifyFieldValue;
    }

    public String getField() {
        return field;
    }

    public String getVerifyField() {
        return verifyField;
    }
}
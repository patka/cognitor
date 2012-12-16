package org.cognitor.server.platform.validation;

/**
 * User: patrick
 * Date: 12.12.12
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
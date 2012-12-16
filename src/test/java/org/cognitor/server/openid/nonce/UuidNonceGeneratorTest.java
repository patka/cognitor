package org.cognitor.server.openid.nonce;

import org.joda.time.DateTimeUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Patrick Kranz
 */
public class UuidNonceGeneratorTest {
    private UuidNonceGenerator generator;

    @Before
    public void setup() {
        DateTimeUtils.setCurrentMillisFixed(0);
        generator = new UuidNonceGenerator();
    }

    @After
    public void tearDown() {
        DateTimeUtils.setCurrentMillisSystem();
    }

    @Test
    public void shouldReturnNonceTokenWithCurrentTimeWhenDateTimeGiven() {
        String nonceToken = generator.next();

        Assert.assertTrue(nonceToken.startsWith("1970-01-01T00:00:00Z"));
    }

    @Test
    public void shouldReturnTwoDifferentNonceTokenWhenTwoCallsAtTheSameTimeGiven() {
        String firstToken = generator.next();
        String secondToken = generator.next();

        Assert.assertTrue(firstToken.startsWith("1970-01-01T00:00:00Z"));
        Assert.assertTrue(secondToken.startsWith("1970-01-01T00:00:00Z"));
        Assert.assertFalse("Tokens should differe, but are the same",
                firstToken.equals(secondToken));
    }
}

package org.cognitor.server.openid.web;

import org.junit.Test;

import static junit.framework.Assert.*;

/**
 * @author Patrick Kranz
 */
public class XrdsDocumentBuilderTest {

    @Test
    public void shouldReturnEmptyDocumentWhenNoServiceWasAdded() throws Exception {
        String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
                + "<xrds:XRDS xmlns:xrds=\"xri://$xrds\">"
                + "<XRD xmlns=\"xri://$xrd*($v*2.0)\"/></xrds:XRDS>";
        XrdsDocumentBuilder builder = new XrdsDocumentBuilder();
        String result = builder.getDocumentAsString();
        assertNotNull(result);
        assertEquals(expectedResult, result);
    }

    @Test
    public void shouldReturnDocumentWithServerServiceElementWhenServerUriGiven() throws Exception {
        XrdsDocumentBuilder builder = new XrdsDocumentBuilder();
        builder.addServerService("http://server.url");
        String result = builder.getDocumentAsString();
        assertNotNull(result);
        assertTrue(result.contains("<Service priority=\"0\""));
        assertTrue(result.contains("<URI>http://server.url</URI>"));
        assertTrue(result.contains("<Type>http://specs.openid.net/auth/2.0/server</Type>"));
    }

    @Test
    public void shouldReturnDocumentWithSignonServiceElementWhenSigonUriGiven() throws Exception {
        XrdsDocumentBuilder builder = new XrdsDocumentBuilder();
        builder.addSignOnSerice("http://server.url");
        String result = builder.getDocumentAsString();
        assertNotNull(result);
        assertTrue(result.contains("<Service priority=\"0\""));
        assertTrue(result.contains("<URI>http://server.url</URI>"));
        assertTrue(result.contains("<Type>http://specs.openid.net/auth/2.0/signon</Type>"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenNullAsUriGiven() {
        XrdsDocumentBuilder builder = new XrdsDocumentBuilder();
        builder.addSignOnSerice(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenEmptyStringAsUriGiven() {
        XrdsDocumentBuilder builder = new XrdsDocumentBuilder();
        builder.addSignOnSerice("");
    }

}

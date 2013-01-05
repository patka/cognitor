package org.cognitor.server.platform.web.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.Assert.assertEquals;
import static org.cognitor.server.platform.web.util.UrlUtil.appendQueryToUrl;

/**
 * @author Patrick Kranz
 */
@RunWith(MockitoJUnitRunner.class)
public class UrlUtilTest {

    @Test
    public void shouldReturnEmptyStringWhenNoQueryGiven() {
        String query = UrlUtil.createQueryString(null);
        assertEquals("", query);
    }

    @Test
    public void shouldReturnEmptyStringWhenEmptyQueryGiven() {
        String query = UrlUtil.createQueryString("");
        assertEquals("", query);
    }

    @Test
    public void shouldReturnGivenQueryWithQuestionMarkPrefixedWhenQueryGiven(){
        String query = UrlUtil.createQueryString("key=value");
        assertEquals("?key=value", query);
    }

    @Test
    public void shouldReturnUrlWithAppendedQueryWhenQueryAndUrlGiven() {
        assertEquals("http://localhost?key=value",
                appendQueryToUrl("http://localhost", "key=value"));
    }

    @Test
    public void shouldReturnUrlWhenUrlAndNoQueryGiven() {
        assertEquals("http://localhost", appendQueryToUrl("http://localhost", null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenNullValueForUrlGiven() {
        appendQueryToUrl(null, null);
    }
}

package org.cognitor.server.platform.web.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.Assert.assertEquals;

/**
 * User: patrick
 * Date: 10.12.12
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
}

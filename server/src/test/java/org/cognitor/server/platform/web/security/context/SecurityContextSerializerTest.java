package org.cognitor.server.platform.web.security.context;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;

import static junit.framework.Assert.*;

/**
 * @author Patrick Kranz
 */
public class SecurityContextSerializerTest {
    private SecurityContextSerializer serializer;

    @Before
    public void setUp() {
        serializer = new SecurityContextSerializer();
    }

    @Test(expected = SerializeException.class)
    public void shouldThrowExceptionWhenNullToSerializeGiven() {
        serializer.serialize(null);
    }

    @Test
    public void shouldReturnSerializedContextWhenValidSecurityContextGiven() {
        SecurityContext context = new SecurityContextImpl();
        context.setAuthentication(new UsernamePasswordAuthenticationToken("testUser", "test"));
        byte[] serializedData = serializer.serialize(context);
        assertNotNull(serializedData);
    }

    @Test
    public void shouldDeserializeDataWhenSerializedDataGiven() {
        SecurityContext context = new SecurityContextImpl();
        context.setAuthentication(new UsernamePasswordAuthenticationToken("testUser", "test"));
        byte[] serializedData = serializer.serialize(context);
        assertNotNull(serializedData);
        SecurityContext serializedContext = serializer.deserialize(serializedData);
        assertNotSame(context, serializedContext);
        assertEquals(context.getAuthentication().getPrincipal(),
                serializedContext.getAuthentication().getPrincipal());
    }

    @Test(expected = SerializeException.class)
    public void shouldThrowExceptionWhenUnserializeableContextGiven() {
        SecurityContext context = new SecurityContextImpl();
        context.setAuthentication(new UsernamePasswordAuthenticationToken("testUser", new Object()));
        serializer.serialize(context);
    }

    @Test(expected = SerializeException.class)
    public void shouldThrowExceptionWhenNullToDeserializeGiven() {
        serializer.deserialize(null);
    }

    @Test(expected = SerializeException.class)
    public void shouldThrowExceptionWhenInvalidDataStringGiven() {
        serializer.deserialize(new byte[]{});
    }
}


package org.cognitor.server.platform.web.security.context;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;

import java.io.*;
import java.nio.charset.Charset;

import static org.apache.commons.codec.binary.Base64.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SecurityCookieMarshallerTest {

    @Mock
    private SecurityContextSerializer serializerMock;

    @Mock
    private Sha512Hash hashMock;

    private DateTime now;
    private SecurityCookieMarshaller marshaller;

    @Before
    public void setUp() {
        marshaller = new SecurityCookieMarshaller(serializerMock, hashMock);
        now = DateTime.now();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenNullValueGiven() {
        marshaller.getBase64EncodedValue(null);
    }

    @Test
    public void shouldReturnValueWithValidUntilWhenSecurityCookieGiven() throws Exception {
        // GIVEN
        SecurityContext securityContext = new SecurityContextImpl();
        byte[] serializedContext = new byte[0];
        when(serializerMock.serialize(securityContext)).thenReturn(serializedContext);

        // WHEN
        SecurityCookie cookie = new SecurityCookie(securityContext, now);
        String base64EncodedData = marshaller.getBase64EncodedValue(cookie);

        // THEN
        String[] values = base64EncodedData.split("&");
        byte[] decodedValue = decodeBase64(values[0]);
        DataInputStream inputStream = getDataInputStream(decodedValue);
        long validUntil = inputStream.readLong();
        assertEquals(now.getMillis(), validUntil);
    }

    @Test
    public void shouldReturnValueWithSerializedContextWhenSecurityCookieGiven() throws Exception {
        // GIVEN
        SecurityContext securityContext = new SecurityContextImpl();
        byte[] serializedContext = new byte[] { 1, 2, 3 };
        when(serializerMock.serialize(securityContext)).thenReturn(serializedContext);

        // WHEN
        SecurityCookie cookie = new SecurityCookie(securityContext, now);
        String base64EncodedData = marshaller.getBase64EncodedValue(cookie);

        // THEN
        String[] values = base64EncodedData.split("&");
        byte[] decodedValue = decodeBase64(values[0]);
        DataInputStream inputStream = getDataInputStream(decodedValue);
        inputStream.skipBytes(Long.SIZE / 8);
        byte[] serializedData = new byte[3];
        inputStream.read(serializedData);
        assertArrayEquals(serializedContext, serializedData);
    }

    @Test
    public void shouldReturnValueWithHashWhenValidSecurityCookieGiven() throws Exception {
        // GIVEN
        SecurityContext securityContext = new SecurityContextImpl();
        byte[] serializedContext = new byte[0];
        byte[] hash = new byte[] { 7, 8, 9 };
        when(serializerMock.serialize(securityContext)).thenReturn(serializedContext);
        when(hashMock.createHash(any(byte[].class))).thenReturn(hash);

        // WHEN
        SecurityCookie cookie = new SecurityCookie(securityContext, now);
        String base64EncodedData = marshaller.getBase64EncodedValue(cookie);

        // THEN
        String[] values = base64EncodedData.split("&");
        byte[] decodedValue = decodeBase64(values[1]);
        assertArrayEquals(hash, decodedValue);
    }

    @Test
    public void shouldReturnNullWhenNullGiven() {
        assertNull(marshaller.getSecurityCookie(null));
    }

    @Test
    public void shouldReturnNullWhenEmptyStringGiven() {
        assertNull(marshaller.getSecurityCookie(""));
    }

    @Test
    public void shouldReturnNullWhenStringWithoutDelimiterGiven() {
        assertNull(marshaller.getSecurityCookie("blabla"));
    }

    @Test
    public void shouldReturnNullWhenHashIsNotValid() {
        when(hashMock.isHashValid(decodeBase64("bla"), decodeBase64("bla"))).thenReturn(false);
        assertNull(marshaller.getSecurityCookie("bla&bla"));
    }

    @Test
    public void shouldReturnNullWhenSerializingThrowsException() {
        SecurityContext context = new SecurityContextImpl();
        SecurityCookie cookie = new SecurityCookie(context, DateTime.now());
        when(serializerMock.serialize(context)).thenThrow(new SerializeException("test"));
        assertNull(marshaller.getBase64EncodedValue(cookie));
    }

    @Test
    public void shouldReturnNullWhenDeserializingThrowsException() {
        when(hashMock.isHashValid(any(byte[].class), any(byte[].class))).thenReturn(true);
        when(serializerMock.deserialize(any(byte[].class))).thenThrow(new SerializeException("test"));
        Charset charset = Charset.forName("UTF-8");
        byte[] value = encodeBase64URLSafe("23blabla".getBytes(charset));
        assertNull(marshaller.getSecurityCookie(new String(value, charset) + "&hash"));
    }

    @Test
    public void shouldReturnNullWhenTooShortValueGiven() {
        when(hashMock.isHashValid(any(byte[].class), any(byte[].class))).thenReturn(true);
        assertNull(marshaller.getSecurityCookie("bla&bla"));
    }

    @Test
    public void shouldReturnSecurityCookieWhenStringWithValidDataGiven() throws IOException {
        byte[] testData = createValidTestData();
        String base64EncodedTestData = encodeBase64URLSafeString(testData) + "&" + "hash";
        when(hashMock.isHashValid(any(byte[].class), any(byte[].class))).thenReturn(true);
        SecurityContextImpl securityContext = new SecurityContextImpl();
        when(serializerMock.deserialize(any(byte[].class))).thenReturn(securityContext);

        SecurityCookie cookie = marshaller.getSecurityCookie(base64EncodedTestData);
        assertNotNull(cookie);
        assertEquals(new DateTime(10L), cookie.getValidUntil());
        assertEquals(securityContext, cookie.getSecurityContext());
    }

    private static DataInputStream getDataInputStream(byte[] data) {
        return new DataInputStream(new ByteArrayInputStream(data));
    }

    private static byte[] createValidTestData() throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream outputStream = new DataOutputStream(byteStream);
        outputStream.writeLong(10);
        outputStream.write(new byte[] { 1, 2, 3 });
        return byteStream.toByteArray();
    }
}

package org.cognitor.server.platform.web.security.context;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContext;

import java.io.*;

import static org.apache.commons.codec.binary.Base64.decodeBase64;
import static org.apache.commons.codec.binary.Base64.encodeBase64;
import static org.apache.commons.codec.binary.Base64.encodeBase64URLSafeString;
import static org.springframework.util.Assert.notNull;

/**
 * @author Patrick Kranz
 */
public class SecurityCookieMarshaller {
    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityCookieMarshaller.class);
    private static final String DELIMITER = "&";
    private final SecurityContextSerializer serializer;
    private final Sha512Hash sha512Hash;

    public SecurityCookieMarshaller(SecurityContextSerializer serializer, Sha512Hash sha512Hash) {
        notNull(serializer);
        notNull(sha512Hash);
        this.serializer = serializer;
        this.sha512Hash = sha512Hash;
    }

    public String getBase64EncodedValue(SecurityCookie cookie) {
        notNull(cookie);
        ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream(byteArrayStream);
        long validUntil = cookie.getValidUntil().getMillis();
        try {
            byte[] serializedContext = serializer.serialize(cookie.getSecurityContext());
            dataStream.writeLong(validUntil);
            dataStream.write(serializedContext);
            dataStream.flush();
        } catch (IOException exception) {
            LOGGER.error("Unable to write security cookie to data stream.", exception);
            return "";
        }
        byte[] hash = sha512Hash.createHash(byteArrayStream.toByteArray());
        return getBase64EncodedString(byteArrayStream.toByteArray(), hash);
    }

    private static String getBase64EncodedString(byte[] data, byte[] hash) {
        StringBuilder valueBuilder = new StringBuilder();
        valueBuilder.append(encodeBase64URLSafeString(data));
        valueBuilder.append(DELIMITER);
        valueBuilder.append(encodeBase64URLSafeString(hash));
        return valueBuilder.toString();
    }

    public SecurityCookie getSecurityCookie(String base64EncodedValue) {
        if (isEmpty(base64EncodedValue)) {
            return null;
        }

        String[] values = base64EncodedValue.split(DELIMITER);
        if (values.length != 2) {
            return null;
        }

        byte[] serializedData = decodeBase64(values[0]);
        byte[] hash = decodeBase64(values[1]);
        if (!sha512Hash.isHashValid(serializedData, hash)) {
            LOGGER.warn("Received data with invalid hash");
            return null;
        }
        return createSecurityCookieFromData(serializedData);
    }

    private SecurityCookie createSecurityCookieFromData(byte[] serializedData) {
        try {
            byte[] serializedContext = new byte[serializedData.length - Long.SIZE / 8];
            ByteArrayInputStream inputStream = new ByteArrayInputStream(serializedData);
            DataInputStream dataInputStream = new DataInputStream(inputStream);
            long validUntil = dataInputStream.readLong();
            dataInputStream.read(serializedContext);
            SecurityContext context = serializer.deserialize(serializedContext);
            return new SecurityCookie(context, new DateTime(validUntil));
        } catch (IOException exception) {
            LOGGER.error("Something really strange happened while deserializing the security cookie.", exception);
            return null;
        }
    }

    private boolean isEmpty(String base64EncodedValue) {
        return base64EncodedValue == null || base64EncodedValue.isEmpty();
    }
}

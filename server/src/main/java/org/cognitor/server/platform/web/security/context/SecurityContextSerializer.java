package org.cognitor.server.platform.web.security.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContext;

import java.io.*;

/**
 * This class serializes the {@link SecurityContext} to a byte stream
 * and back. It uses simple Object Serialization to achieve this.
 * Therefore it is important that every object that is put into
 * the {@link SecurityContext} implements the {@link Serializable}
 * marker interface.
 *
 * If anything goes wrong during the process, a runtime exception
 * of type {@link SerializeException} is thrown.
 *
 * @author Patrick Kranz
 */
public class SecurityContextSerializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityContextSerializer.class);

    public SecurityContext deserialize(byte[] serializedContext) {
        if (serializedContext == null) {
            throw new SerializeException("Serialized data must not be null");
        }

        ByteArrayInputStream memoryStream = null;
        ObjectInputStream inputStream = null;
        try {
            memoryStream = new ByteArrayInputStream(serializedContext);
            inputStream = new ObjectInputStream(memoryStream);
            return (SecurityContext) inputStream.readObject();
        } catch (IOException e) {
            throw new SerializeException("Unable to deserialize security context.", e);
        } catch (ClassNotFoundException exception) {
            throw new SerializeException("Unknown class found in serialized data. "
                    + "Maybe someone is tempering with the data.", exception);

        } finally {
            closeQuietly(memoryStream);
            closeQuietly(inputStream);
        }
    }

    public byte[] serialize(SecurityContext securityContext) throws SerializeException {
        if (securityContext == null) {
            throw new SerializeException("Security Context must not be null");
        }

        ByteArrayOutputStream memoryStream = new ByteArrayOutputStream();
        ObjectOutputStream stream = null;
        try {
            stream = new ObjectOutputStream(memoryStream);
            stream.writeObject(securityContext);
            stream.flush();
            return memoryStream.toByteArray();
        } catch (IOException exception) {
            throw new SerializeException("Error while serializing security context",
                    exception);
        } finally {
            closeQuietly(memoryStream);
            closeQuietly(stream);
        }
    }

    private static void closeQuietly(OutputStream stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException exception) {
                LOGGER.error("Error while closing stream", exception);
            }
        }
    }

    private static void closeQuietly(InputStream stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException exception) {
                LOGGER.error("Error while closing stream", exception);
            }
        }
    }
}

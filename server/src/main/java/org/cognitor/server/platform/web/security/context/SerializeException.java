package org.cognitor.server.platform.web.security.context;

/**
 * @author Patrick Kranz
 */
public class SerializeException extends RuntimeException {
    public SerializeException(String message, Exception innerException) {
        super(message, innerException);
    }

    public SerializeException(String message) {
        super(message);
    }
}

package org.cognitor.server.platform.security.password;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

/**
 * @author Patrick Kranz
 */
public class PasswordEncoderFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(PasswordEncoderFactory.class);

    private PasswordEncoderFactory() {
        // prevent instances
    }

    public static PasswordEncoder getPasswordEncoder(String algorithm) {
        switch (algorithm) {
            case "BCRYPT":
                return new BCryptPasswordEncoder();
            case "NO_HASH":
                return NoOpPasswordEncoder.getInstance();
            case "STANDARD":
                return new StandardPasswordEncoder();
            default: {
                LOGGER.error("No password encoder for algorithm " + algorithm + " found. "
                + " Password encoding is switched off.");
                return NoOpPasswordEncoder.getInstance();
            }
        }
    }
}

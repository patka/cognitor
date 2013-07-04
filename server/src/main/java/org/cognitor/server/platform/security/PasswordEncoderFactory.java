package org.cognitor.server.platform.security;

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
    public static final String BCRYPT_ENCODER = "bcrypt";
    public static final String STANDARD_ENCODER = "standard";
    public static final String NO_ENCODER = "no_hash";

    private static final Logger LOGGER = LoggerFactory.getLogger(PasswordEncoderFactory.class);

    private PasswordEncoderFactory() {
        // prevent instances
    }

    public static PasswordEncoder getPasswordEncoder(String algorithm) {
        if (algorithm == null) {
            algorithm = "";
        }

        switch (algorithm) {
            case BCRYPT_ENCODER:
                return new BCryptPasswordEncoder();
            case NO_ENCODER:
                return NoOpPasswordEncoder.getInstance();
            case STANDARD_ENCODER:
                return new StandardPasswordEncoder();
            default: {
                LOGGER.error("No password encoder for algorithm " + algorithm + " found. "
                + "Password encoding is switched off.");
                return NoOpPasswordEncoder.getInstance();
            }
        }
    }
}

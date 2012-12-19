package org.cognitor.server.openid.nonce;

import org.joda.time.DateTime;
import org.openid4java.server.NonceGenerator;
import org.openid4java.util.InternetDateFormat;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author Patrick Kranz
 */
@Component
public class UuidNonceGenerator implements NonceGenerator {

    private static final DateFormat FORMAT = new InternetDateFormat();

    @Override
    public String next() {
        String now = getCurrentTimeAsString();
        return now + UUID.randomUUID().toString();

    }

    private String getCurrentTimeAsString() {
        Date now = DateTime.now().toDate();
        return FORMAT.format(now);
    }
}

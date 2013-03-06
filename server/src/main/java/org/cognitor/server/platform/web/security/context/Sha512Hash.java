package org.cognitor.server.platform.web.security.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import static org.springframework.util.Assert.notNull;

/**
 * @author Patrick Kranz
 */
public class Sha512Hash {

    private static final Logger LOGGER = LoggerFactory.getLogger(Sha512Hash.class);

    private static final String HASH_ALGORITHM = "HmacSHA512";
    private static final String UTF_8 = "UTF-8";

    private final SecretKeySpec keySpec;


    public Sha512Hash(String salt) {
        notNull(salt);
        Charset charset = Charset.forName(UTF_8);
        keySpec = new SecretKeySpec(salt.getBytes(charset), HASH_ALGORITHM);
    }

    public byte[] createHash(byte[] dataToSign) {
        Mac shaMac = null;
        try {
            shaMac = Mac.getInstance(HASH_ALGORITHM);
            shaMac.init(keySpec);
            return shaMac.doFinal(dataToSign);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error(HASH_ALGORITHM + " not available on this system", e);
        } catch (InvalidKeyException e) {
            LOGGER.error("The specified key is not valid for this hash algorithm", e);
        }
        return null;
    }

    public boolean isHashValid(byte[] data, byte[] possibleHash) {
        if (data == null || possibleHash == null) {
            return false;
        }
        return Arrays.equals(createHash(data), possibleHash);
    }
}

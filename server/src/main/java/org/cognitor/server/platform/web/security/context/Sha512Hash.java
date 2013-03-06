package org.cognitor.server.platform.web.security.context;

import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * @author Patrick Kranz
 */
@Component
public class Sha512Hash {

    public byte[] createHash(byte[] dataToSign) {
        SecretKey hmacKey = new SecretKeySpec("anotherPass".getBytes(), "HmacSHA512");
        Mac shaMac = null;
        try {
            shaMac = Mac.getInstance("HmacSHA512");
            shaMac.init(hmacKey);
            return shaMac.doFinal(dataToSign);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvalidKeyException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    public boolean isHashValid(byte[] data, byte[] possibleHash) {
        return Arrays.equals(createHash(data), possibleHash);
    }
}

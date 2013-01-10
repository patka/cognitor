package org.cognitor.server.openid.domain;

import org.joda.time.DateTime;
import javax.validation.constraints.NotNull;

/**
 * @author Patrick Kranz
 */
public class OpenIdAssociation {
    @NotNull
    private String handle;

    @NotNull
    private String type;

    @NotNull
    private DateTime expiry;

    @NotNull
    private byte[] key;

    @SuppressWarnings("unused")
    public OpenIdAssociation() {
    }

    public OpenIdAssociation(String handle, String type, DateTime expiry, byte[] key) {
        this.handle = handle;
        this.type = type;
        this.expiry = expiry;
        this.key = key;
    }

    public String getHandle() {
        return handle;
    }

    @SuppressWarnings("unused")
    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getType() {
        return type;
    }

    @SuppressWarnings("unused")
    public void setType(String type) {
        this.type = type;
    }

    public DateTime getExpiry() {
        return expiry;
    }

    @SuppressWarnings("unused")
    public void setExpiry(DateTime expiry) {
        this.expiry = expiry;
    }

    public byte[] getKey() {
        return key;
    }

    @SuppressWarnings("unused")
    public void setKey(byte[] key) {
        this.key = key;
    }
}
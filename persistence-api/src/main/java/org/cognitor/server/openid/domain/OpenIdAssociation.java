package org.cognitor.server.openid.domain;

import org.joda.time.DateTime;
import javax.validation.constraints.NotNull;

/**
 * This is the persistent entity for an Open ID association.
 * In the server this entity is mapped to an Association object that
 * is used by the OpenID4Java framework.
 *
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

    /**
     * Constructor to create an association. All values must be provided.
     *
     * @param handle The handle is used to identify an association. It must be
     *               unique and must not be null.
     * @param type The type of the association, e.g. HMAC-SHA256.
     * @param expiry The expiry date. This indicates how long an association is valid.
     * @param key The key that is used to sign open id messages.
     */
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

package org.cognitor.server.openid.domain;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 * @author Patrick Kranz
 */
@Entity
public class OpenIdAssociation {

    @Id
    @NotNull
    @Column(unique = true)
    private String handle;

    @NotNull
    private String type;

    @NotNull
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime expiry;

    @NotNull
    private byte[] key;

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

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public DateTime getExpiry() {
        return expiry;
    }

    public void setExpiry(DateTime expiry) {
        this.expiry = expiry;
    }

    public byte[] getKey() {
        return key;
    }

    public void setKey(byte[] key) {
        this.key = key;
    }
}

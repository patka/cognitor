package org.cognitor.server.openid.persistence;

import org.cognitor.server.openid.domain.OpenIdAssociation;

/**
 * @author Patrick Kranz
 */
public interface OpenIdAssociationDao {
    void delete(String handle);
    void save(OpenIdAssociation openIdAssociation);
    OpenIdAssociation load(String identifier);
}

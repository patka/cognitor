package org.cognitor.server.openid.persistence;

import org.cognitor.server.openid.domain.OpenIdAssociation;

/**
 * User: patrick
 * Date: 19.11.12
 */
public interface OpenIdAssociationDao {
    void delete(String handle);
    void save(OpenIdAssociation openIdAssociation);
    OpenIdAssociation load(String identifier);
}

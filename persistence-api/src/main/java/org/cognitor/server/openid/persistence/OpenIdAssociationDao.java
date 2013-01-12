package org.cognitor.server.openid.persistence;

import org.cognitor.server.openid.domain.OpenIdAssociation;

/**
 * This is used to perform the persistent operations on
 * {@link OpenIdAssociation} domain objects.
 *
 * @author Patrick Kranz
 */
public interface OpenIdAssociationDao {
    /**
     * Deletes the {@link OpenIdAssociation} that is connected to
     * the given handle.
     *
     * @param handle the handle of the {@link OpenIdAssociation} to delete
     */
    void delete(String handle);

    /**
     * Saves the given {@link OpenIdAssociation} to the data store.
     *
     * @param openIdAssociation the {@link OpenIdAssociation} to save
     * @return the persisted {@link OpenIdAssociation}
     */
    OpenIdAssociation save(OpenIdAssociation openIdAssociation);

    /**
     * Tries to load the {@link OpenIdAssociation} that is connected
     * with the given handle.
     *
     * @param handle the handle identifying the {@link OpenIdAssociation}
     *               to load
     * @return {@link OpenIdAssociation} if the handle is present in the data
     *          store, null otherwise
     */
    OpenIdAssociation load(String handle);
}

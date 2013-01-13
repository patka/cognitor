package org.cognitor.server.openid.persistence.impl;

import org.cognitor.server.openid.domain.OpenIdAssociation;
import org.cognitor.server.openid.persistence.OpenIdAssociationDao;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * {@inheritDoc}
 *
 * @author Patrick Kranz
 */
@Component
public class JpaOpenIdAssociationDao implements OpenIdAssociationDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(String handle) {
        Query query = entityManager.createQuery(
                "delete from OpenIdAssociation o where o.handle = :handle");
        query.setParameter("handle", handle);
        query.executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public OpenIdAssociation save(OpenIdAssociation openIdAssociation) {
        entityManager.persist(openIdAssociation);
        return openIdAssociation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public OpenIdAssociation load(String handle) {
        return entityManager.find(OpenIdAssociation.class, handle);
    }
}

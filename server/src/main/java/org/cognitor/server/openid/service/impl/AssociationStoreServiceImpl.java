package org.cognitor.server.openid.service.impl;

import org.cognitor.server.openid.domain.OpenIdAssociation;
import org.cognitor.server.openid.persistence.OpenIdAssociationDao;
import org.joda.time.DateTime;
import org.openid4java.association.Association;
import org.openid4java.association.AssociationException;
import org.openid4java.server.ServerAssociationStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author Patrick Kranz
 */
@Service(value = "serverAssociationStore")
public class AssociationStoreServiceImpl implements ServerAssociationStore {
    private static final Logger LOGGER = LoggerFactory.getLogger(AssociationStoreServiceImpl.class);
    private OpenIdAssociationDao openIdAssociationDao;

    @Autowired
    public AssociationStoreServiceImpl(final OpenIdAssociationDao dao) {
        this.openIdAssociationDao = dao;
    }

    @Override
    public Association generate(final String type, final int expiryIn) throws AssociationException {
        String handle = createAssociationHandle();
        Association association = Association.generate(type, handle, expiryIn);
        OpenIdAssociation ssoAssociation = mapFromAssociation(association);
        openIdAssociationDao.save(ssoAssociation);
        return association;
    }

    @Override
    public Association load(final String handle) {
        final OpenIdAssociation openIdAssociation = openIdAssociationDao.load(handle);
        if (openIdAssociation == null) {
            return null;
        }
        return mapToAssociation(openIdAssociation);
    }

    @Override
    public void remove(String handle) {
        openIdAssociationDao.delete(handle);
    }

    private OpenIdAssociation mapFromAssociation(Association association) {
        return new OpenIdAssociation(association.getHandle(),
                association.getType(),
                new DateTime(association.getExpiry()),
                association.getMacKey().getEncoded()
        );
    }

    private Association mapToAssociation(OpenIdAssociation ssoAssociation) {
        Association association;

        String type = ssoAssociation.getType();
        String handle = ssoAssociation.getHandle();
        byte[] macKey = ssoAssociation.getKey();
        DateTime expiration = ssoAssociation.getExpiry();

        switch(type) {
            case Association.TYPE_HMAC_SHA1:
                association = Association.createHmacSha1(handle, macKey, expiration.toDate());
                break;
            case Association.TYPE_HMAC_SHA256:
                association = Association.createHmacSha256(handle, macKey, expiration.toDate());
                break;
            default:
                LOGGER.warn("Somehow a handle of type {0} managed to get into the persistent store.", type);
                return null;
        }

        return association;
    }

    private static String createAssociationHandle() {
        return UUID.randomUUID().toString();
    }
}

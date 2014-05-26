package org.cognitor.server.openid.persistence.impl;

import org.cognitor.server.openid.domain.OpenIdAssociation;
import org.cognitor.server.openid.persistence.OpenIdAssociationDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Order.ASCENDING;

/**
 * @author Patrick Kranz
 */
@Component
public class MongoOpenIdAssociationDao implements OpenIdAssociationDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(MongoOpenIdAssociationDao.class);
    private static final String HANDLE_FIELD = "handle";
    private MongoTemplate mongoTemplate;

    @Autowired
    public MongoOpenIdAssociationDao(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
        createIndexIfNotExisting();
    }

    private void createIndexIfNotExisting() {
        LOGGER.debug("Creating index on OpenIdAssociation collection if none exists.");
        this.mongoTemplate.indexOps(OpenIdAssociation.class).ensureIndex(
                new Index().on(HANDLE_FIELD, ASCENDING).unique());
    }

    @Override
    public void delete(String handle) {
        mongoTemplate.remove(new Query(where(HANDLE_FIELD).is(handle)), OpenIdAssociation.class);
    }

    @Override
    public OpenIdAssociation save(OpenIdAssociation openIdAssociation) {
        mongoTemplate.insert(openIdAssociation);
        return openIdAssociation;
    }

    @Override
    public OpenIdAssociation load(String handle) {
        return mongoTemplate.findOne(new Query(where(HANDLE_FIELD).is(handle)), OpenIdAssociation.class);
    }
}

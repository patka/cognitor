package org.cognitor.server.platform.user.persistence.impl;

import org.cognitor.server.openid.domain.OpenIdAssociation;
import org.cognitor.server.platform.user.domain.User;
import org.cognitor.server.platform.user.persistence.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Order.ASCENDING;

/**
 * @author Patrick Kranz
 */
@Component
public class MongoUserDao implements UserDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(MongoUserDao.class);
    private static final String EMAIL_FIELD = "email";

    private MongoTemplate mongoTemplate;

    @Autowired
    public MongoUserDao(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
        createIndexIfNotExisting();
    }

    private void createIndexIfNotExisting() {
        LOGGER.debug("Creating index on User collection if none exists.");
        this.mongoTemplate.indexOps(OpenIdAssociation.class).ensureIndex(
                new Index().on(EMAIL_FIELD, ASCENDING).unique());
    }

    @Override
    public User save(User user) {
        if (user.getId() != null && !user.getId().isEmpty()) {
            mongoTemplate.save(user);
        } else {
            mongoTemplate.insert(user);
        }
        return user;
    }

    @Override
    public boolean exists(User user) {
        List<User> userList = mongoTemplate.find(new Query(where(EMAIL_FIELD).is(user.getEmail())), User.class);
        return userList.size() > 0;
    }

    @Override
    public User load(String email) {
        return mongoTemplate.findOne(new Query(where(EMAIL_FIELD).is(email)), User.class);
    }
}

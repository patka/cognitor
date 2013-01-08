package org.cognitor.server.platform.user.persistence.impl;

import org.cognitor.server.platform.user.domain.User;
import org.cognitor.server.platform.user.domain.UserAlreadyExistsException;
import org.cognitor.server.platform.user.persistence.UserDao;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Component
public class JpaUserDao implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User save(User user) {
        if (exists(user)) {
            throw new UserAlreadyExistsException(user.getEmail());
        }

        entityManager.persist(user);
        return user;
    }

    private boolean exists(User user) {
        Query query = entityManager.createQuery(
                "select count(u.email) from User u where u.email = :email");
        query.setParameter("email", user.getEmail());
        return (Long) query.getSingleResult() > 0;
    }

    @Override
    public User load(String email) {
        Query query = entityManager.createQuery("from User u where u.email = :email");
        query.setParameter("email", email);
        try {
            return (User) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}

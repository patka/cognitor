package org.cognitor.server.platform.user.persistence.impl;

import org.cognitor.server.platform.user.domain.User;
import org.cognitor.server.platform.user.persistence.UserDao;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Component
public class JpaUserDao implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public User save(User user) {
        if (user.getId() != null && !user.getId().isEmpty()) {
            entityManager.merge(user);
        } else {
            entityManager.persist(user);
        }
        return user;
    }

    @Override
    @Transactional
    public boolean exists(User user) {
        Query query = entityManager.createQuery(
                "select count(u.email) from User u where u.email = :email");
        query.setParameter("email", user.getEmail());
        return (Long) query.getSingleResult() > 0;
    }

    @Override
    @Transactional
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

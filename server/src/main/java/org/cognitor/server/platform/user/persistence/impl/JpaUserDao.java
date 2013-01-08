package org.cognitor.server.platform.user.persistence.impl;

import org.cognitor.server.platform.user.domain.User;
import org.cognitor.server.platform.user.persistence.UserDao;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Component
public class JpaUserDao implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public void save(User user) {
        entityManager.persist(user);
    }

    @Override
    public boolean exists(User user) {
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

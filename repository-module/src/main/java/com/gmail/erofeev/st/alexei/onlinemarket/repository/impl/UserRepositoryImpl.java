package com.gmail.erofeev.st.alexei.onlinemarket.repository.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.UserRepository;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

@Repository
public class UserRepositoryImpl extends GenericRepositoryImpl<Long, User> implements UserRepository {
    @Override
    public User findByEmail(String email) {
        String hql = "select u from User u where u.email = :email";
        Query query = entityManager.createQuery(hql, User.class);
        query.setParameter("email", email);
        return (User) query.getSingleResult();
    }

    @Override
    public User findByEmailExcludeSecureApiUser(String email) {
        String hql = "select u from User u where u.email = :email and u.role.id<>4";
        Query query = entityManager.createQuery(hql, User.class);
        query.setParameter("email", email);
        return (User) query.getSingleResult();
    }

    @Override
    public Boolean isUserExist(String email) {
        String hql = "select u from User u where u.email = :email";
        Query query = entityManager.createQuery(hql);
        query.setParameter("email", email);
        List resultList = query.getResultList();
        return !resultList.isEmpty();
    }

    @Override
    public List<User> findUsersSortedByEmail(int offset, int amount, boolean showDeleted) {
        String hql;
        if (showDeleted) {
            hql = "select u from User u ORDER BY u.email asc ";
        } else {
            hql = "select u from User u where u.deleted = false ORDER BY u.email asc  ";
        }
        Query query = entityManager.createQuery(hql);
        query.setFirstResult(offset);
        query.setMaxResults(amount);
        return query.getResultList();
    }
}

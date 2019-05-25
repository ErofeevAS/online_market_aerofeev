package com.gmail.erofeev.st.alexei.onlinemarket.repository.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.UserRepository;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

@Repository
public class UserRepositoryImpl extends GenericRepositoryImpl<Long, User> implements UserRepository {
    private static final Logger logger = LoggerFactory.getLogger(UserRepositoryImpl.class);

    @Override
    public User findByEmail(String email) {
        String query = "select u from User u where u.email = :email";
        Query q = entityManager.createQuery(query, User.class);
        q.setParameter("email", email);
        return (User) q.getSingleResult();
    }

    @Override
    public int deleteUserById(Long id) {
        String hql = "update User u set  u.deleted = true  where u.id = :id and u.undeletable = false";
        Query query = entityManager.createQuery(hql);
        query.setParameter("id", id);
        return query.executeUpdate();
    }

    @Override
    public Boolean isUserExist(String email) {
        String hql = "select u from User u where u.email = :email";
        Query query = entityManager.createQuery(hql);
        query.setParameter("email", email);
        List resultList = query.getResultList();
        if (resultList.isEmpty()) {
            return false;
        }
        return true;
    }
}

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

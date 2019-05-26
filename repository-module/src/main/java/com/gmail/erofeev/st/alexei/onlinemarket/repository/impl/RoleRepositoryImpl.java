package com.gmail.erofeev.st.alexei.onlinemarket.repository.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.RoleRepository;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Role;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;

@Repository
public class RoleRepositoryImpl extends GenericRepositoryImpl<Long, Role> implements RoleRepository {
    @Override
    public Role findRoleByName(String roleName) {
        String hql = "select r from Role r where r.name = :name";
        Query query = entityManager.createQuery(hql);
        query.setParameter("name", roleName);
        return (Role) query.getSingleResult();
    }
}
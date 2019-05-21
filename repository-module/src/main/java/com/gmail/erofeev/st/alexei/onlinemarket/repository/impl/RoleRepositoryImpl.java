package com.gmail.erofeev.st.alexei.onlinemarket.repository.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.RoleRepository;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Role;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;

@Repository
public class RoleRepositoryImpl extends GenericRepositoryImpl<Long, Role> implements RoleRepository {

    @Override
    public Role findRoleByName(String roleName) {
        String query = "select r from " + entityClass.getName() + " r where r.name = :name";
        Query q = entityManager.createQuery(query);
        q.setParameter("name", roleName);
        return (Role) q.getSingleResult();
    }
}
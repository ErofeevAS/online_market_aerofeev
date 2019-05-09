package com.gmail.erofeev.st.alexei.onlinemarket.repository;

import java.sql.Connection;

public interface GenericRepository {
    Connection getConnection();

    Integer getAmount(Connection connection, String tableName);
}

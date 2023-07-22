package com.midwesttape.project.challengeapplication.model;


import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Address address = null;
        if (resultSet.getString("user.addressId") != null) {
            address = Address.builder()
                .id(resultSet.getLong("address.id"))
                .address1(resultSet.getString("address.address1"))
                .address2(resultSet.getString("address.address2"))
                .city(resultSet.getString("address.city"))
                .state(resultSet.getString("address.state"))
                .postal(resultSet.getString("address.postal"))
                .build();
        }
        return User.builder()
            .id(resultSet.getLong("user.id"))
            .firstName(resultSet.getString("user.firstName"))
            .lastName(resultSet.getString("user.lastName"))
            .username(resultSet.getString("user.username"))
            .password(resultSet.getString("user.password"))
            .address(address)
            .build();
    }
}

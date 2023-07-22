package com.midwesttape.project.challengeapplication.service;

import com.midwesttape.project.challengeapplication.model.User;
import com.midwesttape.project.challengeapplication.model.UserRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final JdbcTemplate template;

    public User user(final Long userId) {
        try {
            String query = "SELECT user.id, user.firstName, user.lastName, user.username, user.password, user.addressId, " +
                "address.id, address.address1, address.address2, address.city, address.state, address.postal " +
                "FROM User  " +
                "LEFT JOIN Address ON user.addressId = address.id " +
                "WHERE user.id = ?";
            return template.queryForObject(
                query,
                new UserRowMapper(),
                userId
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }

    }

}

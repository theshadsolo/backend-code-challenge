package com.midwesttape.project.challengeapplication.service;

import com.midwesttape.project.challengeapplication.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;

@Service
@RequiredArgsConstructor
public class RepositoryService {

    private final JdbcTemplate template;

    public void updateUser(Long userId, User updatedUser, String userUpdateQuery, Long addressId) {
        template.update(userUpdateQuery,
            updatedUser.getFirstName(),
            updatedUser.getLastName(),
            updatedUser.getUsername(),
            updatedUser.getPassword(),
            addressId,
            userId);
    }

    public void updateAddress(User updatedUser, String addressUpdateQuery) {
        template.update(addressUpdateQuery,
            updatedUser.getAddress().getAddress1(),
            updatedUser.getAddress().getAddress2(),
            updatedUser.getAddress().getCity(),
            updatedUser.getAddress().getState(),
            updatedUser.getAddress().getPostal(),
            updatedUser.getAddress().getId());
    }

    public KeyHolder createNewAddress(User updatedUser, KeyHolder keyHolder, String createNewAddressQuery) {
        template.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(createNewAddressQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, updatedUser.getAddress().getAddress1());
            ps.setString(2, updatedUser.getAddress().getAddress2());
            ps.setString(3, updatedUser.getAddress().getCity());
            ps.setString(4, updatedUser.getAddress().getState());
            ps.setString(5, updatedUser.getAddress().getPostal());
            return ps;
        }, keyHolder);
        return keyHolder;
    }
}

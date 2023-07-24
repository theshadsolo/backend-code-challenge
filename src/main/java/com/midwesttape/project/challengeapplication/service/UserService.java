package com.midwesttape.project.challengeapplication.service;

import com.midwesttape.project.challengeapplication.model.User;
import com.midwesttape.project.challengeapplication.model.UserRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final JdbcTemplate template;
    private final RepositoryService repositoryService;

    public User getUser(final Long userId) {
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

    public User updateUser(Long userId, User updatedUser) {
        boolean addressAssociated = updatedUser.getAddress() != null;
        boolean newAddressNeeded = updatedUser.getAddress() != null && updatedUser.getAddress().getId() == null;

        if (addressAssociated) {
            if (newAddressNeeded) {
                long newAddressId = createAddress(updatedUser);
                updatedUser.getAddress().setId(newAddressId);
            } else {
                doUpdateAddress(updatedUser);
            }
        }

        doUpdateUser(userId, updatedUser, addressAssociated);

        return getUser(userId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void doUpdateUser(Long userId, User updatedUser, boolean addressAssociated) {
        String userUpdateQuery = "UPDATE user " +
            "SET firstName = ?" +
            ", lastName = ?" +
            ", username = ?" +
            ", password = ?" +
            ", addressId = ?" +
            " WHERE id = ?";

        if (addressAssociated) {
            repositoryService.updateUser(userId, updatedUser, userUpdateQuery, updatedUser.getAddress().getId());
        } else {
            repositoryService.updateUser(userId, updatedUser, userUpdateQuery, null);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void doUpdateAddress(User updatedUser) {
        String addressUpdateQuery = "UPDATE address " +
            "SET address1 = ?" +
            ", address2 = ?" +
            ", city = ?" +
            ", state = ?" +
            ", postal = ?" +
            " WHERE id = ?";

        repositoryService.updateAddress(updatedUser, addressUpdateQuery);
    }

    @Transactional(rollbackFor = Exception.class)
    public long createAddress(User updatedUser) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String createNewAddressQuery = "INSERT INTO ADDRESS " +
            "(address1, address2, city, state, postal) " +
            "VALUES (?, ?, ?, ?, ?)";
        keyHolder = repositoryService.createNewAddress(updatedUser, keyHolder, createNewAddressQuery);
        // This can't return a null because if the insert fails then an exception will be thrown and it will never
        // Get to the return statement
        return (long)keyHolder.getKey();
    }


}

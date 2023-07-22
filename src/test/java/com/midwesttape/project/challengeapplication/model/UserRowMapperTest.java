package com.midwesttape.project.challengeapplication.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.when;

import java.sql.ResultSet;
import java.sql.SQLException;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;


@ExtendWith(MockitoExtension.class)
public class UserRowMapperTest {

    @Mock
    ResultSet resultSet;
    UserRowMapper mapper;

    @BeforeEach
    public void beforeEach() {
        mapper = new UserRowMapper();
    }

    @Test
    void resultSetHasAllValuesShouldReturnFullUser() throws SQLException {
        User expectedUser = setUpFullUser();

        setUpMocksFullUser();

        User actualUser = mapper.mapRow(resultSet, 1);
        assertThat(actualUser, is(equalTo(expectedUser)));
    }

    @Test
    void resultSetAddressIdIsNullReturnsUserWithoutAddress() throws SQLException {
        User expectedUser = setUpUserWithoutAddress();

        setUpMocksNoAddress();

        User actualUser = mapper.mapRow(resultSet, 1);
        assertThat(actualUser, is(equalTo(expectedUser)));
    }

    private void setUpMocksFullUser() {
        try {
            when(resultSet.getLong("user.id")).thenReturn(1234L);
            when(resultSet.getString("user.addressId")).thenReturn("5678");
            when(resultSet.getString("user.firstName")).thenReturn("Steve");
            when(resultSet.getString("user.lastName")).thenReturn("Rogers");
            when(resultSet.getString("user.username")).thenReturn("CapIsAwesome");
            when(resultSet.getString("user.password")).thenReturn("Avenger$A$$3mble");
            when(resultSet.getLong("address.id")).thenReturn(5678L);
            when(resultSet.getString("address.address1")).thenReturn("1 Avengers Road");
            when(resultSet.getString("address.address2")).thenReturn("Avengers Compound");
            when(resultSet.getString("address.city")).thenReturn("New York");
            when(resultSet.getString("address.state")).thenReturn("New York");
            when(resultSet.getString("address.postal")).thenReturn("78912");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setUpMocksNoAddress() {
        try {
            when(resultSet.getLong("user.id")).thenReturn(1234L);
            when(resultSet.getString("user.addressId")).thenReturn(null);
            when(resultSet.getString("user.firstName")).thenReturn("Steve");
            when(resultSet.getString("user.lastName")).thenReturn("Rogers");
            when(resultSet.getString("user.username")).thenReturn("CapIsAwesome");
            when(resultSet.getString("user.password")).thenReturn("Avenger$A$$3mble");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private User setUpFullUser() {
        Address address = Address.builder()
            .id(5678L)
            .address1("1 Avengers Road")
            .address2("Avengers Compound")
            .city("New York")
            .state("New York")
            .postal("78912")
            .build();
        return User.builder()
            .id(1234L)
            .firstName("Steve")
            .lastName("Rogers")
            .username("CapIsAwesome")
            .password("Avenger$A$$3mble")
            .address(address)
            .build();
    }

    private User setUpUserWithoutAddress() {
        return User.builder()
            .id(1234L)
            .firstName("Steve")
            .lastName("Rogers")
            .username("CapIsAwesome")
            .password("Avenger$A$$3mble")
            .address(null)
            .build();
    }
}

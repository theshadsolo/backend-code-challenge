package com.midwesttape.project.challengeapplication.service;

import com.midwesttape.project.challengeapplication.model.Address;
import com.midwesttape.project.challengeapplication.model.User;
import com.midwesttape.project.challengeapplication.model.UserRowMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.KeyHolder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserServiceTest {

    private static final Long USER_ID = 1234L;

    @Mock
    private JdbcTemplate template;

    @Mock
    private RepositoryService repositoryService;

    @Mock
    private KeyHolder keyHolder;

    private UserService userService;

    User mockedUser;
    User mockedUserWithoutAddress;

    String userUpdateQuery;
    String addressUpdateQuery;
    String createNewAddressQuery;

    @BeforeEach
    public void beforeEach() {
        userService = new UserService(template, repositoryService);
        mockedUser = createExpectedAndMockedUsers();
        mockedUserWithoutAddress = createUserWithoutAddress();
        userUpdateQuery = "UPDATE user " +
            "SET firstName = ?" +
            ", lastName = ?" +
            ", username = ?" +
            ", password = ?" +
            ", addressId = ?" +
            " WHERE id = ?";
        addressUpdateQuery = "UPDATE address " +
            "SET address1 = ?" +
            ", address2 = ?" +
            ", city = ?" +
            ", state = ?" +
            ", postal = ?" +
            " WHERE id = ?";
        createNewAddressQuery = "INSERT INTO ADDRESS " +
            "(address1, address2, city, state, postal) " +
            "VALUES (?, ?, ?, ?, ?)";
    }

    @Test
    void should_get_user() {

        final User user = new User();

        when(template.queryForObject(anyString(), isA(UserRowMapper.class), eq(USER_ID))).thenReturn(user);

        final User resultUser = userService.getUser(USER_ID);

        assertEquals(user, resultUser);
    }

    @Test
    void should_get_user_with_address() {
        User expectedUser = createExpectedAndMockedUsers();

        when(template.queryForObject(anyString(), isA(UserRowMapper.class), eq(USER_ID))).thenReturn(mockedUser);

        final User resultUser = userService.getUser(USER_ID);

        assertThat(expectedUser, is(equalTo(resultUser)));
    }

    @Test
    void shouldUpdateUserWithoutAddress() {
        User expectedUser = createUserWithoutAddress();
        expectedUser.setUsername("CapNoSwear");

        User updatedUser = createUserWithoutAddress();
        updatedUser.setUsername("CapNoSwear");

        mockedUserWithoutAddress.setUsername("CapNoSwear");

        when(template.queryForObject(anyString(), isA(UserRowMapper.class), eq(USER_ID))).thenReturn(mockedUserWithoutAddress);
        doNothing().when(repositoryService).updateUser(updatedUser.getId(), updatedUser, userUpdateQuery, null);

        User actual = userService.updateUser(1234L, updatedUser);

        verify(repositoryService).updateUser(updatedUser.getId(), updatedUser, userUpdateQuery, null);
        assertThat(actual, is(equalTo(expectedUser)));
    }

    @Test
    void shouldUpdateUserWithAddress() {
        User expectedUser = createExpectedAndMockedUsers();
        expectedUser.setUsername("CapNoSwear");

        User updatedUser = createExpectedAndMockedUsers();
        updatedUser.setUsername("CapNoSwear");

        mockedUser.setUsername("CapNoSwear");

        when(template.queryForObject(anyString(), isA(UserRowMapper.class), eq(USER_ID))).thenReturn(mockedUser);
        doNothing().when(repositoryService).updateUser(updatedUser.getId(), updatedUser, userUpdateQuery, updatedUser.getAddress().getId());
        doNothing().when(repositoryService).updateAddress(updatedUser, addressUpdateQuery);

        User actual = userService.updateUser(1234L, updatedUser);

        verify(repositoryService).updateUser(updatedUser.getId(), updatedUser, userUpdateQuery, updatedUser.getAddress().getId());
        verify(repositoryService).updateAddress(updatedUser, addressUpdateQuery);
        assertThat(actual, is(equalTo(expectedUser)));

    }

    @Test
    void shouldUpdateUserWithNewAddress() {
        User expectedUser = createExpectedAndMockedUsers();
        expectedUser.setUsername("CapNoSwear");

        User updatedUser = createExpectedAndMockedUsers();
        updatedUser.setUsername("CapNoSwear");
        updatedUser.getAddress().setId(null);

        mockedUser.setUsername("CapNoSwear");

        when(template.queryForObject(anyString(), isA(UserRowMapper.class), eq(USER_ID))).thenReturn(mockedUser);
        when(keyHolder.getKey()).thenReturn(1234L);
        doNothing().when(repositoryService).updateUser(updatedUser.getId(), updatedUser, userUpdateQuery, updatedUser.getAddress().getId());
        when(repositoryService.createNewAddress(eq(updatedUser), any(), eq(createNewAddressQuery))).thenReturn(keyHolder);

        User actual = userService.updateUser(1234L, updatedUser);

        verify(repositoryService).updateUser(updatedUser.getId(), updatedUser, userUpdateQuery, updatedUser.getAddress().getId());
        verify(repositoryService).createNewAddress(eq(updatedUser), any(), eq(createNewAddressQuery));
        assertThat(actual, is(equalTo(expectedUser)));
    }

    private User createExpectedAndMockedUsers() {
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

    private User createUserWithoutAddress() {
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
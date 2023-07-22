package com.midwesttape.project.challengeapplication.service;

import com.midwesttape.project.challengeapplication.model.Address;
import com.midwesttape.project.challengeapplication.model.User;
import com.midwesttape.project.challengeapplication.model.UserRowMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private static final Long USER_ID = 1234L;

    @Mock
    private JdbcTemplate template;

    private UserService userService;

    User mockedUser;

    Address address;

    @BeforeEach
    public void beforeEach() {
        userService = new UserService(template);
        mockedUser = createExpectedAndMockedUsers();
    }

    @Test
    void should_get_user() {

        final User user = new User();

        when(template.queryForObject(anyString(), isA(UserRowMapper.class), eq(USER_ID))).thenReturn(user);

        final User resultUser = userService.user(USER_ID);

        assertEquals(user, resultUser);
    }

    @Test
    void should_get_user_with_address() {
        User expectedUser = createExpectedAndMockedUsers();

        when(template.queryForObject(anyString(), isA(UserRowMapper.class), eq(USER_ID))).thenReturn(mockedUser);

        final User resultUser = userService.user(USER_ID);

        assertThat(expectedUser, is(equalTo(resultUser)));
    }

    private User createExpectedAndMockedUsers() {
        Address address = Address.builder()
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

}
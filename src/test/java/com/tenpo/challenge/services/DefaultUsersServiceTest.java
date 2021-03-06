package com.tenpo.challenge.services;

import com.tenpo.challenge.model.User;
import com.tenpo.challenge.exceptions.UserAlreadyExistsException;
import com.tenpo.challenge.repository.RequestsAuditRepository;
import com.tenpo.challenge.repository.UsersRepository;
import com.tenpo.challenge.services.impl.DefaultUsersService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DefaultUsersServiceTest {

    private UsersService userService;
    private PasswordEncoder passwordEncoder;
    private UsersRepository userRepository;
    private RequestsAuditRepository requestsAuditRepository;

    @Before
    public void setUp() {
        userRepository = Mockito.mock(UsersRepository.class);
        requestsAuditRepository = Mockito.mock(RequestsAuditRepository.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new DefaultUsersService(passwordEncoder, userRepository, requestsAuditRepository);
    }

    @Test
    public void testCreateUserThenReturnSameUser() {
        User expectedUser = buildNewUserDto();
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(expectedUser);
        User response = userService.createUser(expectedUser);
        assertEquals(expectedUser, response);
    }

    @Test
    public void testCreateUserThenThrowsUserAlreadyExistsException() {
        User expectedUser = buildNewUserDto();
        Mockito.when(userRepository.findByUserName(Mockito.anyString())).thenReturn(Optional.of(expectedUser));
        Throwable ex = assertThrows(UserAlreadyExistsException.class, () -> {
            userService.createUser(expectedUser);});
        assertEquals(String.format("The user with name %s already exists", expectedUser.getUserName()), ex.getMessage());
    }

    private User buildNewUserDto() {
        User expectedUser = new User();
        expectedUser.setId(1L);
        expectedUser.setUserName("Santi");
        expectedUser.setPassword("Garcete");
        return expectedUser;
    }

}

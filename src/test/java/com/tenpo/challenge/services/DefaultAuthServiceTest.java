package com.tenpo.challenge.services;

import com.tenpo.challenge.dtos.UserDTO;
import com.tenpo.challenge.exceptions.UserAlreadyExistsException;
import com.tenpo.challenge.repository.UsersRepository;
import com.tenpo.challenge.services.impl.DefaultUsersService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DefaultAuthServiceTest {
    private UsersService userService;
    private PasswordEncoder passwordEncoder;
    private UsersRepository userRepository;

    @Before
    public void setUp() {
        userRepository = Mockito.mock(UsersRepository.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new DefaultUsersService(passwordEncoder, userRepository);
    }

    @Test
    public void testCreateUserThenReturnSameUser() {
        UserDTO expectedUser = buildNewUser();
        Mockito.when(userRepository.save(Mockito.any(UserDTO.class))).thenReturn(expectedUser);
        UserDTO response = userService.createUser(expectedUser);
        assertEquals(expectedUser, response);
    }

    @Test
    public void testCreateUserThenThrowsUserAlreadyExistsException() {
        UserDTO expectedUser = buildNewUser();
        Mockito.when(userRepository.findByUserName(Mockito.anyString())).thenReturn(Optional.of(expectedUser));
        Throwable ex = assertThrows(UserAlreadyExistsException.class, () -> {
            userService.createUser(expectedUser);});
        assertEquals(String.format("The user with name %s already exists", expectedUser.getUserName()), ex.getMessage());
    }

    private UserDTO buildNewUser() {
        UserDTO expectedUser = new UserDTO();
        expectedUser.setId(1L);
        expectedUser.setUserName("Santi");
        expectedUser.setPassword("Garcete");
        return expectedUser;
    }

}

package com.tenpo.challenge.services;

import com.tenpo.challenge.model.User;
import com.tenpo.challenge.exceptions.PasswordNotValidException;
import com.tenpo.challenge.exceptions.UserAlreadyLoggedException;
import com.tenpo.challenge.exceptions.UserNotFoundException;
import com.tenpo.challenge.exceptions.UserNotLoggedException;
import com.tenpo.challenge.repository.RequestsAuditRepository;
import com.tenpo.challenge.repository.UsersRepository;
import com.tenpo.challenge.services.impl.DefaultAuthService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DefaultAuthServiceTest {
    private AuthService authService;
    private UsersRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private RequestsAuditRepository requestsAuditRepository;
    private User user;

    @Before
    public void setUp() {
        userRepository = Mockito.mock(UsersRepository.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        requestsAuditRepository = Mockito.mock(RequestsAuditRepository.class);
        authService = new DefaultAuthService(passwordEncoder, userRepository, requestsAuditRepository);
        user = buildNewUser();
        authService.deleteAllUsersTokens();
    }

    @Test
    public void testLoginUserThenReturnsUser() {
        Mockito.when(userRepository.findByUserName(Mockito.anyString())).thenReturn(Optional.of(this.user));
        Mockito.when(passwordEncoder.matches(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        User response = authService.loginUser(this.user.getUserName(), this.user.getPassword());
        assertEquals(this.user, response);
    }

    @Test
    public void testLoginUserThenThrowsUserNotFoundException() {
        Mockito.when(userRepository.findByUserName(Mockito.anyString())).thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.matches(Mockito.anyString(), Mockito.anyString())).thenReturn(false);

        Throwable ex = assertThrows(UserNotFoundException.class, () -> {authService.loginUser(user.getUserName(), user.getPassword());});
        assertEquals(String.format("The user with name %s does not exists", user.getUserName()), ex.getMessage());
    }

    @Test
    public void testLoginUserThenThrowsPasswordNotValidException() {
        Mockito.when(userRepository.findByUserName(Mockito.anyString())).thenReturn(Optional.of(this.user));

        Throwable ex = assertThrows(PasswordNotValidException.class, () -> {authService.loginUser(user.getUserName(), "abc");});
        assertEquals(String.format("Password not valid for user %s", user.getUserName()), ex.getMessage());
    }

    @Test
    public void testUserIsAlreadyLoggedThenThrowsUserAlreadyLoggedException() {
        authService.addActiveUserToken(user, "blabla");
        Throwable ex = assertThrows(UserAlreadyLoggedException.class, () -> {authService.checkIfUserIsAlreadyLogged(user);});
        assertEquals(String.format("The user with name %s is already logged", user.getUserName()), ex.getMessage());
    }

    @Test
    public void testLogoutUserThenReturnsUser() {
        Mockito.when(userRepository.findByUserName(Mockito.anyString())).thenReturn(Optional.of(this.user));
        authService.addActiveUserToken(user, "bla");
        User response = authService.logoutUser(this.user.getUserName());
        assertEquals(this.user, response);
    }

    @Test
    public void testLogoutUserThenThrowsUserNotFoundException() {
        Mockito.when(userRepository.findByUserName(Mockito.anyString())).thenReturn(Optional.empty());

        Throwable ex = assertThrows(UserNotFoundException.class, () -> {authService.logoutUser(user.getUserName());});
        assertEquals(String.format("The user with name %s does not exists", user.getUserName()), ex.getMessage());
    }

    @Test
    public void testLogoutUserThenThrowsUserNotLoggedException() {
        Mockito.when(userRepository.findByUserName(Mockito.anyString())).thenReturn(Optional.of(this.user));

        Throwable ex = assertThrows(UserNotLoggedException.class, () -> {authService.logoutUser(user.getUserName());});
        assertEquals(String.format("The user with name %s is not logged", user.getUserName()), ex.getMessage());
    }

    private User buildNewUser() {
        User expectedUser = new User();
        expectedUser.setId(1L);
        expectedUser.setUserName("Santi");
        expectedUser.setPassword("Garcete");
        return expectedUser;
    }

}

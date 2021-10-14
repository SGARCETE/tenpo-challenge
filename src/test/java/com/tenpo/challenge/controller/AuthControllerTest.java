package com.tenpo.challenge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenpo.challenge.dtos.User;
import com.tenpo.challenge.exceptions.PasswordNotValidException;
import com.tenpo.challenge.exceptions.UserAlreadyLoggedException;
import com.tenpo.challenge.exceptions.UserNotFoundException;
import com.tenpo.challenge.services.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
@ActiveProfiles("test")
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    private User user;
    private String token;

    @BeforeEach
    void setUp() {
        user = buildExpectedUser();
        token= "12345";
    }

    @Test
    void testAuthUserSuccessfullyWithStatus200ReturnsUserId() throws Exception {
        doReturn(user).when(authService).loginUser(anyString(), anyString());
        doReturn(token).when(authService).getAndSaveToken(any(User.class));
        doNothing().when(authService).checkIfUserIsAlreadyLogged(any(User.class));

        mockMvc.perform(post("/auth/login")
                .content(objectMapper.writeValueAsString(user))
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.token").value(token))
        ;
    }

    @Test
    void testAuthUserFailWithStatus404ReturnsUserNotFoundException() throws Exception {

        UserNotFoundException expectedException = new UserNotFoundException(String.format("The user with name %s does not exists",
                user.getUserName()));

        doNothing().when(authService).checkIfUserIsAlreadyLogged(any(User.class));
        doThrow(expectedException).when(authService).loginUser(user.getUserName(), user.getPassword());

        mockMvc.perform(post("/auth/login")
                .content(objectMapper.writeValueAsString(user))
                .contentType("application/json"))
                .andExpect(jsonPath("$.error").value("User not found Exception"))
                .andExpect(jsonPath("$.message").value(expectedException.getMessage()))
                .andExpect(jsonPath("$.status").value("404"))
        ;
    }

    @Test
    void testAuthUserPasswordFailWithStatus400ReturnsPasswordNotValidException() throws Exception {

        PasswordNotValidException expectedException = new PasswordNotValidException(String.format("Password not valid for user %s",
                user.getUserName()));

        doNothing().when(authService).checkIfUserIsAlreadyLogged(any(User.class));
        doThrow(expectedException).when(authService).loginUser(user.getUserName(), user.getPassword());

        mockMvc.perform(post("/auth/login")
                .content(objectMapper.writeValueAsString(user))
                .contentType("application/json"))
                .andExpect(jsonPath("$.error").value("Password not valid exception"))
                .andExpect(jsonPath("$.message").value(expectedException.getMessage()))
                .andExpect(jsonPath("$.status").value("400"))
        ;
    }

    @Test
    void testAuthUserIsAlreadyLoggedFailWithStatus400ReturnsUserAlreadyLoggedException() throws Exception {

        UserAlreadyLoggedException expectedException = new UserAlreadyLoggedException(String.format("The user with name %s is already logged", user.getUserName()));

        doThrow(expectedException).when(authService).checkIfUserIsAlreadyLogged(any(User.class));

        mockMvc.perform(post("/auth/login")
                .content(objectMapper.writeValueAsString(user))
                .contentType("application/json"))
                .andExpect(jsonPath("$.error").value("User already logged exception"))
                .andExpect(jsonPath("$.message").value(expectedException.getMessage()))
                .andExpect(jsonPath("$.status").value("400"))
        ;
    }

    private User buildExpectedUser() {
        return new User()
                .setId(1L)
                .setUserName("Santiago")
                .setPassword("Garcete");
    };

}

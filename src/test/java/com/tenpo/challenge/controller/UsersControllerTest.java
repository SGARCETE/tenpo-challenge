package com.tenpo.challenge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenpo.challenge.dtos.User;
import com.tenpo.challenge.exceptions.UserAlreadyExistsException;
import com.tenpo.challenge.services.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UsersController.class)
@ActiveProfiles("test")
public class UsersControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UsersService userService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testCreateUserSuccessfullyWithStatus200ReturnsUserId() throws Exception {

        User expectedUser = buildExpectedUser();

        doReturn(expectedUser).when(userService).createUser(any(User.class));

        mockMvc.perform(post("/users")
                .content(objectMapper.writeValueAsString(expectedUser))
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedUser.getId()))
        ;
    }

    @Test
    void testCreateUserFailWithStatus400UserAlreadyExistsException() throws Exception {

        User expectedUser = buildExpectedUser();

        UserAlreadyExistsException expectedException = new UserAlreadyExistsException(String.format("The user with name %s already exists",
                expectedUser.getUserName()));
        doThrow(expectedException).when(userService).createUser(any(User.class));

        mockMvc.perform(post("/users")
                .content(objectMapper.writeValueAsString(expectedUser))
                .contentType("application/json"))
                .andExpect(jsonPath("$.error").value("User already exists Exception"))
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

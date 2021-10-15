package com.tenpo.challenge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenpo.challenge.dtos.User;
import com.tenpo.challenge.exceptions.TokenNotValidException;
import com.tenpo.challenge.exceptions.UserNotFoundException;
import com.tenpo.challenge.model.SumDto;
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

@WebMvcTest(controllers = SumController.class)
@ActiveProfiles("test")
public class SumControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    private SumDto sumDto;

    @BeforeEach
    void setUp() {
        this.sumDto = buildExpectedSumDto();
    }

    @Test
    void testSumSuccessfullyWithStatus200ReturnsResult() throws Exception {
        doNothing().when(authService).checkIfUserExistsAndIsLogged(anyString(), anyString());

        mockMvc.perform(post("/sum").header("Authorization", "bla")
                .content(objectMapper.writeValueAsString(this.sumDto))
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(sumDto.getFirstNumber() + sumDto.getSecondNumber()))
        ;
    }

    @Test
    void testSumFailsWithStatus404UserDoesNotExists() throws Exception {
        UserNotFoundException expectedException = new UserNotFoundException(String.format("The user with name %s does not exists",
                sumDto.getUserName()));

        doThrow(expectedException).when(authService).checkIfUserExistsAndIsLogged(anyString(), anyString());

        mockMvc.perform(post("/sum").header("Authorization", "bla")
                .content(objectMapper.writeValueAsString(sumDto))
                .contentType("application/json"))
                .andExpect(jsonPath("$.error").value("User not found Exception"))
                .andExpect(jsonPath("$.message").value(expectedException.getMessage()))
                .andExpect(jsonPath("$.status").value("404"))
        ;
    }

    @Test
    void testSumFailsWithStatus400TokenIsNotValid() throws Exception {
        TokenNotValidException expectedException = new TokenNotValidException(String.format("The token is not valid por user %s",
                sumDto.getUserName()));

        doThrow(expectedException).when(authService).checkIfUserExistsAndIsLogged(anyString(), anyString());

        mockMvc.perform(post("/sum").header("Authorization", "bla")
                .content(objectMapper.writeValueAsString(sumDto))
                .contentType("application/json"))
                .andExpect(jsonPath("$.error").value("Token not valid exception"))
                .andExpect(jsonPath("$.message").value(expectedException.getMessage()))
                .andExpect(jsonPath("$.status").value("400"))
        ;
    }

    private SumDto buildExpectedSumDto() {
        return new SumDto()
                .setUserName("santiago")
                .setFirstNumber(1.0)
                .setSecondNumber(2.0);
    };


}

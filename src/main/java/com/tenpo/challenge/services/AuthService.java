package com.tenpo.challenge.services;

import com.tenpo.challenge.dtos.UserDTO;

public interface AuthService {
    UserDTO loginUser(String userName, String password);
    UserDTO logoutUser(String userName);
    void checkIfUserIsAlreadyLogged(UserDTO userDTO);
    String getAndSaveToken(UserDTO userDTO);
    void addActiveUserToken(UserDTO userDTO, String token);
    void deleteAllUsersTokens();
}

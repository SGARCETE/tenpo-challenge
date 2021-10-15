package com.tenpo.challenge.services;

import com.tenpo.challenge.dtos.User;

public interface AuthService {
    User loginUser(String userName, String password);
    User logoutUser(String userName);
    void checkIfUserIsAlreadyLogged(User userDTO);
    String getAndSaveToken(User userDTO);
    void addActiveUserToken(User userDTO, String token);
    void deleteAllUsersTokens();
    void checkIfUserExistsAndIsLogged(String username, String token);
}

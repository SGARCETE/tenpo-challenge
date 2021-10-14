package com.tenpo.challenge.services;

import com.tenpo.challenge.dtos.UserDTO;

public interface AuthService {
    UserDTO authUser(String userName, String password);
    String getAndSaveToken(UserDTO userDTO);
    void checkIfUserIsAlreadyLogged(UserDTO userDTO);
    void addActiveUserToken(UserDTO userDTO, String token);
}

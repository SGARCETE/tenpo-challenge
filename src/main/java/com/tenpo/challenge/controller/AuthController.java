package com.tenpo.challenge.controller;

import com.tenpo.challenge.dtos.User;
import com.tenpo.challenge.model.UserDto;
import com.tenpo.challenge.resources.AuthResource;
import com.tenpo.challenge.services.AuthService;
import com.tenpo.challenge.util.MappingHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @PostMapping("login")
    public ResponseEntity<AuthResource> loginUser(@Valid @RequestBody UserDto user) {
        User userDTO = MappingHelper.map(user, User.class);
        User response = authService.loginUser(userDTO.getUserName(), userDTO.getPassword());
        authService.checkIfUserIsAlreadyLogged(userDTO);
        return new ResponseEntity(new AuthResource(response.getId(), authService.getAndSaveToken(userDTO)), HttpStatus.OK);
    }

    @PostMapping("logout")
    public ResponseEntity<AuthResource> logoutUser(@Valid @RequestBody UserDto user) {
        User userDTO = MappingHelper.map(user, User.class);
        User response = authService.loginUser(userDTO.getUserName(), userDTO.getPassword());
        authService.checkIfUserIsAlreadyLogged(userDTO);
        return new ResponseEntity(new AuthResource(response.getId(), authService.getAndSaveToken(userDTO)), HttpStatus.OK);
    }

}

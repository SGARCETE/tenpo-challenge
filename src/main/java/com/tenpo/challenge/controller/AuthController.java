package com.tenpo.challenge.controller;

import com.tenpo.challenge.dtos.User;
import com.tenpo.challenge.model.UserDto;
import com.tenpo.challenge.resources.AuthLoginResource;
import com.tenpo.challenge.resources.AuthLogoutResource;
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
    public ResponseEntity<AuthLoginResource> loginUser(@Valid @RequestBody UserDto user) {
        User userDTO = MappingHelper.map(user, User.class);
        User response = authService.loginUser(userDTO.getUserName(), userDTO.getPassword());
        authService.checkIfUserIsAlreadyLogged(userDTO);
        return new ResponseEntity(new AuthLoginResource(response.getId(), authService.getAndSaveToken(userDTO)), HttpStatus.OK);
    }

    @PostMapping("logout")
    public ResponseEntity<AuthLoginResource> logoutUser(@Valid @RequestBody UserDto user) {
        User userDTO = MappingHelper.map(user, User.class);
        User response = authService.logoutUser(userDTO.getUserName());
        return new ResponseEntity(new AuthLogoutResource(response.getId()), HttpStatus.OK);
    }

}

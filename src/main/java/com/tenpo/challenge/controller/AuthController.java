package com.tenpo.challenge.controller;

import com.tenpo.challenge.dtos.UserDTO;
import com.tenpo.challenge.model.User;
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
@RequestMapping("/login")
public class AuthController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @PostMapping
    public ResponseEntity<AuthResource> loginUser(@Valid @RequestBody User user) {
        UserDTO userDTO = MappingHelper.map(user, UserDTO.class);
        UserDTO response = authService.authUser(userDTO.getUserName(), userDTO.getPassword());
        LOGGER.info(String.format("user %s logged succesfully", response.getUserName()));
        return new ResponseEntity(new AuthResource(response.getId(), authService.getToken(user.getUserName())), HttpStatus.OK);
    }
}

package com.tenpo.challenge.controller;

import com.tenpo.challenge.dtos.User;
import com.tenpo.challenge.model.UserDto;
import com.tenpo.challenge.resources.UserResource;
import com.tenpo.challenge.services.UsersService;
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
@RequestMapping("/users")
public class UsersController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UsersController.class);

    @Autowired
    private UsersService usersService;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto user) {
        User userDTO = MappingHelper.map(user, User.class);
        User response = usersService.createUser(userDTO);
        LOGGER.debug(String.format("User with id %s created succesfully", response.getId()));
        return new ResponseEntity(new UserResource(response.getId()), HttpStatus.OK);
    }
}

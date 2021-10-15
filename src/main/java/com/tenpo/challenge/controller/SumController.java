package com.tenpo.challenge.controller;

import com.tenpo.challenge.dtos.SumDto;
import com.tenpo.challenge.resources.SumResource;
import com.tenpo.challenge.services.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/sum")
public class SumController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SumController.class);

    @Autowired
    private AuthService authService;

    @PostMapping()
    public ResponseEntity<SumResource> sum(HttpServletRequest request, @Valid @RequestBody SumDto sumDto) {
        authService.checkIfUserExistsAndIsLogged(sumDto.getUserName(), request.getHeader("Authorization"));
        return new ResponseEntity(new SumResource(sumDto.getFirstNumber() + sumDto.getSecondNumber()), HttpStatus.OK);
    }

}

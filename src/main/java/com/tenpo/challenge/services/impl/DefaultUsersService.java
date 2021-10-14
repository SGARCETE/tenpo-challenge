package com.tenpo.challenge.services.impl;

import com.tenpo.challenge.dtos.User;
import com.tenpo.challenge.exceptions.UserAlreadyExistsException;
import com.tenpo.challenge.repository.UsersRepository;
import com.tenpo.challenge.services.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultUsersService implements UsersService {
    @Autowired
    private final PasswordEncoder encoder;

    @Autowired
    private final UsersRepository usersRepository;

    public User createUser(User user) {
        if (usersRepository.findByUserName(user.getUserName()).isPresent())
            throw new UserAlreadyExistsException(String.format("The user with name %s already exists", user.getUserName()));
        user.setPassword(encoder.encode(user.getPassword()));
        return usersRepository.save(user);
    }

}

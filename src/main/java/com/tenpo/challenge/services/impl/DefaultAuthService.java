package com.tenpo.challenge.services.impl;

import com.tenpo.challenge.model.User;
import com.tenpo.challenge.exceptions.*;
import com.tenpo.challenge.repository.UsersRepository;
import com.tenpo.challenge.services.AuthService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefaultAuthService implements AuthService {
    @Autowired
    private final PasswordEncoder encoder;

    @Autowired
    private final UsersRepository usersRepository;

    private static Map<Long, String> activeUsersTokens = new HashMap<>();

    public User loginUser(String userName, String password) {
        User user = usersRepository.findByUserName(userName).orElseThrow(() ->
                new UserNotFoundException(String.format("The user with name %s does not exists", userName)));

        checkIfUserIsAlreadyLogged(user);

        if (encoder.matches(password, user.getPassword())) {
            return user;
        } else {
            throw new PasswordNotValidException(String.format("Password not valid for user %s", userName));
        }
    }

    public User logoutUser(String userName) {
        User user = usersRepository.findByUserName(userName).orElseThrow(() ->
                new UserNotFoundException(String.format("The user with name %s does not exists", userName)));
        deleteActiveUserToken(user);
        return user;
    }

    public void checkIfUserExistsAndIsLogged(String username, String token) {
        User user = usersRepository.findByUserName(username).orElseThrow(() ->
                new UserNotFoundException(String.format("The user with name %s does not exists", username)));
        checkIfUserTokenIsValid(user, token);
    }


    //todo: token logic should be in another service.

    public String getAndSaveToken(User userDTO) {
        String secretKey = "mySecretKey";
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_USER");

        String token = "Bearer " + Jwts
                .builder()
                .setId("ChallengeJWT")
                .setSubject(userDTO.getUserName())
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 600000))
                .signWith(SignatureAlgorithm.HS512,
                        secretKey.getBytes()).compact();

        deleteAllUsersTokens();
        addActiveUserToken(userDTO, token);
        return token;
    }

    public void checkIfUserIsAlreadyLogged(User user) {
        if(activeUsersTokens.containsKey(user.getId()))
            throw new UserAlreadyLoggedException(String.format("The user with name %s is already logged", user.getUserName()));
    }

    public void checkIfUserTokenIsValid(User user, String token) {
        if(!activeUsersTokens.values().contains(token) || !activeUsersTokens.containsKey(user.getId()) || !activeUsersTokens.get(user.getId()).equals(token))
            throw new TokenNotValidException(String.format("The current token is not valid por user %s", user.getUserName()));
    }

    public void addActiveUserToken(User user, String token) {
        this.activeUsersTokens.put(user.getId(), token);
    }

    public void deleteActiveUserToken(User user) {
        if(activeUsersTokens.containsKey(user.getId())) {
            this.activeUsersTokens.remove(user.getId());
        } else {
            throw new UserNotLoggedException(String.format("The user with name %s is not logged", user.getUserName()));
        }
    }

    public void deleteAllUsersTokens() {
        this.activeUsersTokens.clear();
    }

}

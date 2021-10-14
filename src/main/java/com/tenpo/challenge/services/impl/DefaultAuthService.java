package com.tenpo.challenge.services.impl;

import com.tenpo.challenge.dtos.UserDTO;
import com.tenpo.challenge.exceptions.PasswordNotValidException;
import com.tenpo.challenge.exceptions.UserAlreadyLoggedException;
import com.tenpo.challenge.exceptions.UserNotFoundException;
import com.tenpo.challenge.exceptions.UserNotLoggedException;
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

    public UserDTO loginUser(String userName, String password) {
        UserDTO user = usersRepository.findByUserName(userName).orElseThrow(() ->
                new UserNotFoundException(String.format("The user with name %s does not exists", userName)));

        checkIfUserIsAlreadyLogged(user);

        if (encoder.matches(password, user.getPassword())) {
            return user;
        } else {
            throw new PasswordNotValidException(String.format("Password not valid for user %s", userName));
        }
    }

    public UserDTO logoutUser(String userName) {
        UserDTO user = usersRepository.findByUserName(userName).orElseThrow(() ->
                new UserNotFoundException(String.format("The user with name %s does not exists", userName)));
        deleteActiveUserToken(user);
        return user;
    }


    //todo: token logic should be in another service.

    public String getAndSaveToken(UserDTO userDTO) {
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

        addActiveUserToken(userDTO, token);
        return token;
    }

    public void checkIfUserIsAlreadyLogged(UserDTO userDTO) {
        if(activeUsersTokens.containsKey(userDTO.getId()))
            throw new UserAlreadyLoggedException(String.format("The user with name %s is already logged", userDTO.getUserName()));
    }

    public void addActiveUserToken(UserDTO userDTO, String token) {
        this.activeUsersTokens.put(userDTO.getId(), token);
    }

    public void deleteActiveUserToken(UserDTO userDTO) {
        if(activeUsersTokens.containsKey(userDTO.getId())) {
            this.activeUsersTokens.remove(userDTO.getId());
        } else {
            throw new UserNotLoggedException(String.format("The user with name %s is not logged", userDTO.getUserName()));
        }
    }

    public void deleteAllUsersTokens() {
        this.activeUsersTokens.clear();
    }

}

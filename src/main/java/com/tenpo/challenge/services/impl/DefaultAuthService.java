package com.tenpo.challenge.services.impl;

import com.tenpo.challenge.dtos.UserDTO;
import com.tenpo.challenge.exceptions.PasswordNotValidException;
import com.tenpo.challenge.exceptions.UserNotFoundException;
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
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefaultAuthService implements AuthService {
    @Autowired
    private final PasswordEncoder encoder;

    @Autowired
    private final UsersRepository usersRepository;

    public UserDTO authUser(String userName, String password) {
        UserDTO user = usersRepository.findByUserName(userName).orElseThrow(() ->
                new UserNotFoundException(String.format("The user with name %s does not exists", userName)));

        if (encoder.matches(password, user.getPassword())) {
            return user;
        } else {
            throw new PasswordNotValidException(String.format("Password not valid for user %s", userName));
        }
    }

    public String getToken(String userName) {
        String secretKey = "mySecretKey";
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_USER");

        String token = Jwts
                .builder()
                .setId("ChallengeJWT")
                .setSubject(userName)
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 600000))
                .signWith(SignatureAlgorithm.HS512,
                        secretKey.getBytes()).compact();

        return "Bearer " + token;
    }

}

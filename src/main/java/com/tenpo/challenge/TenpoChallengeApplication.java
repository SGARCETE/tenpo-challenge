package com.tenpo.challenge;

import com.tenpo.challenge.filter.TokenValidatorFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

@SpringBootApplication
public class TenpoChallengeApplication {

    @Autowired
    private Environment environment;

    public static void main(String[] args) {
        SpringApplication.run(TenpoChallengeApplication.class, args);
    }

    @EnableWebSecurity
    @Configuration
    class WebSecurityConfig extends WebSecurityConfigurerAdapter {
        //TODO: Security configs should not be disabled in tests and this conditional should not be here
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            if(Arrays.stream(environment.getActiveProfiles()).anyMatch(
                    env -> (env.equalsIgnoreCase("test")))) {
                http.csrf().disable()
                        .addFilterAfter(new TokenValidatorFilter(), UsernamePasswordAuthenticationFilter.class)
                        .authorizeRequests()
                        .antMatchers(HttpMethod.POST, "/users").permitAll()
                        .antMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .antMatchers(HttpMethod.POST, "/auth/logout").permitAll()
                        .anyRequest().authenticated();
            } else {
                http.csrf().disable()
                        .addFilterAfter(new TokenValidatorFilter(), UsernamePasswordAuthenticationFilter.class)
                        .authorizeRequests()
                        .antMatchers(HttpMethod.POST, "/users").permitAll()
                        .antMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .antMatchers(HttpMethod.POST, "/auth/logout").permitAll()
                        .anyRequest().authenticated();
            }
        }
    }

}

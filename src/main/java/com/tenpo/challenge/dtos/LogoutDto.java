package com.tenpo.challenge.dtos;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode
public class LogoutDto {
    @NotEmpty(message = "Please provide a user_name attribute in JSON request")
    private String userName;
}

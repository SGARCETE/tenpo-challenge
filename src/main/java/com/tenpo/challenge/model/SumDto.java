package com.tenpo.challenge.model;

import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode
public class SumDto {
    @NotEmpty(message = "Please provide a user_name attribute in JSON request")
    private String userName;
    @Positive(message = "Please provide a positive first_number attribute in JSON request")
    @NotNull(message = "Please provide a first_number attribute in JSON request")
    private Double firstNumber;
    @NotNull(message = "Please provide a second_number attribute in JSON request")
    @Positive(message = "Please provide a positive second_number in JSON request")
    private Double secondNumber;
}

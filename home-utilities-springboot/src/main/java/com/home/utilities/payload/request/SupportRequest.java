package com.home.utilities.payload.request;

import com.home.utilities.validator.email.ValidateEmail;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class SupportRequest {

    @ValidateEmail
    @NotBlank(message = "{message.field.empty}")
    private String email;
}

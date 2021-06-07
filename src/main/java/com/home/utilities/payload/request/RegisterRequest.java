package com.home.utilities.payload.request;

import com.home.utilities.validators.email.ValidateEmail;
import com.home.utilities.validators.password.RepeatPassword;
import com.home.utilities.validators.password.ValidatePassword;
import com.home.utilities.entities.Gender;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@RepeatPassword(field = "password", fieldMatch = "repeatPassword")
public class RegisterRequest {

    @ValidateEmail
    @NotBlank(message = "{message.field.empty}")
    private String email;

    @ValidatePassword
    @NotBlank(message = "{message.field.empty}")
    private String password;

    @NotBlank(message = "{message.field.empty}")
    private String repeatPassword;

    @NotBlank(message = "{message.field.empty}")
    @Size(max = 30, message = "{size.registerRequest.firstName}")
    private String firstName;

    @NotBlank(message = "{message.field.empty}")
    @Size(max = 30, message = "{size.registerRequest.lastName}")
    private String lastName;

    @NotNull(message = "{message.field.empty}")
    private Gender gender;

    @AssertTrue(message = "{message.agree.empty}")
    private Boolean terms;

    @AssertTrue(message = "{message.agree.empty}")
    private Boolean gdpr;
}

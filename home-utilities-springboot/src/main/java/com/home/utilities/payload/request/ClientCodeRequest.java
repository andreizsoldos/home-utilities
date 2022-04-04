package com.home.utilities.payload.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ClientCodeRequest {

    @NotBlank(message = "{message.field.empty}")
    private String clientNumber;

    @NotBlank(message = "{message.field.empty}")
    private String clientName;

    @NotBlank(message = "{message.field.empty}")
    private String consumptionLocationNumber;

    @NotBlank(message = "{message.field.empty}")
    private String consumptionAddress;

    @NotNull(message = "{message.field.empty}")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate contractDate;
}

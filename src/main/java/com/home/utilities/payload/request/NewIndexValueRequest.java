package com.home.utilities.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewIndexValueRequest {

    @NotNull(message = "{message.field.empty}")
    @PositiveOrZero(message = "{message.field.positive}")
    private Double newValue;
}

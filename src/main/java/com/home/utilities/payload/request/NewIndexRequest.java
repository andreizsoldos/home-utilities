package com.home.utilities.payload.request;

import com.home.utilities.validators.index.NewValueGreaterThanOldValue;
import com.home.utilities.validators.index.OncePerDay;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
@OncePerDay(field = "value", fieldMatch = "lastIndex")
@NewValueGreaterThanOldValue(field = "value", fieldMatch = "lastIndex")
public class NewIndexRequest {

    @NotNull(message = "{message.index.illegal.value}")
    @PositiveOrZero(message = "{message.field.positive}")
    private Double value;

    private Double lastIndex;

    public NewIndexRequest() {
    }

    public NewIndexRequest(final Double lastIndex) {
        this.lastIndex = lastIndex;
    }
}

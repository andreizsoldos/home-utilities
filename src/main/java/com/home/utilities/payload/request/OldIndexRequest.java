package com.home.utilities.payload.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.home.utilities.validators.index.NewValueGreaterThanOldValue;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
@NewValueGreaterThanOldValue(field = "value", fieldMatch = "lastIndex")
public class OldIndexRequest {

    @JsonDeserialize(using = IndexRequestDeserializer.class)
    @NotNull(message = "{message.index.illegal.value}")
    @PositiveOrZero(message = "{message.field.positive}")
    private Double value;

    private Double lastIndex;

    public OldIndexRequest() {
    }

    public OldIndexRequest(final Double lastIndex) {
        this.lastIndex = lastIndex;
    }
}

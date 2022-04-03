package com.home.utilities.payload.request;

import com.home.utilities.entities.Branch;
import com.home.utilities.validators.index.NewValueGreaterThanOldValue;
import com.home.utilities.validators.index.OncePerDay;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
@OncePerDay(field = "value", fieldMatch = "lastIndex", fieldMatchClientId = "clientIdValue", fieldMatchBranch = "branchValue")
@NewValueGreaterThanOldValue(field = "value", fieldMatch = "lastIndex")
public class NewIndexRequest {

    @NotNull(message = "{message.index.illegal.value}")
    @PositiveOrZero(message = "{message.field.positive}")
    private Double value;

    private Double lastIndex;
    private Long clientIdValue;
    private Branch branchValue;

    public NewIndexRequest() {
    }

    public NewIndexRequest(final Double lastIndex, final Long clientIdValue, final Branch branchValue) {
        this.lastIndex = lastIndex;
        this.clientIdValue = clientIdValue;
        this.branchValue = branchValue;
    }
}

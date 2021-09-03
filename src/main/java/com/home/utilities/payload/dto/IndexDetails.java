package com.home.utilities.payload.dto;

import com.home.utilities.services.util.DateTimeConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IndexDetails {

    private static final String CHAR = "&nbsp"; // or use -

    private Long id;
    private Long clientId;
    private Double value;
    private Instant createdAt;
    private Instant modifiedAt;

    public String getOldIndex(final List<OldIndexDetails> oldIndexesList) {
        return oldIndexesList.stream()
              .filter(i -> i.getIndexId().equals(this.id))
              .map(i -> DateTimeConverter.toTime(i.getModifiedAt()) + " --> " + i.getValue())
              .collect(Collectors.joining("<br>"));
    }

    public String insertTooltipTitle(final String leftTitle, final String rightTitle, final List<OldIndexDetails> oldIndexesList) {
        final var leftChar = addDelimiter(Math.round((8f - leftTitle.length()) / 2f));
        final var rightChar = addDelimiter((maxListLength(oldIndexesList) - 13 - rightTitle.length() - 1) < 0 ? 3 : maxListLength(oldIndexesList) - 13 - rightTitle.length() - 1);
        final var middleChar = addDelimiter((maxListLength(oldIndexesList) - countDelimiter(leftChar) - leftTitle.length() - countDelimiter(rightChar)) < 0 ? 6 : maxListLength(oldIndexesList) - countDelimiter(leftChar) - leftTitle.length() - countDelimiter(rightChar));

        return leftChar + leftTitle + middleChar + rightTitle + rightChar;
    }

    private Integer maxListLength(final List<OldIndexDetails> oldIndexesList) {
        return oldIndexesList.stream()
              .filter(i -> i.getIndexId().equals(this.id))
              .map(i -> DateTimeConverter.toTime(i.getModifiedAt()) + " --> " + i.getValue())
              .map(String::length)
              .reduce(0, Integer::max);
    }

    private String addDelimiter(final Integer length) {
        return CHAR.repeat(length);
    }

    private Integer countDelimiter(final String delimiter) {
        return delimiter.replace(CHAR, " ").length();
    }
}

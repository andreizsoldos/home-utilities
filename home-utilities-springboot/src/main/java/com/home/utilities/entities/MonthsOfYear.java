package com.home.utilities.entities;

public enum MonthsOfYear {
    JANUARY("chart.year.january.short.label"),
    FEBRUARY("chart.year.february.short.label"),
    MARCH("chart.year.march.short.label"),
    APRIL("chart.year.april.short.label"),
    MAY("chart.year.may.short.label"),
    JUNE("chart.year.june.short.label"),
    JULY("chart.year.july.short.label"),
    AUGUST("chart.year.august.short.label"),
    SEPTEMBER("chart.year.september.short.label"),
    OCTOBER("chart.year.october.short.label"),
    NOVEMBER("chart.year.november.short.label"),
    DECEMBER("chart.year.december.short.label");

    private final String description;

    private MonthsOfYear(final String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }
}

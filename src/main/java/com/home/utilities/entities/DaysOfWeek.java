package com.home.utilities.entities;

public enum DaysOfWeek {
    MONDAY("chart.week.monday.short.label"),
    TUESDAY("chart.week.tuesday.short.label"),
    WEDNESDAY("chart.week.wednesday.short.label"),
    THURSDAY("chart.week.thursday.short.label"),
    FRIDAY("chart.week.friday.short.label"),
    SATURDAY("chart.week.saturday.short.label"),
    SUNDAY("chart.week.sunday.short.label");

    private final String description;

    private DaysOfWeek(final String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }
}

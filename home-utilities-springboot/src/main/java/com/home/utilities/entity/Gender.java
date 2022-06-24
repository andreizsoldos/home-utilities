package com.home.utilities.entity;

public enum Gender {
    MALE("register.gender.male.label"),
    FEMALE("register.gender.female.label"),
    OTHER("register.gender.other.label");

    private final String description;

    private Gender(final String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }
}

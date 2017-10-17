package com.kushki.enums;

public enum KushkiPeriodicitySubscriptionType {
    DAILY("daily"),
    WEEKLY("weekly"),
    BIWEEKLY("biweekly"),
    MONTLY("monthly"),
    QUATTERLY("quarterly"),
    HALFYEARLY("halfYearly"),
    YEARLY("yearly"),
    CUSTOM("custom");

    private String name;

    @Override
    public String toString(){
        return name;
    }

    KushkiPeriodicitySubscriptionType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

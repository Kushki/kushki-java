package com.kushki.Enum;

public enum KushkiPeriodicitySuscriptionEnum {
    DAILY("daily"),
    WEEKLY("weekly"),
    BIWEEKLY("biweekly"),
    MONTLY("monthly"),
    QUATTERLY("quarterly"),
    HALFYEARLY("halfYearly"),
    YEARLY("yearly"),
    CUSTOM("custom");

    private String name;

    public String toString(){
        return name;
    }

    KushkiPeriodicitySuscriptionEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

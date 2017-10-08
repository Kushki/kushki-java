package com.kushki.Enum;

public enum KushkiAdjustSuscriptionEnum {
    CHARGE("Charge"),
    DISCOUNT("discount");
    private String name;

    public String toString(){
        return name;
    }

    KushkiAdjustSuscriptionEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

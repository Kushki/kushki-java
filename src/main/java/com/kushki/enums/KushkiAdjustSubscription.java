package com.kushki.enums;

public enum KushkiAdjustSubscription {
    CHARGE("Charge"),
    DISCOUNT("discount");
    private String name;

    @Override
    public String toString(){
        return name;
    }

    KushkiAdjustSubscription(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

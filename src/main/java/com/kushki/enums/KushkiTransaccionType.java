package com.kushki.enums;

public enum KushkiTransaccionType {
    VOID("/charges/"),
    REFUND("/refund/"),
    CHARGE("/charges"),
    SUSCRIPTION("/subscriptions"),
    SUSCRIPTION_CARD("/card"),
    SUSCRIPTION_ADJUSTMENT("/adjustment");

    private String url;

    @Override
    public String toString(){
        return url;
    }

    KushkiTransaccionType(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}

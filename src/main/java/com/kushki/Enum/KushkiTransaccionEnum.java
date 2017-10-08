package com.kushki.Enum;

public enum KushkiTransaccionEnum {
    VOID("/charges/"),
    REFUND("/refund/"),
    CHARGE("/charges"),
    SUSCRIPTION("/subscriptions"),
    SUSCRIPTION_CARD("/card"),
    SUSCRIPTION_ADJUSTMENT("/adjustment");

    private String url;

    public String toString(){
        return url;
    }

    KushkiTransaccionEnum(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}

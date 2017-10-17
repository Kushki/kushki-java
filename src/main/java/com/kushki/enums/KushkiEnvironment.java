package com.kushki.enums;

public enum KushkiEnvironment {


    TESTING("https://api-uat.kushkipagos.com/v1"),
    STAGING("https://api-stg.kushkipagos.com/v1"),
    PRODUCTION("https://api.kushkipagos.com/v1");

    private String url;

    KushkiEnvironment(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}

package com.kushki.enums;

public enum KushkiEnvironment {
    TESTING("https://regional-uat.kushkipagos.com/v1"),
    STAGING("https://regional-stg.kushkipagos.com/v1"),
    PRODUCTION("https://regional.kushkipagos.com/v1");

    private String url;

    KushkiEnvironment(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}

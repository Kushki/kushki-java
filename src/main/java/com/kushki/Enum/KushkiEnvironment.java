package com.kushki.Enum;

public enum KushkiEnvironment {
    WRAPPER_TESTING("https://uat.aurusinc.com/kushki/api/v1"),
    WRAPPER_STAGING("https://staging.aurusinc.com/kushki/api/v1"),
    WRAPPER_PRODUCTION("https://p1.kushkipagos.com/kushki/api/v1"),

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

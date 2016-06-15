package com.kushkipagos;

/**
 * Created by lmunda on 14/06/16 19:13.
 */
public enum KushkiEnvironment {
    TESTING("https://uat.aurusinc.com/kushki/api/v1"),
    STAGING("https://staging.aurusinc.com/kushki/api/v1"),
    PRODUCTION("https://p1.kushkipagos.com/kushki/api/v1");

    private String url;

    KushkiEnvironment(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}

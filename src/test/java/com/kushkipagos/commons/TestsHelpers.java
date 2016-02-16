package com.kushkipagos.commons;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomUtils.nextInt;

/**
 * Created by lmunda on 12/16/15 10:22.
 */
public final class TestsHelpers {

    private TestsHelpers() {

    }

    public static Double getRandomDouble(Double min, Double max) {
        Random r = new Random();
        return min + (max - min) * r.nextDouble();
    }

    public static Integer getRandomInteger(Integer min, Integer max) {
        return nextInt(min, max + 1);
    }

    public static Double getRandomAmount(boolean valid) {
        double[] validCents = {0.0, 0.08, 0.11, 0.59, 0.6};
        double[] invalidCents = {0.05, 0.1, 0.21, 0.61, 0.62, 0.63};

        double cents;
        if (valid) {
            int centPosition = nextInt(0, validCents.length - 1);
            cents = validCents[centPosition];
        } else {
            int centPosition = nextInt(0, invalidCents.length - 1);
            cents = invalidCents[centPosition];
        }
        return nextInt(1, 9999) + cents;
    }

    public static Double getRandomAmount() {
        return getRandomAmount(true);
    }

    public static Integer getRandomMonths() {
        return nextInt(1, 13);
    }

    public static Double getRandomInterest() {
        return getRandomDouble(0.01, 0.99);
    }

    public static Map<String, String> getValidCardData() {
        Map<String, String> cardParams = new HashMap<>(5);
        cardParams.put("name", randomAlphabetic(20));
        cardParams.put("number", "4111111111111111");
        cardParams.put("expiry_month", "12");
        cardParams.put("expiry_year", "20");
        cardParams.put("cvv", "123");
        return cardParams;
    }
}

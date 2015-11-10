package org.kushki;

import org.junit.Before;
import org.junit.Test;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomUtils.nextDouble;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class KushkiIntegrationTest {
    private Kushki kushki;
    private static String currency = "USD";

    @Before
    public void setUp() throws Exception {
        String merchantId = randomAlphabetic(10);
        String language = "es";
        kushki = new Kushki(merchantId, language, currency);
    }

    @Test
    public void shouldReturnASuccessfulTransaction() {
        String token = randomAlphabetic(10);
        String amount = String.valueOf(nextDouble(1, 100));
        Transaction transaction = kushki.charge(token, amount);
        assertThat(transaction.isSuccessful(), is(true));
    }

    @Test
    public void shouldReturnANonSuccessfulTransaction() {
        String token = "123456789-declined";
        String amount = String.valueOf(nextDouble(1, 100));
        Transaction transaction = kushki.charge(token, amount);
        assertThat(transaction.isSuccessful(), is(false));
    }
}

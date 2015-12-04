package com.kushkipagos;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Before;
import org.junit.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.apache.commons.lang3.RandomUtils.nextDouble;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class KushkiIntegrationTest {
    private Kushki kushki;
    private static String currency = "USD";

    @Before
    public void setUp() throws Exception {
        String merchantId = "10000000123454545454546546";
        String language = "es";
        kushki = new Kushki(merchantId, language, currency);
    }

    @Test
    public void shouldReturnASuccessfulTransaction() throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
//        String amount = String.valueOf(nextDouble(1, 100));
//        String amount = String.valueOf(5.0000);
        String amount = "4758493.3434";
        Logger log = Logger.getLogger(Encryption.class.getName());
        log.log(Level.WARNING, amount);
        String token = "s25s784a87ad497af797a48sdg7rhy4d";
        Transaction transaction = kushki.charge(token, amount);
        log.log(Level.INFO, transaction.getResponseBody().toString());
        assertThat(transaction.isSuccessful(), is(true));
    }

    @Test
    public void shouldReturnANonSuccessfulTransaction() throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        String token = "123456789-declined";
        String amount = String.valueOf(nextDouble(1, 100));
        Transaction transaction = kushki.charge(token, amount);
        assertThat(transaction.isSuccessful(), is(false));
    }
}

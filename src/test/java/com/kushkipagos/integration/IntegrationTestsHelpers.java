package com.kushkipagos.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kushkipagos.Kushki;
import com.kushkipagos.Transaction;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

import static com.kushkipagos.commons.TestsHelpers.getValidCardData;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by lmunda on 1/21/16 16:16.
 */
public final class IntegrationTestsHelpers {

    public final static int THREAD_SLEEP = 600;
    public static final String MERCHANT_ID = "10000001604958481814111215";

    private IntegrationTestsHelpers() {
    }

    public static Kushki setupKushki() throws InvalidKeySpecException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IOException {
        String merchantId = MERCHANT_ID; //TW
        // String merchantId = "10000001408518323354818001"; //TW 7 fails: 006, 007, 026, 025, 009, 014, 024: 006: Transacción rechazada
        // String merchantId = "10000001604958481814111215"; //GMS OK
        // String merchantId = "10000001604093396985111213"; //fybeca OK
        // String merchantId = "10000001605036059475111214"; //ya esta OK
        // String merchantId = "10000001605199764649111216"; //latam
        String language = "es";
        String currency = "USD";
        return new Kushki(merchantId, language, currency);
    }

    public static Transaction getValidTokenTransaction(Kushki kushki) throws BadPaddingException, IllegalBlockSizeException, JsonProcessingException {
        Map<String, String> cardParams = getValidCardData();
        return kushki.requestToken(cardParams);
    }

    public static void assertsTransaction(Transaction transaction, Boolean isSuccessful,
                                          String expectedMessage, String expectedCode) {
        assertThat(transaction.isSuccessful(), is(isSuccessful));
        assertThat(transaction.getResponseText(), is(expectedMessage));
        assertThat(transaction.getResponseCode(), is(expectedCode));
    }

    public static void assertsValidTransaction(Transaction transaction) {
        assertsTransaction(transaction, true, "Transacción aprobada", "000");
    }

}

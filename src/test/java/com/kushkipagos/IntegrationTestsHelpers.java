package com.kushkipagos;

import com.fasterxml.jackson.core.JsonProcessingException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;

import static com.kushkipagos.TestsHelpers.getValidCardData;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by lmunda on 1/21/16 16:16.
 */
public class IntegrationTestsHelpers {

    public static Kushki setupKushki() throws InvalidKeySpecException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IOException {
        String merchantId = "10000001408518323354818001";
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

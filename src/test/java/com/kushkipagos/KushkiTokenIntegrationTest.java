package com.kushkipagos;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Before;
import org.junit.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by lmunda on 1/22/16 09:47.
 */
public class KushkiTokenIntegrationTest {
    private Kushki kushki;
    Map<String, String> cardParams = new HashMap<>(5);

    @Before
    public void setup() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, IOException {
        kushki = IntegrationTestsHelpers.setup();
        cardParams = TestsHelpers.getValidCardData();
    }

    @Test
    public void shouldReturnSuccessfulTokenTransaction_TC_001() throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        Transaction tokenTransaction = kushki.requestToken(cardParams);

        assertsTokenTransaction(tokenTransaction, true, "Transacción aprobada", "000");
    }

    @Test
    public void shouldReturnNonSuccessfulTokenTransactionInvalidCard_TC_002() throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        cardParams.put("number", "5411111111115854");

        Transaction tokenTransaction = kushki.requestToken(cardParams);

        assertsTokenTransaction(tokenTransaction, false, "Tarjeta no válida", "017");
    }

    @Test
    public void shouldReturnNonSuccessfulTokenTransactionInvalidExpiry_TC_003() throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        cardParams.put("expiry_month", "ab");
        cardParams.put("expiry_year", "cd");

        Transaction tokenTransaction = kushki.requestToken(cardParams);

        assertsTokenTransaction(tokenTransaction, false, "Tarjeta no válida", "017");
    }

    @Test
    public void shouldReturnNonSuccessfulTokenTransactionCardExpired_TC_004() throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        cardParams.put("expiry_month", "12");
        cardParams.put("expiry_year", "14");

        Transaction tokenTransaction = kushki.requestToken(cardParams);

        assertsTokenTransaction(tokenTransaction, false, "Tarjeta vencida", "018");
    }

    @Test
    public void shouldReturnNonSuccessfulTokenTransactionInvalidCVV_TC_005() throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        cardParams.put("cvv", "abc");

        Transaction tokenTransaction = kushki.requestToken(cardParams);

        assertsTokenTransaction(tokenTransaction, false, "CVC no válido", "007");
    }

    private void assertsTokenTransaction(Transaction tokenTransaction, Boolean isSuccessful,
                                         String expectedMessage, String expectedCode) {
        assertThat(tokenTransaction.isSuccessful(), is(isSuccessful));
        assertThat(tokenTransaction.getResponseText(), is(expectedMessage));
        assertThat(tokenTransaction.getResponseCode(), is(expectedCode));
    }
}

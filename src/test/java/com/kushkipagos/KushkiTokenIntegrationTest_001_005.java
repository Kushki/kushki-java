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

import static com.kushkipagos.IntegrationTestsHelpers.assertsTransaction;
import static com.kushkipagos.IntegrationTestsHelpers.assertsValidTransaction;
import static com.kushkipagos.IntegrationTestsHelpers.setupKushki;

/**
 * Created by lmunda on 1/22/16 09:47.
 */
public class KushkiTokenIntegrationTest_001_005 {
    private Kushki kushki;
    Map<String, String> cardParams = new HashMap<>(5);
    private Transaction tokenTransaction;

    @Before
    public void setup() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, IOException {
        kushki = setupKushki();
        cardParams = TestsHelpers.getValidCardData();
    }

    @Test
    public void shouldReturnSuccessfulTokenTransaction_TC_001() throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        tokenTransaction = kushki.requestToken(cardParams);

        assertsValidTransaction(tokenTransaction);
    }

    @Test
    public void shouldReturnNonSuccessfulTokenTransactionInvalidCard_TC_002() throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        cardParams.put("number", "5411111111115854");

        tokenTransaction = kushki.requestToken(cardParams);

        assertsTransaction(tokenTransaction, false, "Tarjeta no válida", "017");
    }

    @Test
    public void shouldReturnNonSuccessfulTokenTransactionInvalidExpiry_TC_003() throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        cardParams.put("expiry_month", "ab");
        cardParams.put("expiry_year", "cd");

        tokenTransaction = kushki.requestToken(cardParams);

        assertsTransaction(tokenTransaction, false, "Tarjeta no válida", "017");
    }

    @Test
    public void shouldReturnNonSuccessfulTokenTransactionCardExpired_TC_004() throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        cardParams.put("expiry_month", "12");
        cardParams.put("expiry_year", "14");

        tokenTransaction = kushki.requestToken(cardParams);

        assertsTransaction(tokenTransaction, false, "Tarjeta vencida", "018");
    }

    @Test
    public void shouldReturnNonSuccessfulTokenTransactionInvalidCVV_TC_005() throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        cardParams.put("cvv", "abc");

        tokenTransaction = kushki.requestToken(cardParams);

        assertsTransaction(tokenTransaction, false, "CVC no válido", "007");
    }
}

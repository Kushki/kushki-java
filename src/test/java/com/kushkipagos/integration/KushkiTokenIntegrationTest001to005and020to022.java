package com.kushkipagos.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kushkipagos.Amount;
import com.kushkipagos.AurusTokenService;
import com.kushkipagos.Kushki;
import com.kushkipagos.KushkiEnvironment;
import com.kushkipagos.KushkiException;
import com.kushkipagos.Transaction;
import com.kushkipagos.commons.TestsHelpers;
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

import static com.kushkipagos.integration.IntegrationTestsHelpers.assertsTransaction;
import static com.kushkipagos.integration.IntegrationTestsHelpers.assertsValidTransaction;
import static com.kushkipagos.integration.IntegrationTestsHelpers.setupKushki;
import static com.kushkipagos.integration.TokenHelper.getValidTokenTransaction;

public class KushkiTokenIntegrationTest001to005and020to022 {
    private Kushki kushki;
    private Map<String, String> cardParams = new HashMap<>(5);
    private Transaction tokenTransaction;
    private AurusTokenService aurusTokenService;

    @Before
    public void setUp() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, IOException {
        kushki = setupKushki(false);
        cardParams = TestsHelpers.getValidCardData();
        aurusTokenService = new AurusTokenService();
    }

    @Test
    public void shouldReturnSuccessfulTokenTransactionTC001() throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        tokenTransaction = getValidTokenTransaction(kushki);

        assertsValidTransaction(tokenTransaction);
    }

    @Test
    public void shouldReturnNonSuccessfulTokenTransactionInvalidCardTC002() throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        cardParams.put("number", "5411111111115854");

        tokenTransaction = aurusTokenService.requestToken(kushki, cardParams, TestsHelpers.getRandomAmount());

        assertsTransaction(tokenTransaction, false, "Tarjeta no válida", "017");
    }

    @Test
    public void shouldReturnNonSuccessfulTokenTransactionInvalidExpiryTC003() throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        cardParams.put("expiry_month", "ab");
        cardParams.put("expiry_year", "cd");

        tokenTransaction = aurusTokenService.requestToken(kushki, cardParams, TestsHelpers.getRandomAmount());

        assertsTransaction(tokenTransaction, false, "Tarjeta no válida", "017");
    }

    @Test
    public void shouldReturnNonSuccessfulTokenTransactionCardExpiredTC004() throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        cardParams.put("expiry_month", "12");
        cardParams.put("expiry_year", "14");

        tokenTransaction = aurusTokenService.requestToken(kushki, cardParams, TestsHelpers.getRandomAmount());

        assertsTransaction(tokenTransaction, false, "Tarjeta vencida", "018");
    }

    @Test
    public void shouldReturnNonSuccessfulTokenTransactionInvalidCVVTC005() throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        cardParams.put("cvv", "abc");

        tokenTransaction = aurusTokenService.requestToken(kushki, cardParams, TestsHelpers.getRandomAmount());

        assertsTransaction(tokenTransaction, false, "CVC no válido", "007");
    }

    @Test
    public void shouldReturnFailedTokenTransactionInvalidMerchantIdTC020() throws IOException, BadPaddingException, IllegalBlockSizeException, KushkiException, InvalidKeySpecException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
        String merchantId = "abc";
        String language = "es";
        String currency = "USD";
        kushki = new Kushki(merchantId, language, currency, KushkiEnvironment.TESTING);

        Transaction tokenTransaction = getValidTokenTransaction(kushki);

        assertsTransaction(tokenTransaction, false, "ID de comercio no válido", "201");
    }

    @Test
    public void shouldReturnFailedTokenTransactionInvalidLanguageTC021() throws IOException, BadPaddingException, IllegalBlockSizeException, KushkiException, InvalidKeySpecException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
        String merchantId = "10000001408518323354818001";
        String language = "xyz";
        String currency = "USD";
        kushki = new Kushki(merchantId, language, currency, KushkiEnvironment.TESTING);

        Transaction tokenTransaction = getValidTokenTransaction(kushki);

        assertsTransaction(tokenTransaction, false, "Indicador de idioma no válido", "277");
    }
}

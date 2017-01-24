package com.kushkipagos.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kushkipagos.*;
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

import static com.kushkipagos.integration.IntegrationTestsHelpers.*;
import static com.kushkipagos.integration.TokenHelper.getValidTokenTransaction;
import static com.kushkipagos.integration.TokenHelper.getValidTokenTransactionColombia;

public class KushkiChargeIntegrationTest006to008and023 {
    private Kushki kushki;
    private Kushki secretKushki;
    private Kushki secretKushkiColombia;

    private Transaction tokenTransaction;
    private Transaction tokenTransactionColombia;
    private Transaction chargeTransaction;
    private Transaction chargeTransactionColombia;
    private Amount amount;
    private Amount amountColombia;
    private String token;
    private String tokenColombia;

    @Before
    public void setUp() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, IOException, BadPaddingException, IllegalBlockSizeException, KushkiException, InterruptedException {
        kushki = setupKushki(false);
        Kushki kushkiColombia = setupKushkiColombia(false);
        secretKushki = setupKushki(true);
        secretKushkiColombia = setupKushkiColombia(true);

        amount = TestsHelpers.getRandomAmount();
        amountColombia = TestsHelpers.getRandomAmountColombia();
        tokenTransaction = getValidTokenTransaction(kushki, amount.getTotalAmount());
        tokenTransactionColombia = getValidTokenTransactionColombia(kushkiColombia, amountColombia.getTotalAmount());
        token = tokenTransaction.getToken();
        tokenColombia = tokenTransactionColombia.getToken();
        chargeTransaction = secretKushki.charge(token, amount);
        chargeTransactionColombia = secretKushkiColombia.charge(tokenColombia, amountColombia);
    }

    @Test
    public void shouldReturnSuccessfulChargeTransactionTC006() throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        assertsValidTransaction(tokenTransaction);
        assertsValidTransaction(chargeTransaction);
    }
    // TODO: Uncomment this when Colombian charge starts working
//    @Test
//    public void shouldReturnSuccessfulChargeTransactionTC006Colombia() throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
//        assertsValidTransaction(tokenTransactionColombia);
//        assertsValidTransaction(chargeTransactionColombia);
//    }

    @Test
    public void shouldReturnNonSuccessfulChargeTransactionUsedTokenTC007() throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException, InterruptedException {
        Thread.sleep(IntegrationTestsHelpers.THREAD_SLEEP);
        Transaction secondChargeTransaction = secretKushki.charge(token, amount);

        assertsValidTransaction(tokenTransaction);
        assertsValidTransaction(chargeTransaction);
        assertsTransaction(secondChargeTransaction, false, "El token de la transacción ha expirado", "578");
    }

    @Test
    public void shouldReturnNonSuccessfulChargeTransactionInvalidTokenTC008() throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException, InterruptedException {
        String token = "k7jwynu59sd28wu81i2ygsyvllyfimju";

        Thread.sleep(IntegrationTestsHelpers.THREAD_SLEEP);
        Transaction chargeTransaction = secretKushkiColombia.charge(token, amountColombia);

        assertsTransaction(chargeTransaction, false, "ID de comercio no válido", "201");
    }

    @Test
    public void shouldReturnFailedChargeTransactionInvalidCurrencyTC023() throws IOException, BadPaddingException, IllegalBlockSizeException, KushkiException, InvalidKeySpecException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InterruptedException {
        String merchantId = IntegrationTestsHelpers.MERCHANT_ID;
        String language = "es";
        String currency = "USD";
        String invalidCurrency = "XYZ";
        Kushki tokenKushki = new Kushki(merchantId, language, currency, KushkiEnvironment.TESTING);
        Thread.sleep(IntegrationTestsHelpers.THREAD_SLEEP);
        Transaction tokenTransaction = getValidTokenTransaction(tokenKushki, amount.getTotalAmount());
        String token = tokenTransaction.getToken();

        kushki = new Kushki(merchantId, language, invalidCurrency, KushkiEnvironment.TESTING);
        Transaction chargeTransaction = kushki.charge(token, amount);

        assertsValidTransaction(tokenTransaction);
        assertsTransaction(chargeTransaction, false, "Tipo de moneda no válida", "205");
    }
}

package com.kushkipagos.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kushkipagos.Kushki;
import com.kushkipagos.KushkiException;
import com.kushkipagos.commons.TestsHelpers;
import com.kushkipagos.Transaction;
import org.junit.Before;
import org.junit.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static com.kushkipagos.integration.IntegrationTestsHelpers.assertsTransaction;
import static com.kushkipagos.integration.IntegrationTestsHelpers.assertsValidTransaction;
import static com.kushkipagos.integration.IntegrationTestsHelpers.getValidTokenTransaction;
import static com.kushkipagos.integration.IntegrationTestsHelpers.setupKushki;

/**
 * Created by lmunda on 1/22/16 10:16.
 */
public class KushkiChargeIntegrationTest006to008and023 {
    private Kushki kushki;

    Transaction tokenTransaction;
    Transaction chargeTransaction;
    private Double amount;
    private String token;

    @Before
    public void setUp() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, IOException, BadPaddingException, IllegalBlockSizeException, KushkiException, InterruptedException {
        kushki = setupKushki();

        tokenTransaction = getValidTokenTransaction(kushki);
        amount = TestsHelpers.getRandomAmount();
        token = tokenTransaction.getToken();
        Thread.sleep(IntegrationTestsHelpers.THREAD_SLEEP);
        chargeTransaction = kushki.charge(token, amount);
    }

    @Test
    public void shouldReturnSuccessfulChargeTransactionTC006() throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        assertsValidTransaction(tokenTransaction);
        assertsValidTransaction(chargeTransaction);
    }

    @Test
    public void shouldReturnNonSuccessfulChargeTransactionUsedTokenTC007() throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException, InterruptedException {
        Thread.sleep(IntegrationTestsHelpers.THREAD_SLEEP);
        Transaction secondChargeTransaction = kushki.charge(token, amount);

        assertsValidTransaction(tokenTransaction);
        assertsValidTransaction(chargeTransaction);
//        assertsTransaction(secondChargeTransaction, false, "El token de la transacción ha expirado", "575");
        assertsTransaction(secondChargeTransaction, false, "El token de la transacción ha expirado", "578");
    }

    @Test
    public void shouldReturnNonSuccessfulChargeTransactionInvalidTokenTC008() throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException, InterruptedException {
        String token = "k7jwynu59sd28wu81i2ygsyvllyfimju";

        Thread.sleep(IntegrationTestsHelpers.THREAD_SLEEP);
        Transaction chargeTransaction = kushki.charge(token, amount);

//        assertsTransaction(chargeTransaction, false, "El token de la transacción no es válido", "574");
        assertsTransaction(chargeTransaction, false, "El token de la transacción no es válido", "577");
    }

    @Test
    public void shouldReturnFailedChargeTransactionInvalidCurrencyTC023() throws IOException, BadPaddingException, IllegalBlockSizeException, KushkiException, InvalidKeySpecException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InterruptedException {
        String merchantId = "10000001408518323354818001";
        String language = "es";
        String currency = "xyz";
        kushki = new Kushki(merchantId, language, currency);
        Thread.sleep(IntegrationTestsHelpers.THREAD_SLEEP);
        Transaction tokenTransaction = getValidTokenTransaction(kushki);
        String token = tokenTransaction.getToken();

        Transaction chargeTransaction = kushki.charge(token, amount);

        assertsValidTransaction(tokenTransaction);
        assertsTransaction(chargeTransaction, false, "Tipo de moneda no válida", "205");
    }
}

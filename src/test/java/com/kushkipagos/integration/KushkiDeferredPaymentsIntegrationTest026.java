package com.kushkipagos.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kushkipagos.Amount;
import com.kushkipagos.Kushki;
import com.kushkipagos.KushkiException;
import com.kushkipagos.Transaction;
import org.junit.Before;
import org.junit.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import static com.kushkipagos.commons.TestsHelpers.getRandomAmount;
import static com.kushkipagos.commons.TestsHelpers.getRandomMonths;
import static com.kushkipagos.integration.IntegrationTestsHelpers.assertsValidTransaction;
import static com.kushkipagos.integration.IntegrationTestsHelpers.setupKushki;
import static com.kushkipagos.integration.TokenHelper.getValidTokenTransaction;

public class KushkiDeferredPaymentsIntegrationTest026 {

    private Kushki kushki;
    private Kushki secretKushki;

    @Before
    public void setUp() throws Exception {
        kushki = setupKushki(false);
        secretKushki = setupKushki(true);
    }

    @Test
    public void shouldReturnSuccessfulDeferredChargeTransactionTC026() throws BadPaddingException, IllegalBlockSizeException, JsonProcessingException, KushkiException, InterruptedException {
        Amount amount = getRandomAmount();
        Transaction tokenTransaction = getValidTokenTransaction(kushki, amount.getTotalAmount());
        Integer months = getRandomMonths();
        String token = tokenTransaction.getToken();

        Thread.sleep(IntegrationTestsHelpers.THREAD_SLEEP);
        Transaction deferredChargeTransaction = secretKushki.deferredCharge(token, amount, months);

        assertsValidTransaction(tokenTransaction);
        assertsValidTransaction(deferredChargeTransaction);
    }
}

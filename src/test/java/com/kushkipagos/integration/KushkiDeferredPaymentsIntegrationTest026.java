package com.kushkipagos.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kushkipagos.Kushki;
import com.kushkipagos.KushkiException;
import com.kushkipagos.commons.TestsHelpers;
import com.kushkipagos.Transaction;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import static com.kushkipagos.commons.TestsHelpers.getRandomDoubleAmount;
import static com.kushkipagos.commons.TestsHelpers.getRandomInterest;
import static com.kushkipagos.commons.TestsHelpers.getRandomMonths;
import static com.kushkipagos.integration.IntegrationTestsHelpers.assertsValidTransaction;
import static com.kushkipagos.integration.IntegrationTestsHelpers.getValidTokenTransaction;
import static com.kushkipagos.integration.IntegrationTestsHelpers.setupKushki;

/**
 * Created by lmunda on 1/21/16 16:15.
 */
public class KushkiDeferredPaymentsIntegrationTest026 {

    private Kushki kushki;
    private Kushki secretKushki;

    @Before
    public void setUp() throws Exception {
        kushki = setupKushki(false);
        secretKushki = setupKushki(true);
    }

    @Ignore("Test is ignored: working on charge method")
    @Test
    public void shouldReturnSuccessfulDeferredChargeTransactionTC026() throws BadPaddingException, IllegalBlockSizeException, JsonProcessingException, KushkiException, InterruptedException {
        Transaction tokenTransaction = getValidTokenTransaction(kushki);
        Double amount = getRandomDoubleAmount();
        Integer months = getRandomMonths();
        Double interest = getRandomInterest();
        String token = tokenTransaction.getToken();

        Thread.sleep(IntegrationTestsHelpers.THREAD_SLEEP);
        Transaction deferredChargeTransaction = secretKushki.deferredCharge(token, amount, months, interest);

        assertsValidTransaction(tokenTransaction);
        assertsValidTransaction(deferredChargeTransaction);
    }
}

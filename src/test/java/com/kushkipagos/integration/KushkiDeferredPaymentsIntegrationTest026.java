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

import static com.kushkipagos.commons.TestsHelpers.*;
import static com.kushkipagos.integration.IntegrationTestsHelpers.*;
import static com.kushkipagos.integration.TokenHelper.getValidTokenTransaction;
import static com.kushkipagos.integration.TokenHelper.getValidTokenTransactionColombia;

public class KushkiDeferredPaymentsIntegrationTest026 {

    private Kushki kushki;
    private Kushki kushkiColombia;
    private Kushki secretKushki;
    private Kushki secretKushkiColombia;

    @Before
    public void setUp() throws Exception {
        kushki = setupKushki(false);
        kushkiColombia = setupKushkiColombia(false);
        secretKushki = setupKushki(true);
        secretKushkiColombia = setupKushkiColombia(true);
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

    @Test
    public void shouldReturnSuccessfulDeferredChargeTransactionTC026Colombia() throws BadPaddingException, IllegalBlockSizeException, JsonProcessingException, KushkiException, InterruptedException {
        Amount amount = getRandomAmountColombia();
        Transaction tokenTransaction = getValidTokenTransactionColombia(kushkiColombia, amount.getTotalAmount());
        Integer months = getRandomMonthsColombia();
        String token = tokenTransaction.getToken();

        Thread.sleep(IntegrationTestsHelpers.THREAD_SLEEP);
        Transaction deferredChargeTransaction = secretKushkiColombia.deferredCharge(token, amount, months);

        // TODO: Uncomment this when Colombian void starts working
        //assertsValidTransaction(tokenTransaction);
        //assertsValidTransaction(deferredChargeTransaction);
    }
}

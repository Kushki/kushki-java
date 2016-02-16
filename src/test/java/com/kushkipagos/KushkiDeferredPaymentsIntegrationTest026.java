package com.kushkipagos;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Before;
import org.junit.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by lmunda on 1/21/16 16:15.
 */
public class KushkiDeferredPaymentsIntegrationTest026 {

    private Kushki kushki;

    @Before
    public void setUp() throws Exception {
        kushki = IntegrationTestsHelpers.setupKushki();
    }

    @Test
    public void shouldReturnSuccessfulDeferredChargeTransactionTC026() throws BadPaddingException, IllegalBlockSizeException, JsonProcessingException, KushkiException, InterruptedException {
        Transaction tokenTransaction = IntegrationTestsHelpers.getValidTokenTransaction(kushki);
        Double amount = TestsHelpers.getRandomAmount();
        Integer months = TestsHelpers.getRandomMonths();
        Double interest = TestsHelpers.getRandomInterest();
        String token = tokenTransaction.getToken();

        Thread.sleep(TestsHelpers.THREAD_SLEEP);
        Transaction deferredChargeTransaction = kushki.deferredCharge(token, amount, months, interest);

        assertThat(tokenTransaction.isSuccessful(), is(true));
        assertThat(deferredChargeTransaction.isSuccessful(), is(true));
        assertThat(deferredChargeTransaction.getResponseText(), is("Transacción aprobada"));
        assertThat(deferredChargeTransaction.getResponseCode(), is("000"));
    }
}

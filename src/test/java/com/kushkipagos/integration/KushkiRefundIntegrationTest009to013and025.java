package com.kushkipagos.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kushkipagos.Amount;
import com.kushkipagos.Kushki;
import com.kushkipagos.KushkiException;
import com.kushkipagos.commons.TestsHelpers;
import com.kushkipagos.Transaction;
import org.junit.Before;
import org.junit.Ignore;
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
 * Created by lmunda on 1/22/16 10:33.
 */
public class KushkiRefundIntegrationTest009to013and025 {
    private Kushki kushki;
    private Kushki secretKushki;
    private Transaction tokenTransaction;
    private Transaction chargeTransaction;
    private Transaction refundTransaction;
    private Amount amount;
    private Double refundAmount;
    private String ticket;

    @Before
    public void setUp() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, IOException, BadPaddingException, IllegalBlockSizeException, KushkiException, InterruptedException {
        kushki = setupKushki(false);
        secretKushki = setupKushki(true);

        tokenTransaction = getValidTokenTransaction(kushki);
        amount = TestsHelpers.getRandomAmount();
        refundAmount = TestsHelpers.getRandomDoubleAmount();
        String token = tokenTransaction.getToken();
        Thread.sleep(IntegrationTestsHelpers.THREAD_SLEEP);
        chargeTransaction = secretKushki.charge(token, amount);
        ticket = chargeTransaction.getTicketNumber();
        Thread.sleep(IntegrationTestsHelpers.THREAD_SLEEP);
        refundTransaction = secretKushki.refundCharge(ticket, refundAmount);
    }

    @Ignore("Test is ignored: working on charge method")
    @Test
    public void shouldReturnSuccessfulRefundTransactionTC009() throws BadPaddingException, IllegalBlockSizeException, JsonProcessingException, KushkiException {
        assertsValidTransaction(tokenTransaction);
        assertsValidTransaction(chargeTransaction);
        assertsValidTransaction(refundTransaction);
    }

    @Test
    public void shouldReturnNonSuccessfulRefundTransactionNoTicketTC012() throws BadPaddingException, IllegalBlockSizeException, JsonProcessingException, KushkiException, InterruptedException {
        Thread.sleep(IntegrationTestsHelpers.THREAD_SLEEP);
        Transaction refundTransaction = secretKushki.refundCharge("", refundAmount);

        assertsTransaction(refundTransaction, false, "El número de ticket de la transacción es requerido", "705");
    }

    @Ignore("Test is ignored: working on charge method")
    @Test
    public void shouldReturnFailedRefundTransactionInvalidTicketTC013() throws BadPaddingException, IllegalBlockSizeException, JsonProcessingException, KushkiException, InterruptedException {
        String ticket = "153633977318400068";

        Thread.sleep(IntegrationTestsHelpers.THREAD_SLEEP);

        Transaction refundTransaction = secretKushki.refundCharge(ticket, refundAmount);

        assertsTransaction(refundTransaction, false, "Transacción no encontrada", "222");
    }

    @Ignore("Test is ignored: working on charge method")
    @Test
    public void shouldReturnFailedVoidTransactionAfterRefundingChargeTC025() throws BadPaddingException, IllegalBlockSizeException, JsonProcessingException, KushkiException, InterruptedException {
        Thread.sleep(IntegrationTestsHelpers.THREAD_SLEEP);
        Transaction voidTransaction = secretKushki.voidCharge(ticket, refundAmount);

        assertsValidTransaction(tokenTransaction);
        assertsValidTransaction(chargeTransaction);
        assertsValidTransaction(refundTransaction);
        assertsTransaction(voidTransaction, false, "Anulación de venta no permitida", "231");
    }
}

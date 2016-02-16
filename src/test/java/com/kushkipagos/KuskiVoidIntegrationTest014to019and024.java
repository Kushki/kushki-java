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

import static com.kushkipagos.IntegrationTestsHelpers.assertsTransaction;
import static com.kushkipagos.IntegrationTestsHelpers.assertsValidTransaction;
import static com.kushkipagos.IntegrationTestsHelpers.getValidTokenTransaction;
import static com.kushkipagos.IntegrationTestsHelpers.setupKushki;

/**
 * Created by lmunda on 1/22/16 10:50.
 */
public class KuskiVoidIntegrationTest014to019and024 {
    private Kushki kushki;
    private Transaction tokenTransaction;
    private Transaction chargeTransaction;
    private Transaction voidTransaction;
    private Double amount;
    private String ticket;

    @Before
    public void setUp() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, IOException, BadPaddingException, IllegalBlockSizeException, KushkiException, InterruptedException {
        kushki = setupKushki();

        tokenTransaction = getValidTokenTransaction(kushki);
        amount = TestsHelpers.getRandomAmount();
        String token = tokenTransaction.getToken();
        Thread.sleep(600);
        chargeTransaction = kushki.charge(token, amount);
        ticket = chargeTransaction.getTicketNumber();
        Thread.sleep(600);
        voidTransaction = kushki.voidCharge(ticket, amount);
    }

    @Test
    public void shouldReturnSuccessfulVoidTransactionTC014() throws BadPaddingException, IllegalBlockSizeException, JsonProcessingException, KushkiException {
        assertsValidTransaction(tokenTransaction);
        assertsValidTransaction(chargeTransaction);
        assertsValidTransaction(voidTransaction);
    }

    @Test
    public void shouldReturnFailedVoidTransactionNoTicketTC018() throws BadPaddingException, IllegalBlockSizeException, JsonProcessingException, KushkiException {
        Transaction voidTransaction = kushki.voidCharge("", amount);

        assertsTransaction(voidTransaction, false, "El número de ticket de la transacción es requerido", "705");
    }

    @Test
    public void shouldReturnFailedVoidTransactionInvalidTicketTC019() throws BadPaddingException, IllegalBlockSizeException, JsonProcessingException, KushkiException {
        String ticket = "153633977318400068";

        Transaction voidTransaction = kushki.voidCharge(ticket, amount);

        assertsTransaction(voidTransaction, false, "Transacción no encontrada", "222");
    }

    @Test
    public void shouldReturnFailedRefundTransactionAfterVoidingChargeTC024() throws BadPaddingException, IllegalBlockSizeException, JsonProcessingException, KushkiException {
        Transaction refundTransaction = kushki.refundCharge(ticket, amount);

        assertsValidTransaction(tokenTransaction);
        assertsValidTransaction(chargeTransaction);
        assertsValidTransaction(voidTransaction);
        assertsTransaction(refundTransaction, false, "Transacción no encontrada", "222");
    }
}

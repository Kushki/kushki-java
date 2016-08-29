package com.kushkipagos.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kushkipagos.Amount;
import com.kushkipagos.Kushki;
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

import static com.kushkipagos.integration.IntegrationTestsHelpers.assertsTransaction;
import static com.kushkipagos.integration.IntegrationTestsHelpers.assertsValidTransaction;
import static com.kushkipagos.integration.IntegrationTestsHelpers.setupKushki;
import static com.kushkipagos.integration.TokenHelper.getValidTokenTransaction;

public class KushkiVoidIntegrationTest014to019and024 {
    private Kushki secretKushki;
    private Transaction tokenTransaction;
    private Transaction chargeTransaction;
    private Transaction voidTransaction;
    private Amount amount;

    @Before
    public void setUp() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, IOException, BadPaddingException, IllegalBlockSizeException, KushkiException, InterruptedException {
        Kushki kushki = setupKushki(false);
        secretKushki = setupKushki(true);

        amount = TestsHelpers.getRandomAmount();
        tokenTransaction = getValidTokenTransaction(kushki, amount.getTotalAmount());
        String token = tokenTransaction.getToken();
        Thread.sleep(IntegrationTestsHelpers.THREAD_SLEEP);
        chargeTransaction = secretKushki.charge(token, amount);
        String ticket = chargeTransaction.getTicketNumber();
        Thread.sleep(IntegrationTestsHelpers.THREAD_SLEEP_VOID);
        voidTransaction = secretKushki.voidCharge(ticket, amount);
    }

    @Test
    public void shouldReturnSuccessfulVoidTransactionTC014() throws BadPaddingException, IllegalBlockSizeException, JsonProcessingException, KushkiException {
        assertsValidTransaction(tokenTransaction);
        assertsValidTransaction(chargeTransaction);
        System.out.println("Charge Ticket Number: " + chargeTransaction.getTicketNumber());
        assertsValidTransaction(voidTransaction);
    }

    @Test
    public void shouldReturnFailedVoidTransactionNoTicketTC018() throws BadPaddingException, IllegalBlockSizeException, JsonProcessingException, KushkiException, InterruptedException {
        Thread.sleep(IntegrationTestsHelpers.THREAD_SLEEP_VOID);
        Transaction voidTransaction = secretKushki.voidCharge("", amount);

        assertsTransaction(voidTransaction, false, "El número de ticket de la transacción es requerido", "705");
    }

    @Test
    public void shouldReturnFailedVoidTransactionInvalidTicketTC019() throws BadPaddingException, IllegalBlockSizeException, JsonProcessingException, KushkiException, InterruptedException {
        String ticket = "153633977318400068";

        Thread.sleep(IntegrationTestsHelpers.THREAD_SLEEP_VOID);
        Transaction voidTransaction = secretKushki.voidCharge(ticket, amount);

        assertsTransaction(voidTransaction, false, "Transacción no encontrada", "222");
    }
}

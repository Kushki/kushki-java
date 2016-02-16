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

import static com.kushkipagos.IntegrationTestsHelpers.*;

/**
 * Created by lmunda on 1/22/16 10:33.
 */
public class KushkiRefundIntegrationTest_009_013__025 {
    private Kushki kushki;
    private Transaction tokenTransaction;
    private Transaction chargeTransaction;
    private Transaction refundTransaction;
    private Double amount;
    private String ticket;

    @Before
    public void setup() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, IOException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        kushki = setupKushki();

        tokenTransaction = getValidTokenTransaction(kushki);
        amount = TestsHelpers.getRandomAmount();
        String token = tokenTransaction.getToken();
        chargeTransaction = kushki.charge(token, amount);
        ticket = chargeTransaction.getTicketNumber();

        refundTransaction = kushki.refundCharge(ticket, amount);
    }

    @Test
    public void shouldReturnSuccessfulRefundTransaction_TC_009() throws BadPaddingException, IllegalBlockSizeException, JsonProcessingException, KushkiException {
        assertsValidTransaction(tokenTransaction);
        assertsValidTransaction(chargeTransaction);
        assertsValidTransaction(refundTransaction);
    }

    @Test
    public void shouldReturnNonSuccessfulRefundTransactionNoTicket_TC_012() throws BadPaddingException, IllegalBlockSizeException, JsonProcessingException, KushkiException {
        Transaction refundTransaction = kushki.refundCharge("", amount);

        assertsTransaction(refundTransaction, false, "El número de ticket de la transacción es requerido", "705");
    }

    @Test
    public void shouldReturnFailedRefundTransactionInvalidTicket_TC_013() throws BadPaddingException, IllegalBlockSizeException, JsonProcessingException, KushkiException {
        String ticket = "153633977318400068";

        Transaction refundTransaction = kushki.refundCharge(ticket, amount);

        assertsTransaction(refundTransaction, false, "Transacción no encontrada", "222");
    }

    @Test
    public void shouldReturnFailedVoidTransactionAfterRefundingCharge_TC_025() throws BadPaddingException, IllegalBlockSizeException, JsonProcessingException, KushkiException {
        Transaction voidTransaction = kushki.voidCharge(ticket, amount);

        assertsValidTransaction(tokenTransaction);
        assertsValidTransaction(chargeTransaction);
        assertsValidTransaction(refundTransaction);
        assertsTransaction(voidTransaction, false, "Anulación de venta no permitida", "231");
    }
}
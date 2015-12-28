package com.kushkipagos;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Before;
import org.junit.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class KushkiIntegrationTest {
    private Kushki kushki;

    @Before
    public void setUp() throws Exception {
        String merchantId = "10000001408518323354818001";
        String language = "es";
        String currency = "USD";
        kushki = new Kushki(merchantId, language, currency);
    }

    @Test
    public void shouldReturnSuccessfulTokenTransaction() throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        Transaction transaction = getValidTokenTransaction();
        assertThat(transaction.isSuccessful(), is(true));
        assertThat(transaction.getResponseText(), is("Transacción aprobada"));
    }

    @Test
    public void shouldReturnNonSuccessfulTokenTransactionInvalidCard() throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        Map<String, String> cardParams = new HashMap<>(5);
        cardParams.put("name", "John Doe");
        cardParams.put("number", "5411111111115854");
        cardParams.put("expiry_month", "12");
        cardParams.put("expiry_year", "20");
        cardParams.put("cvv", "123");
        Transaction transaction = kushki.getToken(cardParams);
        assertThat(transaction.isSuccessful(), is(false));
        assertThat(transaction.getResponseText(), is("Tarjeta no válida"));
    }

    @Test
    public void shouldReturnNonSuccessfulTokenTransactionInvalidExpiry() throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        Map<String, String> cardParams = new HashMap<>(5);
        cardParams.put("name", "John Doe");
        cardParams.put("number", "4111111111111111");
        cardParams.put("expiry_month", "ab");
        cardParams.put("expiry_year", "cd");
        cardParams.put("cvv", "123");
        Transaction transaction = kushki.getToken(cardParams);
        assertThat(transaction.isSuccessful(), is(false));
        assertThat(transaction.getResponseText(), is("Tarjeta no válida"));
    }

    @Test
    public void shouldReturnNonSuccessfulTokenTransactionCardExpired() throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        Map<String, String> cardParams = new HashMap<>(5);
        cardParams.put("name", "John Doe");
        cardParams.put("number", "4111111111111111");
        cardParams.put("expiry_month", "12");
        cardParams.put("expiry_year", "14");
        cardParams.put("cvv", "123");
        Transaction transaction = kushki.getToken(cardParams);
        assertThat(transaction.isSuccessful(), is(false));
        assertThat(transaction.getResponseText(), is("Tarjeta vencida"));
    }

    @Test
    public void shouldReturnNonSuccessfulTokenTransactionInvalidCVV() throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        Map<String, String> cardParams = new HashMap<>(5);
        cardParams.put("name", "John Doe");
        cardParams.put("number", "4111111111111111");
        cardParams.put("expiry_month", "12");
        cardParams.put("expiry_year", "14");
        cardParams.put("cvv", "abc");
        Transaction transaction = kushki.getToken(cardParams);
        assertThat(transaction.isSuccessful(), is(false));
        assertThat(transaction.getResponseText(), is("CVC no válido"));
    }

    @Test
    public void shouldReturnSuccessfulChargeTransaction() throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        Transaction tokenTransaction = getValidTokenTransaction();
        Double amount = TestsHelpers.getRandomAmount();
        String token = tokenTransaction.getToken();
        Transaction transaction = kushki.charge(token, amount);
        assertThat(transaction.isSuccessful(), is(true));
    }

    @Test
    public void shouldReturnNonSuccessfulChargeTransactionUsedToken() throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        Transaction tokenTransaction = getValidTokenTransaction();
        Double amount = TestsHelpers.getRandomAmount();
        String token = tokenTransaction.getToken();
        kushki.charge(token, amount);
        Transaction transaction = kushki.charge(token, amount);
        assertThat(transaction.isSuccessful(), is(false));
        assertThat(transaction.getResponseText(), is("Transacción rechazada"));
    }

    @Test
    public void shouldReturnSuccessfulRefundTransaction() throws BadPaddingException, IllegalBlockSizeException, JsonProcessingException, KushkiException {
        Transaction tokenTransaction = getValidTokenTransaction();
        Double amount = TestsHelpers.getRandomAmount();
        String token = tokenTransaction.getToken();
        Transaction transaction = kushki.charge(token, amount);
        String ticket = transaction.getTicketNumber();

        Transaction refundTransaction = kushki.refundCharge(token, ticket, amount);
        assertThat(refundTransaction.isSuccessful(), is(true));
    }

    @Test
    public void shouldReturnNonSuccessfulChargeTransactionInvalidToken() throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        String token = "123456789-declined";
        Double amount = TestsHelpers.getRandomAmount();
        Transaction transaction = kushki.charge(token, amount);
        assertThat(transaction.isSuccessful(), is(false));
    }

    private Transaction getValidTokenTransaction() throws BadPaddingException, IllegalBlockSizeException, JsonProcessingException {
        Map<String, String> cardParams = new HashMap<>(5);
        cardParams.put("name", "John Doe");
        cardParams.put("number", "4111111111111111");
        cardParams.put("expiry_month", "12");
        cardParams.put("expiry_year", "20");
        cardParams.put("cvv", "123");
        return kushki.getToken(cardParams);
    }
}

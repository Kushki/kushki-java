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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class KushkiIntegrationTest {
    private Kushki kushki;

    @Before
    public void setup() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, IOException {
        kushki = IntegrationTestsHelpers.setup();
    }

    @Test
    public void shouldReturnSuccessfulChargeTransaction_TC_006() throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        Transaction tokenTransaction = IntegrationTestsHelpers.getValidTokenTransaction(kushki);
        Double amount = TestsHelpers.getRandomAmount();
        String token = tokenTransaction.getToken();

        Transaction chargeTransaction = kushki.charge(token, amount);

        assertThat(tokenTransaction.isSuccessful(), is(true));
        assertThat(chargeTransaction.isSuccessful(), is(true));
        assertThat(chargeTransaction.getResponseText(), is("Transacción aprobada"));
        assertThat(chargeTransaction.getResponseCode(), is("000"));
    }

    @Test
    public void shouldReturnNonSuccessfulChargeTransactionUsedToken_TC_007() throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        Transaction tokenTransaction = IntegrationTestsHelpers.getValidTokenTransaction(kushki);
        Double amount = TestsHelpers.getRandomAmount();
        String token = tokenTransaction.getToken();

        Transaction firstChargeTransaction = kushki.charge(token, amount);
        Transaction secondChargeTransaction = kushki.charge(token, amount);

        assertThat(tokenTransaction.isSuccessful(), is(true));
        assertThat(firstChargeTransaction.isSuccessful(), is(true));
        assertThat(secondChargeTransaction.isSuccessful(), is(false));
        assertThat(secondChargeTransaction.getResponseText(), is("El token de la transacción ha expirado"));
        assertThat(secondChargeTransaction.getResponseCode(), is("575"));
    }

    @Test
    public void shouldReturnNonSuccessfulChargeTransactionInvalidToken_TC_008() throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        String token = "k7jwynu59sd28wu81i2ygsyvllyfimju";
        Double amount = TestsHelpers.getRandomAmount();

        Transaction chargeTransaction = kushki.charge(token, amount);

        assertThat(chargeTransaction.isSuccessful(), is(false));
        assertThat(chargeTransaction.getResponseText(), is("El token de la transacción no es válido"));
        assertThat(chargeTransaction.getResponseCode(), is("574"));
    }

    @Test
    public void shouldReturnSuccessfulRefundTransaction_TC_009() throws BadPaddingException, IllegalBlockSizeException, JsonProcessingException, KushkiException {
        Transaction tokenTransaction = IntegrationTestsHelpers.getValidTokenTransaction(kushki);
        Double amount = TestsHelpers.getRandomAmount();
        String token = tokenTransaction.getToken();
        Transaction chargeTransaction = kushki.charge(token, amount);
        String ticket = chargeTransaction.getTicketNumber();

        Transaction refundTransaction = kushki.refundCharge(ticket, amount);

        assertThat(tokenTransaction.isSuccessful(), is(true));
        assertThat(chargeTransaction.isSuccessful(), is(true));
        assertThat(refundTransaction.isSuccessful(), is(true));
        assertThat(refundTransaction.getResponseText(), is("Transacción aprobada"));
        assertThat(refundTransaction.getResponseCode(), is("000"));
    }

    @Test
    public void shouldReturnFailedRefundTransactionNoTicket_TC_012() throws BadPaddingException, IllegalBlockSizeException, JsonProcessingException, KushkiException {
        Transaction tokenTransaction = IntegrationTestsHelpers.getValidTokenTransaction(kushki);
        Double amount = TestsHelpers.getRandomAmount();

        Transaction refundTransaction = kushki.refundCharge("", amount);

        assertThat(tokenTransaction.isSuccessful(), is(true));
        assertThat(refundTransaction.isSuccessful(), is(false));
        assertThat(refundTransaction.getResponseText(), is("El número de ticket de la transacción es requerido"));
        assertThat(refundTransaction.getResponseCode(), is("705"));
    }

    @Test
    public void shouldReturnFailedRefundTransactionInvalidTicket_TC_013() throws BadPaddingException, IllegalBlockSizeException, JsonProcessingException, KushkiException {
        Double amount = TestsHelpers.getRandomAmount();
        String ticket = "153633977318400068";

        Transaction refundTransaction = kushki.refundCharge(ticket, amount);

        assertThat(refundTransaction.isSuccessful(), is(false));
        assertThat(refundTransaction.getResponseText(), is("Transacción no encontrada"));
        assertThat(refundTransaction.getResponseCode(), is("222"));
    }

    @Test
    public void shouldReturnSuccessfulVoidTransaction_TC_014() throws BadPaddingException, IllegalBlockSizeException, JsonProcessingException, KushkiException {
        Transaction tokenTransaction = IntegrationTestsHelpers.getValidTokenTransaction(kushki);
        Double amount = TestsHelpers.getRandomAmount();
        String token = tokenTransaction.getToken();
        Transaction chargeTransaction = kushki.charge(token, amount);
        String ticket = chargeTransaction.getTicketNumber();

        Transaction voidTransaction = kushki.voidCharge(ticket, amount);

        assertThat(tokenTransaction.isSuccessful(), is(true));
        assertThat(chargeTransaction.isSuccessful(), is(true));
        assertThat(voidTransaction.isSuccessful(), is(true));
        assertThat(voidTransaction.getResponseText(), is("Transacción aprobada"));
        assertThat(voidTransaction.getResponseCode(), is("000"));
    }

    @Test
    public void shouldReturnFailedVoidTransactionNoTicket_TC_018() throws BadPaddingException, IllegalBlockSizeException, JsonProcessingException, KushkiException {
        Transaction tokenTransaction = IntegrationTestsHelpers.getValidTokenTransaction(kushki);
        Double amount = TestsHelpers.getRandomAmount();

        Transaction voidTransaction = kushki.voidCharge("", amount);

        assertThat(tokenTransaction.isSuccessful(), is(true));
        assertThat(voidTransaction.isSuccessful(), is(false));
        assertThat(voidTransaction.getResponseText(), is("El número de ticket de la transacción es requerido"));
        assertThat(voidTransaction.getResponseCode(), is("705"));
    }

    @Test
    public void shouldReturnFailedVoidTransactionInvalidTicket_TC_019() throws BadPaddingException, IllegalBlockSizeException, JsonProcessingException, KushkiException {
        Double amount = TestsHelpers.getRandomAmount();
        String ticket = "153633977318400068";

        Transaction voidTransaction = kushki.voidCharge(ticket, amount);

        assertThat(voidTransaction.isSuccessful(), is(false));
        assertThat(voidTransaction.getResponseText(), is("Transacción no encontrada"));
        assertThat(voidTransaction.getResponseCode(), is("222"));
    }

    @Test
    public void shouldReturnFailedTokenTransactionInvalidMerchantId_TC_020() throws IOException, BadPaddingException, IllegalBlockSizeException, KushkiException, InvalidKeySpecException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
        String merchantId = "abc";
        String language = "es";
        String currency = "USD";
        kushki = new Kushki(merchantId, language, currency);

        Transaction tokenTransaction = IntegrationTestsHelpers.getValidTokenTransaction(kushki);

        assertThat(tokenTransaction.isSuccessful(), is(false));
        assertThat(tokenTransaction.getResponseText(), is("ID de comercio no válido"));
        assertThat(tokenTransaction.getResponseCode(), is("201"));
    }

    @Test
    public void shouldReturnFailedTokenTransactionInvalidLanguage_TC_021() throws IOException, BadPaddingException, IllegalBlockSizeException, KushkiException, InvalidKeySpecException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
        String merchantId = "10000001408518323354818001";
        String language = "xyz";
        String currency = "USD";
        kushki = new Kushki(merchantId, language, currency);

        Transaction tokenTransaction = IntegrationTestsHelpers.getValidTokenTransaction(kushki);

        assertThat(tokenTransaction.isSuccessful(), is(false));
        assertThat(tokenTransaction.getResponseText(), is("Indicador de idioma no válido"));
        assertThat(tokenTransaction.getResponseCode(), is("277"));
    }

    @Test
    public void shouldReturnFailedTokenTransactionInvalidCurrency_TC_023() throws IOException, BadPaddingException, IllegalBlockSizeException, KushkiException, InvalidKeySpecException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
        String merchantId = "10000001408518323354818001";
        String language = "es";
        String currency = "xyz";
        kushki = new Kushki(merchantId, language, currency);
        Transaction tokenTransaction = IntegrationTestsHelpers.getValidTokenTransaction(kushki);
        String token = tokenTransaction.getToken();
        Double amount = 10.0;

        Transaction chargeTransaction = kushki.charge(token, amount);

        assertThat(tokenTransaction.isSuccessful(), is(true));
        assertThat(chargeTransaction.isSuccessful(), is(false));
        assertThat(chargeTransaction.getResponseText(), is("Tipo de moneda no válida"));
        assertThat(chargeTransaction.getResponseCode(), is("205"));
    }

    @Test
    public void shouldReturnFailedRefundTransactionAfterVoidingCharge_TC_024() throws BadPaddingException, IllegalBlockSizeException, JsonProcessingException, KushkiException {
        Transaction tokenTransaction = IntegrationTestsHelpers.getValidTokenTransaction(kushki);
        Double amount = TestsHelpers.getRandomAmount();
        String token = tokenTransaction.getToken();
        Transaction chargeTransaction = kushki.charge(token, amount);
        String ticket = chargeTransaction.getTicketNumber();
        Transaction voidTransaction = kushki.voidCharge(ticket, amount);

        Transaction refundTransaction = kushki.refundCharge(ticket, amount);

        assertThat(tokenTransaction.isSuccessful(), is(true));
        assertThat(chargeTransaction.isSuccessful(), is(true));
        assertThat(voidTransaction.isSuccessful(), is(true));
        assertThat(refundTransaction.isSuccessful(), is(false));
        assertThat(refundTransaction.getResponseText(), is("Transacción no encontrada"));
        assertThat(refundTransaction.getResponseCode(), is("222"));
    }

    @Test
    public void shouldReturnFailedVoidTransactionAfterRefundingCharge_TC_025() throws BadPaddingException, IllegalBlockSizeException, JsonProcessingException, KushkiException {
        Transaction tokenTransaction = IntegrationTestsHelpers.getValidTokenTransaction(kushki);
        Double amount = TestsHelpers.getRandomAmount();
        String token = tokenTransaction.getToken();
        Transaction chargeTransaction = kushki.charge(token, amount);
        String ticket = chargeTransaction.getTicketNumber();
        Transaction refundTransaction = kushki.refundCharge(ticket, amount);

        Transaction voidTransaction = kushki.voidCharge(ticket, amount);

        assertThat(tokenTransaction.isSuccessful(), is(true));
        assertThat(chargeTransaction.isSuccessful(), is(true));
        assertThat(refundTransaction.isSuccessful(), is(true));
        assertThat(voidTransaction.isSuccessful(), is(false));
        assertThat(voidTransaction.getResponseText(), is("Anulación de venta no permitida"));
        assertThat(voidTransaction.getResponseCode(), is("231"));
    }
}

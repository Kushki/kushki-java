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
    public void shouldReturnSuccessfulTokenTransaction_TC_001() throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        Transaction tokenTransaction = getValidTokenTransaction();

        assertThat(tokenTransaction.isSuccessful(), is(true));
        assertThat(tokenTransaction.getResponseText(), is("Transacción aprobada"));
        assertThat(tokenTransaction.getResponseCode(), is("000"));
    }

    @Test
    public void shouldReturnNonSuccessfulTokenTransactionInvalidCard_TC_002() throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        Map<String, String> cardParams = new HashMap<>(5);
        cardParams.put("name", "John Doe");
        cardParams.put("number", "5411111111115854");
        cardParams.put("expiry_month", "12");
        cardParams.put("expiry_year", "20");
        cardParams.put("cvv", "123");

        Transaction tokenTransaction = kushki.requestToken(cardParams);

        assertThat(tokenTransaction.isSuccessful(), is(false));
        assertThat(tokenTransaction.getResponseText(), is("Tarjeta no válida"));
        assertThat(tokenTransaction.getResponseCode(), is("017"));
    }

    @Test
    public void shouldReturnNonSuccessfulTokenTransactionInvalidExpiry_TC_003() throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        Map<String, String> cardParams = new HashMap<>(5);
        cardParams.put("name", "John Doe");
        cardParams.put("number", "4111111111111111");
        cardParams.put("expiry_month", "ab");
        cardParams.put("expiry_year", "cd");
        cardParams.put("cvv", "123");

        Transaction tokenTransaction = kushki.requestToken(cardParams);

        assertThat(tokenTransaction.isSuccessful(), is(false));
        assertThat(tokenTransaction.getResponseText(), is("Tarjeta no válida"));
        assertThat(tokenTransaction.getResponseCode(), is("017"));
    }

    @Test
    public void shouldReturnNonSuccessfulTokenTransactionCardExpired_TC_004() throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        Map<String, String> cardParams = new HashMap<>(5);
        cardParams.put("name", "John Doe");
        cardParams.put("number", "4111111111111111");
        cardParams.put("expiry_month", "12");
        cardParams.put("expiry_year", "14");
        cardParams.put("cvv", "123");

        Transaction tokenTransaction = kushki.requestToken(cardParams);

        assertThat(tokenTransaction.isSuccessful(), is(false));
        assertThat(tokenTransaction.getResponseText(), is("Tarjeta vencida"));
        assertThat(tokenTransaction.getResponseCode(), is("018"));
    }

    @Test
    public void shouldReturnNonSuccessfulTokenTransactionInvalidCVV_TC_005() throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        Map<String, String> cardParams = new HashMap<>(5);
        cardParams.put("name", "John Doe");
        cardParams.put("number", "4111111111111111");
        cardParams.put("expiry_month", "12");
        cardParams.put("expiry_year", "14");
        cardParams.put("cvv", "abc");

        Transaction tokenTransaction = kushki.requestToken(cardParams);

        assertThat(tokenTransaction.isSuccessful(), is(false));
        assertThat(tokenTransaction.getResponseText(), is("CVC no válido"));
        assertThat(tokenTransaction.getResponseCode(), is("007"));
    }

    @Test
    public void shouldReturnSuccessfulChargeTransaction_TC_006() throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        Transaction tokenTransaction = getValidTokenTransaction();
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
        Transaction tokenTransaction = getValidTokenTransaction();
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
        Transaction tokenTransaction = getValidTokenTransaction();
        Double amount = TestsHelpers.getRandomAmount();
        String token = tokenTransaction.getToken();
        Transaction chargeTransaction = kushki.charge(token, amount);
        String ticket = chargeTransaction.getTicketNumber();
        Transaction token4refundTransaction = getValidTokenTransaction();
        String token4refund = token4refundTransaction.getToken();

        Transaction refundTransaction = kushki.refundCharge(token4refund, ticket, amount);

        assertThat(tokenTransaction.isSuccessful(), is(true));
        assertThat(chargeTransaction.isSuccessful(), is(true));
        assertThat(token4refundTransaction.isSuccessful(), is(true));
        assertThat(refundTransaction.isSuccessful(), is(true));
        assertThat(refundTransaction.getResponseText(), is("Transacción aprobada"));
        assertThat(refundTransaction.getResponseCode(), is("000"));
    }

    @Test
    public void shouldReturnFailedRefundTransactionUsedToken_TC_010() throws BadPaddingException, IllegalBlockSizeException, JsonProcessingException, KushkiException {
        Transaction tokenTransaction = getValidTokenTransaction();
        Double amount = TestsHelpers.getRandomAmount();
        String token = tokenTransaction.getToken();
        Transaction chargeTransaction = kushki.charge(token, amount);
        String ticket = chargeTransaction.getTicketNumber();
        Transaction token4refundTransaction = getValidTokenTransaction();
        String token4refund = token4refundTransaction.getToken();

        Transaction firstRefundTransaction = kushki.refundCharge(token4refund, ticket, amount);
        Transaction secondRefundTransaction = kushki.refundCharge(token4refund, ticket, amount);

        assertThat(tokenTransaction.isSuccessful(), is(true));
        assertThat(chargeTransaction.isSuccessful(), is(true));
        assertThat(firstRefundTransaction.isSuccessful(), is(true));
        assertThat(secondRefundTransaction.isSuccessful(), is(false));
        assertThat(secondRefundTransaction.getResponseText(), is("El token de la transacción ha expirado"));
        assertThat(secondRefundTransaction.getResponseCode(), is("575"));
    }

    @Test
    public void shouldReturnFailedRefundTransactionInvalidToken_TC_011() throws BadPaddingException, IllegalBlockSizeException, JsonProcessingException, KushkiException {
        Transaction tokenTransaction = getValidTokenTransaction();
        Double amount = TestsHelpers.getRandomAmount();
        String token = tokenTransaction.getToken();
        Transaction chargeTransaction = kushki.charge(token, amount);
        String ticket = chargeTransaction.getTicketNumber();
        String token4refund = "k7jwynu59sd28xu81i2ygsyvllyfimju";

        Transaction refundTransaction = kushki.refundCharge(token4refund, ticket, amount);

        assertThat(refundTransaction.isSuccessful(), is(false));
        assertThat(refundTransaction.getResponseText(), is("El token de la transacción no es válido"));
        assertThat(refundTransaction.getResponseCode(), is("574"));
    }

    @Test
    public void shouldReturnFailedRefundTransactionNoTicket_TC_012() throws BadPaddingException, IllegalBlockSizeException, JsonProcessingException, KushkiException {
        Transaction tokenTransaction = getValidTokenTransaction();
        Double amount = TestsHelpers.getRandomAmount();
        String token = tokenTransaction.getToken();

        Transaction refundTransaction = kushki.refundCharge(token, "", amount);

        assertThat(refundTransaction.isSuccessful(), is(false));
        assertThat(refundTransaction.getResponseText(), is("El número de ticket de la transacción es requerido"));
        assertThat(refundTransaction.getResponseCode(), is("705"));
    }

    @Test
    public void shouldReturnFailedRefundTransactionInvalidTicket_TC_013() throws BadPaddingException, IllegalBlockSizeException, JsonProcessingException, KushkiException {
        Transaction tokenTransaction = getValidTokenTransaction();
        Double amount = TestsHelpers.getRandomAmount();
        String token = tokenTransaction.getToken();
        String ticket = "153633977318400068";

        Transaction refundTransaction = kushki.refundCharge(token, ticket, amount);

        assertThat(tokenTransaction.isSuccessful(), is(true));
        assertThat(refundTransaction.isSuccessful(), is(false));
        assertThat(refundTransaction.getResponseText(), is("Transacción no encontrada"));
        assertThat(refundTransaction.getResponseCode(), is("222"));
    }

    @Test
    public void shouldReturnSuccessfulVoidTransaction_TC_014() throws BadPaddingException, IllegalBlockSizeException, JsonProcessingException, KushkiException {
        Transaction tokenTransaction = getValidTokenTransaction();
        Double amount = TestsHelpers.getRandomAmount();
        String token = tokenTransaction.getToken();
        Transaction chargeTransaction = kushki.charge(token, amount);
        String ticket = chargeTransaction.getTicketNumber();
        Transaction token4voidTransaction = getValidTokenTransaction();
        String token4void = token4voidTransaction.getToken();

        Transaction voidTransaction = kushki.voidCharge(token4void, ticket, amount);

        assertThat(tokenTransaction.isSuccessful(), is(true));
        assertThat(chargeTransaction.isSuccessful(), is(true));
        assertThat(token4voidTransaction.isSuccessful(), is(true));
        assertThat(voidTransaction.isSuccessful(), is(true));
        assertThat(voidTransaction.getResponseText(), is("Transacción aprobada"));
        assertThat(voidTransaction.getResponseCode(), is("000"));
    }

    @Test
    public void shouldReturnFailedVoidTransactionUsedToken_TC_015() throws BadPaddingException, IllegalBlockSizeException, JsonProcessingException, KushkiException {
        Transaction tokenTransaction = getValidTokenTransaction();
        Double amount = TestsHelpers.getRandomAmount();
        String token = tokenTransaction.getToken();
        Transaction chargeTransaction = kushki.charge(token, amount);
        String ticket = chargeTransaction.getTicketNumber();
        Transaction token4voidTransaction = getValidTokenTransaction();
        String token4void = token4voidTransaction.getToken();

        Transaction firstVoidTransaction = kushki.voidCharge(token4void, ticket, amount);
        Transaction secondVoidTransaction = kushki.voidCharge(token4void, ticket, amount);

        assertThat(tokenTransaction.isSuccessful(), is(true));
        assertThat(chargeTransaction.isSuccessful(), is(true));
        assertThat(token4voidTransaction.isSuccessful(), is(true));
        assertThat(firstVoidTransaction.isSuccessful(), is(true));
        assertThat(secondVoidTransaction.isSuccessful(), is(false));
        assertThat(secondVoidTransaction.getResponseText(), is("El token de la transacción ha expirado"));
        assertThat(secondVoidTransaction.getResponseCode(), is("575"));
    }

    @Test
    public void shouldReturnFailedVoidTransactionInvalidToken_TC_016() throws BadPaddingException, IllegalBlockSizeException, JsonProcessingException, KushkiException {
        Transaction tokenTransaction = getValidTokenTransaction();
        Double amount = TestsHelpers.getRandomAmount();
        String token = tokenTransaction.getToken();
        Transaction chargeTransaction = kushki.charge(token, amount);
        String ticket = chargeTransaction.getTicketNumber();
        String token4void = "k7jwynu59sd28wu81i2ygsyvllyfimju";

        Transaction voidTransaction = kushki.voidCharge(token4void, ticket, amount);

        assertThat(tokenTransaction.isSuccessful(), is(true));
        assertThat(chargeTransaction.isSuccessful(), is(true));
        assertThat(voidTransaction.isSuccessful(), is(false));
        assertThat(voidTransaction.getResponseText(), is("El token de la transacción no es válido"));
        assertThat(voidTransaction.getResponseCode(), is("574"));
    }

    @Test
    public void shouldReturnFailedVoidTransactionInvalidFormatToken_TC_017() throws BadPaddingException, IllegalBlockSizeException, JsonProcessingException, KushkiException {
        Transaction tokenTransaction = getValidTokenTransaction();
        Double amount = TestsHelpers.getRandomAmount();
        String token = tokenTransaction.getToken();
        Transaction chargeTransaction = kushki.charge(token, amount);
        String ticket = chargeTransaction.getTicketNumber();
        String token4void = "xyz";

        Transaction voidTransaction = kushki.voidCharge(token4void, ticket, amount);

        assertThat(tokenTransaction.isSuccessful(), is(true));
        assertThat(chargeTransaction.isSuccessful(), is(true));
        assertThat(voidTransaction.isSuccessful(), is(false));
        assertThat(voidTransaction.getResponseText(), is("El token de la transacción no es válido"));
        assertThat(voidTransaction.getResponseCode(), is("574"));
    }

    @Test
    public void shouldReturnFailedVoidTransactionNoTicket_TC_018() throws BadPaddingException, IllegalBlockSizeException, JsonProcessingException, KushkiException {
        Transaction tokenTransaction = getValidTokenTransaction();
        Double amount = TestsHelpers.getRandomAmount();
        String token = tokenTransaction.getToken();

        Transaction voidTransaction = kushki.voidCharge(token, "", amount);

        assertThat(tokenTransaction.isSuccessful(), is(true));
        assertThat(voidTransaction.isSuccessful(), is(false));
        assertThat(voidTransaction.getResponseText(), is("El número de ticket de la transacción es requerido"));
        assertThat(voidTransaction.getResponseCode(), is("705"));
    }

    @Test
    public void shouldReturnFailedVoidTransactionInvalidTicket_TC_019() throws BadPaddingException, IllegalBlockSizeException, JsonProcessingException, KushkiException {
        Transaction tokenTransaction = getValidTokenTransaction();
        Double amount = TestsHelpers.getRandomAmount();
        String token = tokenTransaction.getToken();
        String ticket = "153633977318400068";

        Transaction voidTransaction = kushki.voidCharge(token, ticket, amount);

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

        Transaction tokenTransaction = getValidTokenTransaction();

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

        Transaction tokenTransaction = getValidTokenTransaction();

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
        Transaction tokenTransaction = getValidTokenTransaction();
        String token = tokenTransaction.getToken();
        Double amount = 10.0;

        Transaction chargeTransaction = kushki.charge(token, amount);

        assertThat(tokenTransaction.isSuccessful(), is(true));
        assertThat(chargeTransaction.isSuccessful(), is(false));
        assertThat(chargeTransaction.getResponseText(), is("Tipo de moneda no válida"));
        assertThat(chargeTransaction.getResponseCode(), is("205"));
    }


    private Transaction getValidTokenTransaction() throws BadPaddingException, IllegalBlockSizeException, JsonProcessingException {
        Map<String, String> cardParams = new HashMap<>(5);
        cardParams.put("name", "John Doe");
        cardParams.put("number", "4111111111111111");
        cardParams.put("expiry_month", "12");
        cardParams.put("expiry_year", "20");
        cardParams.put("cvv", "123");
        return kushki.requestToken(cardParams);
    }
}

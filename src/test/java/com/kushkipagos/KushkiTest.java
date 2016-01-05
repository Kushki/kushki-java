package com.kushkipagos;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.util.Map;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class KushkiTest {
    private String merchantId;
    private Kushki kushki;
    private String language;

    private final String URL = "https://ping.auruspay.com/kushki/api/v1";

    @Before
    public void setUp() throws Exception {
        merchantId = randomAlphabetic(10);
        language = randomAlphabetic(2);
        String currency = randomAlphabetic(10);
        kushki = new Kushki(merchantId, language, currency);
    }

    @Test
    public void shouldCreateInstanceWithMerchantId() {
        assertThat(kushki.getMerchantId(), is(merchantId));
    }

    @Test
    public void shouldHaveAPIURL() {
        assertThat(Kushki.URL, is(URL));
    }

    @Test
    public void shouldHaveChargeURL() {
        assertThat(Kushki.CHARGE_URL, is(URL + "/charge"));
    }

    @Test
    public void shouldHaveTokensURL() {
        assertThat(Kushki.TOKENS_URL, is(URL + "/tokens"));
    }

    @Test
    public void shouldHaveVoidURL() {
        assertThat(Kushki.VOID_URL, is(URL + "/void"));
    }

    @Test
    public void shouldHaveRefundURL() {
        assertThat(Kushki.REFUND_URL, is(URL + "/refund"));
    }

    @Test
    public void shouldCreateInstanceWithLanguage() {
        assertThat(kushki.getLanguage(), is(language));
    }

    @Test
    public void shouldRequestToken() throws IllegalBlockSizeException, IllegalAccessException, BadPaddingException, NoSuchFieldException, JsonProcessingException {
        WebResource.Builder builder = TestsHelpers.mockWebBuilder(kushki, Kushki.TOKENS_URL);
        Map<String, String> cardParams = TestsHelpers.getCardData();
        kushki.requestToken(cardParams);
        verify(builder).post(eq(ClientResponse.class), any(Map.class));
    }

    @Test
    public void shouldSendRightParametersToRequestToken() throws IllegalBlockSizeException, IllegalAccessException, BadPaddingException, NoSuchFieldException, IOException {
        Map<String, String> cardParams = TestsHelpers.getCardData();
        ObjectMapper mapper = new ObjectMapper();
        String params = mapper.writeValueAsString(cardParams);

        AurusEncryption encryption = mock(AurusEncryption.class);
        String encrypted = randomAlphabetic(10);
        TestsHelpers.mockEncryption(kushki, encryption, encrypted);
        WebResource.Builder builder = TestsHelpers.mockWebBuilder(kushki, Kushki.TOKENS_URL);
        kushki.requestToken(cardParams);

        ArgumentCaptor<Map> encryptedParams = ArgumentCaptor.forClass(Map.class);
        ArgumentCaptor<String> unencryptedParams = ArgumentCaptor.forClass(String.class);

        verify(builder).post(eq(ClientResponse.class), encryptedParams.capture());
        Map<String, String> parameters = encryptedParams.getValue();
        assertThat(parameters.get("request"), is(encrypted));

        verify(encryption).encryptMessageChunk(unencryptedParams.capture());
        parameters = new ObjectMapper().readValue(unencryptedParams.getValue(), Map.class);
        assertThat(parameters.get("card"), is(params));
    }

    @Test
    public void shouldReturnTransactionObjectAfterGettingToken() throws NoSuchFieldException, IllegalAccessException, IllegalBlockSizeException, KushkiException, BadPaddingException, JsonProcessingException {
        WebResource.Builder builder = TestsHelpers.mockClient(kushki, Kushki.TOKENS_URL);
        ClientResponse response = mock(ClientResponse.class);
        when(builder.post(eq(ClientResponse.class), any())).thenReturn(response);
        Map<String, String> cardParams = TestsHelpers.getCardData();
        Transaction transaction = kushki.requestToken(cardParams);
        assertThat(transaction.getResponse(), is(response));
    }

    @Test
    public void shouldChargeACardWithToken() throws NoSuchFieldException, IllegalAccessException, JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        String token = randomAlphabetic(10);
        Double amount = TestsHelpers.getRandomAmount();
        WebResource.Builder builder = TestsHelpers.mockWebBuilder(kushki, Kushki.CHARGE_URL);
        kushki.charge(token, amount);
        verify(builder).post(eq(ClientResponse.class), any(Map.class));
    }

    @Test
    public void shouldSendRightParametersToChargeCard() throws NoSuchFieldException, IllegalAccessException, IOException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        String token = randomAlphabetic(10);
        Double amount = TestsHelpers.getRandomAmount();

        AurusEncryption encryption = mock(AurusEncryption.class);
        String encrypted = randomAlphabetic(10);
        TestsHelpers.mockEncryption(kushki, encryption, encrypted);
        WebResource.Builder builder = TestsHelpers.mockWebBuilder(kushki, Kushki.CHARGE_URL);
        kushki.charge(token, amount);

        ArgumentCaptor<Map> encryptedParams = ArgumentCaptor.forClass(Map.class);
        ArgumentCaptor<String> unencryptedParams = ArgumentCaptor.forClass(String.class);

        verify(builder).post(eq(ClientResponse.class), encryptedParams.capture());
        Map<String, String> parameters = encryptedParams.getValue();
        assertThat(parameters.get("request"), is(encrypted));

        verify(encryption).encryptMessageChunk(unencryptedParams.capture());
        parameters = new ObjectMapper().readValue(unencryptedParams.getValue(), Map.class);
        assertThat(parameters.get("transaction_token"), is(token));
        assertThat(parameters.get("transaction_amount"), is(String.format("%.2f", amount)));
    }

    @Test
    public void shouldReturnTransactionObjectAfterChargingCard() throws NoSuchFieldException, IllegalAccessException, JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        String token = randomAlphabetic(10);
        Double amount = TestsHelpers.getRandomAmount();
        WebResource.Builder builder = TestsHelpers.mockClient(kushki, Kushki.CHARGE_URL);
        ClientResponse response = mock(ClientResponse.class);
        when(builder.post(eq(ClientResponse.class), any())).thenReturn(response);
        Transaction transaction = kushki.charge(token, amount);
        assertThat(transaction.getResponse(), is(response));
    }

    @Test
    public void shouldVoidChargeWithTokenAndTicket() throws IllegalBlockSizeException, IllegalAccessException, BadPaddingException, NoSuchFieldException, KushkiException, JsonProcessingException {
        String ticket = randomAlphabetic(10);
        Double amount = TestsHelpers.getRandomAmount();
        WebResource.Builder builder = TestsHelpers.mockWebBuilder(kushki, Kushki.VOID_URL);
        kushki.voidCharge(ticket, amount);
        verify(builder).post(eq(ClientResponse.class), any(Map.class));
    }

    @Test
    public void shouldSendRightParametersToVoidCharge() throws NoSuchFieldException, IllegalAccessException, IOException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        String ticket = randomAlphabetic(10);
        Double amount = TestsHelpers.getRandomAmount();
        AurusEncryption encryption = mock(AurusEncryption.class);
        String encrypted = randomAlphabetic(10);
        TestsHelpers.mockEncryption(kushki, encryption, encrypted);
        WebResource.Builder builder = TestsHelpers.mockWebBuilder(kushki, Kushki.VOID_URL);
        kushki.voidCharge(ticket, amount);

        ArgumentCaptor<Map> encryptedParams = ArgumentCaptor.forClass(Map.class);
        ArgumentCaptor<String> unencryptedParams = ArgumentCaptor.forClass(String.class);

        verify(builder).post(eq(ClientResponse.class), encryptedParams.capture());
        Map<String, String> parameters = encryptedParams.getValue();
        assertThat(parameters.get("request"), is(encrypted));

        verify(encryption).encryptMessageChunk(unencryptedParams.capture());
        parameters = new ObjectMapper().readValue(unencryptedParams.getValue(), Map.class);
        assertThat(parameters.get("ticket_number"), is(ticket));
        assertThat(parameters.get("transaction_amount"), is(String.format("%.2f", amount)));
    }

    @Test
    public void shouldReturnTransactionObjectAfterVoidingCharge() throws NoSuchFieldException, IllegalAccessException, JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        String ticket = randomAlphabetic(10);
        Double amount = TestsHelpers.getRandomAmount();
        WebResource.Builder builder = TestsHelpers.mockClient(kushki, Kushki.VOID_URL);
        ClientResponse response = mock(ClientResponse.class);
        when(builder.post(eq(ClientResponse.class), any())).thenReturn(response);
        Transaction transaction = kushki.voidCharge(ticket, amount);
        assertThat(transaction.getResponse(), is(response));
    }

    @Test
    public void shouldRefundChargeWithTicket() throws IllegalBlockSizeException, IllegalAccessException, BadPaddingException, NoSuchFieldException, KushkiException, JsonProcessingException {
        String ticket = randomAlphabetic(10);
        Double amount = TestsHelpers.getRandomAmount();
        WebResource.Builder builder = TestsHelpers.mockWebBuilder(kushki, Kushki.REFUND_URL);
        kushki.refundCharge(ticket, amount);
        verify(builder).post(eq(ClientResponse.class), any(Map.class));
    }

    @Test
    public void shouldSendRightParametersToRefundCharge() throws NoSuchFieldException, IllegalAccessException, IOException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        String ticket = randomAlphabetic(10);
        Double amount = TestsHelpers.getRandomAmount();
        AurusEncryption encryption = mock(AurusEncryption.class);
        String encrypted = randomAlphabetic(10);
        TestsHelpers.mockEncryption(kushki, encryption, encrypted);
        WebResource.Builder builder = TestsHelpers.mockWebBuilder(kushki, Kushki.REFUND_URL);
        kushki.refundCharge(ticket, amount);

        ArgumentCaptor<Map> encryptedParams = ArgumentCaptor.forClass(Map.class);
        ArgumentCaptor<String> unencryptedParams = ArgumentCaptor.forClass(String.class);

        verify(builder).post(eq(ClientResponse.class), encryptedParams.capture());
        Map<String, String> parameters = encryptedParams.getValue();
        assertThat(parameters.get("request"), is(encrypted));

        verify(encryption).encryptMessageChunk(unencryptedParams.capture());
        parameters = new ObjectMapper().readValue(unencryptedParams.getValue(), Map.class);
        assertThat(parameters.get("ticket_number"), is(ticket));
        assertThat(parameters.get("transaction_amount"), is(String.format("%.2f", amount)));
    }

    @Test
    public void shouldReturnTransactionObjectAfterRefundingCharge() throws NoSuchFieldException, IllegalAccessException, JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        String ticket = randomAlphabetic(10);
        Double amount = TestsHelpers.getRandomAmount();
        WebResource.Builder builder = TestsHelpers.mockClient(kushki, Kushki.REFUND_URL);
        ClientResponse response = mock(ClientResponse.class);
        when(builder.post(eq(ClientResponse.class), any())).thenReturn(response);
        Transaction transaction = kushki.refundCharge(ticket, amount);
        assertThat(transaction.getResponse(), is(response));
    }

    @Test
    public void shouldThrowKushkiExceptionIfAmountHasMoreThan12Characters() throws KushkiException, BadPaddingException, IllegalBlockSizeException, JsonProcessingException {
        Double amount = 321381238123123123.0;
        TestsHelpers.assertThatChargeThrowsExceptionWithInvalidAmount(kushki, amount, "El monto debe tener menos de 12 dígitos");
    }

    @Test
    public void shouldThrowKushkiExceptionIfAmountIs0() throws KushkiException, BadPaddingException, IllegalBlockSizeException, JsonProcessingException {
        TestsHelpers.assertThatChargeThrowsExceptionWithInvalidAmount(kushki, 0.0, "El monto debe ser superior a 0");
    }

    @Test
    public void shouldThrowKushkiExceptionIfAmountIsNegative() throws KushkiException, BadPaddingException, IllegalBlockSizeException, JsonProcessingException {
        TestsHelpers.assertThatChargeThrowsExceptionWithInvalidAmount(kushki, -5.0, "El monto debe ser superior a 0");
    }

    @Test
    public void shouldThrowKushkiExceptionIfAmountIsNull() throws KushkiException, BadPaddingException, IllegalBlockSizeException, JsonProcessingException {
        TestsHelpers.assertThatChargeThrowsExceptionWithInvalidAmount(kushki, null, "El monto no puede ser nulo");
    }
}
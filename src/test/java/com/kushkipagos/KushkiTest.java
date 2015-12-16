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
import static org.apache.commons.lang3.RandomUtils.nextDouble;
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
        assertThat(Kushki.URL, is("https://ping.auruspay.com/kushki/api/v1/charge"));
    }

    @Test
    public void shouldCreateInstanceWithLanguage() {
        assertThat(kushki.getLanguage(), is(language));
    }

    @Test
    public void shouldChargeACardWithToken() throws NoSuchFieldException, IllegalAccessException, JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        String token = randomAlphabetic(10);
        Double amount = Double.parseDouble(String.format("%.2f", nextDouble(1, 99)));
        WebResource.Builder builder = TestsHelpers.mockClient(kushki);
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
        WebResource.Builder builder = TestsHelpers.mockClient(kushki);
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
        WebResource.Builder builder = TestsHelpers.mockClient(kushki);
        ClientResponse response = mock(ClientResponse.class);
        when(builder.post(eq(ClientResponse.class), any())).thenReturn(response);
        Transaction transaction = kushki.charge(token, amount);
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
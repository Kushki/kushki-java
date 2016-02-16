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

public class KushkiApiRefundChargeTest {
    private Kushki kushki;

    @Before
    public void setUp() throws Exception {
        String merchantId = randomAlphabetic(10);
        String language = randomAlphabetic(2);
        String currency = randomAlphabetic(10);
        kushki = new Kushki(merchantId, language, currency);
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
}
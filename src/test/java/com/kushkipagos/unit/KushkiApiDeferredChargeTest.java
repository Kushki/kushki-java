package com.kushkipagos.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kushkipagos.Kushki;
import com.kushkipagos.KushkiException;
import com.kushkipagos.Transaction;
import com.kushkipagos.AurusEncryption;
import com.kushkipagos.commons.TestsHelpers;
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

/**
 * Created by lmunda on 2/16/16 12:13.
 */
public class KushkiApiDeferredChargeTest {
    private Kushki kushki;

    @Before
    public void setUp() throws Exception {
        String merchantId = randomAlphabetic(10);
        String language = randomAlphabetic(2);
        String currency = randomAlphabetic(10);
        kushki = new Kushki(merchantId, language, currency);
    }


    @Test
    public void shouldSendRightParametersToDeferredChargeCard() throws NoSuchFieldException, IllegalAccessException, IOException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        String token = randomAlphabetic(10);
        Double amount = TestsHelpers.getRandomAmount();
        Integer months = TestsHelpers.getRandomMonths();
        Double interest = TestsHelpers.getRandomInterest();

        AurusEncryption encryption = mock(AurusEncryption.class);
        String encrypted = randomAlphabetic(10);
        UnitTestsHelpers.mockEncryption(kushki, encryption, encrypted);
        WebResource.Builder builder = UnitTestsHelpers.mockWebBuilder(kushki, Kushki.DEFERRED_CHARGE_URL);
        kushki.deferredCharge(token, amount, months, interest);

        ArgumentCaptor<Map> encryptedParams = ArgumentCaptor.forClass(Map.class);
        ArgumentCaptor<String> unencryptedParams = ArgumentCaptor.forClass(String.class);

        verify(builder).post(eq(ClientResponse.class), encryptedParams.capture());
        Map<String, String> parameters = encryptedParams.getValue();
        assertThat(parameters.get("request"), is(encrypted));

        verify(encryption).encryptMessageChunk(unencryptedParams.capture());
        parameters = new ObjectMapper().readValue(unencryptedParams.getValue(), Map.class);
        assertThat(parameters.get("transaction_token"), is(token));
        assertThat(parameters.get("transaction_amount"), is(String.format("%.2f", amount)));
        assertThat(parameters.get("months"), is(String.valueOf(months)));
        assertThat(parameters.get("rate_of_interest"), is(String.format("%.2f", interest)));
    }

    @Test
    public void shouldReturnTransactionObjectAfterDeferredChargingCard() throws NoSuchFieldException, IllegalAccessException, JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        String token = randomAlphabetic(10);
        Double amount = TestsHelpers.getRandomAmount();
        Integer months = TestsHelpers.getRandomMonths();
        Double interest = TestsHelpers.getRandomInterest();
        WebResource.Builder builder = UnitTestsHelpers.mockClient(kushki, Kushki.DEFERRED_CHARGE_URL);
        ClientResponse response = mock(ClientResponse.class);
        when(builder.post(eq(ClientResponse.class), any())).thenReturn(response);
        Transaction transaction = kushki.deferredCharge(token, amount, months, interest);
        assertThat(transaction.getResponse(), is(response));
    }

}

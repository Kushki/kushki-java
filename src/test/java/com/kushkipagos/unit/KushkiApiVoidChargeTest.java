package com.kushkipagos.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kushkipagos.Kushki;
import com.kushkipagos.KushkiException;
import com.kushkipagos.Transaction;
import com.kushkipagos.AurusEncryption;
import com.kushkipagos.commons.TestsHelpers;
//import com.sun.jersey.api.client.ClientResponse;
//import com.sun.jersey.api.client.WebResource;
import org.junit.Before;
import org.junit.Ignore;
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
 * Created by lmunda on 2/16/16 12:14.
 */
public class KushkiApiVoidChargeTest {

    private Kushki kushki;

    @Before
    public void setUp() throws Exception {
        String merchantId = randomAlphabetic(10);
        String language = randomAlphabetic(2);
        String currency = randomAlphabetic(10);
        kushki = new Kushki(merchantId, language, currency);
    }

    @Ignore("Test is ignored: working on charge method")
    @Test
    public void shouldVoidChargeWithTokenAndTicket() throws IllegalBlockSizeException, IllegalAccessException, BadPaddingException, NoSuchFieldException, KushkiException, JsonProcessingException {
        String ticket = randomAlphabetic(10);
        Double amount = TestsHelpers.getRandomDoubleAmount();
//        WebResource.Builder builder = UnitTestsHelpers.mockWebBuilder(kushki, Kushki.VOID_URL);
//        kushki.voidCharge(ticket, amount);
//        verify(builder).post(eq(ClientResponse.class), any(Map.class));
    }

    @Ignore("Test is ignored: working on charge method")
    @Test
    public void shouldSendRightParametersToVoidCharge() throws NoSuchFieldException, IllegalAccessException, IOException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        String ticket = randomAlphabetic(10);
        Double amount = TestsHelpers.getRandomDoubleAmount();
        AurusEncryption encryption = mock(AurusEncryption.class);
        String encrypted = randomAlphabetic(10);
//        UnitTestsHelpers.mockEncryption(kushki, encryption, encrypted);
//        WebResource.Builder builder = UnitTestsHelpers.mockWebBuilder(kushki, Kushki.VOID_URL);
//        kushki.voidCharge(ticket, amount);
//
//        ArgumentCaptor<Map> encryptedParams = ArgumentCaptor.forClass(Map.class);
//        ArgumentCaptor<String> unencryptedParams = ArgumentCaptor.forClass(String.class);
//
//        verify(builder).post(eq(ClientResponse.class), encryptedParams.capture());
//        Map<String, String> parameters = encryptedParams.getValue();
//        assertThat(parameters.get("request"), is(encrypted));
//
//        verify(encryption).encryptMessageChunk(unencryptedParams.capture());
//        parameters = new ObjectMapper().readValue(unencryptedParams.getValue(), Map.class);
//        assertThat(parameters.get("ticket_number"), is(ticket));
//        assertThat(parameters.get("transaction_amount"), is(String.format("%.2f", amount)));
    }

    @Ignore("Test is ignored: working on charge method")
    @Test
    public void shouldReturnTransactionObjectAfterVoidingCharge() throws NoSuchFieldException, IllegalAccessException, JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        String ticket = randomAlphabetic(10);
        Double amount = TestsHelpers.getRandomDoubleAmount();
//        WebResource.Builder builder = UnitTestsHelpers.mockClient(kushki, Kushki.VOID_URL);
//        ClientResponse response = mock(ClientResponse.class);
//        when(builder.post(eq(ClientResponse.class), any())).thenReturn(response);
//        Transaction transaction = kushki.voidCharge(ticket, amount);
//        assertThat(transaction.getResponse(), is(response));
    }

}

package com.kushkipagos.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kushkipagos.*;
import com.kushkipagos.commons.TestsHelpers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Map;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
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
        kushki = new Kushki(merchantId, language, currency, KushkiEnvironment.TESTING);
    }

    @Test
    public void shouldVoidChargeWithTokenAndTicket() throws IllegalBlockSizeException, IllegalAccessException, BadPaddingException, NoSuchFieldException, KushkiException, JsonProcessingException {
        String ticket = randomAlphabetic(10);
        Amount amount = TestsHelpers.getRandomAmount();
        Invocation.Builder invocationBuilder = UnitTestsHelpers.mockInvocationBuilder(kushki, KushkiEnvironment.TESTING.getUrl(), Kushki.VOID_URL);
        kushki.voidCharge(ticket, amount);
        verify(invocationBuilder).post(any(Entity.class));
    }

    @Test
    public void shouldSendRightParametersToVoidCharge() throws NoSuchFieldException, IllegalAccessException, IOException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        String ticket = randomAlphabetic(10);
        Amount amount = TestsHelpers.getRandomAmount();
        String stringifiedAmount = new ObjectMapper().writeValueAsString(amount.toHash());

        AurusEncryption encryption = mock(AurusEncryption.class);
        String encrypted = randomAlphabetic(10);
        UnitTestsHelpers.mockEncryption(kushki, encryption, encrypted);
        Invocation.Builder invocationBuilder = UnitTestsHelpers.mockInvocationBuilder(kushki,KushkiEnvironment.TESTING.getUrl(),  Kushki.VOID_URL);
        kushki.voidCharge(ticket, amount);

        ArgumentCaptor<Entity> entityArgumentCaptor = ArgumentCaptor.forClass(Entity.class);
        ArgumentCaptor<String> unencryptedParamsArgumentCaptor = ArgumentCaptor.forClass(String.class);

        verify(invocationBuilder).post(entityArgumentCaptor.capture());
        Entity<Map<String, String>> entity = entityArgumentCaptor.getValue();
        Map<String, String> parameters = entity.getEntity();
        assertThat(parameters.get("request"), is(encrypted));

        verify(encryption).encryptMessageChunk(unencryptedParamsArgumentCaptor.capture());
        parameters = new ObjectMapper().readValue(unencryptedParamsArgumentCaptor.getValue(), Map.class);
        assertThat(parameters.get("ticket_number"), is(ticket));
        assertThat(parameters.get("transaction_amount"), is(stringifiedAmount));
    }

    @Test
    public void shouldReturnTransactionObjectAfterVoidingCharge() throws NoSuchFieldException, IllegalAccessException, JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        String ticket = randomAlphabetic(10);
        Amount amount = TestsHelpers.getRandomAmount();

        Invocation.Builder invocationBuilder = UnitTestsHelpers.mockClient(kushki, KushkiEnvironment.TESTING.getUrl(), Kushki.VOID_URL);

        Response response = mock(Response.class);
        when(invocationBuilder.post(any(Entity.class))).thenReturn(response);
        Transaction transaction = kushki.voidCharge(ticket, amount);
        assertThat(transaction.getResponse(), is(response));
    }

}

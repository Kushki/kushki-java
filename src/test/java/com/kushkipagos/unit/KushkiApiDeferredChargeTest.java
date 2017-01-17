package com.kushkipagos.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kushkipagos.Amount;
import com.kushkipagos.AurusEncryption;
import com.kushkipagos.Kushki;
import com.kushkipagos.KushkiEnvironment;
import com.kushkipagos.KushkiException;
import com.kushkipagos.Transaction;
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

public class KushkiApiDeferredChargeTest {
    private Kushki kushki;

    @Before
    public void setUp() throws Exception {
        String merchantId = randomAlphabetic(10);
        String language = randomAlphabetic(2);
        String currency = randomAlphabetic(10);
        kushki = new Kushki(merchantId, language, currency, KushkiEnvironment.TESTING);
    }

    @Test
    public void shouldSendRightParametersToDeferredChargeCard() throws NoSuchFieldException, IllegalAccessException, IOException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        String token = randomAlphabetic(10);
        Amount amount = TestsHelpers.getRandomAmount();
        Integer months = TestsHelpers.getRandomMonths();
        String stringifiedAmount = new ObjectMapper().writeValueAsString(amount.toHash());

        AurusEncryption encryption = mock(AurusEncryption.class);
        String encrypted = randomAlphabetic(10);
        UnitTestsHelpers.mockEncryption(kushki, encryption, encrypted);
        Invocation.Builder invocationBuilder = UnitTestsHelpers.mockInvocationBuilder(kushki, KushkiEnvironment.TESTING.getUrl(), Kushki.DEFERRED_CHARGE_URL);
        kushki.deferredCharge(token, amount, months);

        ArgumentCaptor<Entity> entityArgumentCaptor = ArgumentCaptor.forClass(Entity.class);
        ArgumentCaptor<String> unencryptedParamsArgumentCaptor = ArgumentCaptor.forClass(String.class);

        verify(invocationBuilder).post(entityArgumentCaptor.capture());
        Entity<Map<String, String>> entity = entityArgumentCaptor.getValue();
        Map<String, String> parameters = entity.getEntity();
        assertThat(parameters.get("request"), is(encrypted));

        verify(encryption).encryptMessageChunk(unencryptedParamsArgumentCaptor.capture());
        parameters = new ObjectMapper().readValue(unencryptedParamsArgumentCaptor.getValue(), Map.class);
        assertThat(parameters.get("transaction_token"), is(token));
        assertThat(parameters.get("transaction_amount"), is(stringifiedAmount));
        assertThat(parameters.get("months"), is(String.valueOf(months)));
    }

    @Test
    public void shouldSendRightParametersToDeferredChargeCardColombia() throws NoSuchFieldException, IllegalAccessException, IOException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        String token = randomAlphabetic(10);
        Amount amount = TestsHelpers.getRandomAmountColombia();
        Integer months = TestsHelpers.getRandomMonthsColombia();
        String stringifiedAmount = new ObjectMapper().writeValueAsString(amount.toHashColombia());

        AurusEncryption encryption = mock(AurusEncryption.class);
        String encrypted = randomAlphabetic(10);
        UnitTestsHelpers.mockEncryption(kushki, encryption, encrypted);
        Invocation.Builder invocationBuilder = UnitTestsHelpers.mockInvocationBuilder(kushki, KushkiEnvironment.TESTING.getUrl(), Kushki.DEFERRED_CHARGE_URL);
        kushki.deferredChargeColombia(token, amount, months);

        ArgumentCaptor<Entity> entityArgumentCaptor = ArgumentCaptor.forClass(Entity.class);
        ArgumentCaptor<String> unencryptedParamsArgumentCaptor = ArgumentCaptor.forClass(String.class);

        verify(invocationBuilder).post(entityArgumentCaptor.capture());
        Entity<Map<String, String>> entity = entityArgumentCaptor.getValue();
        Map<String, String> parameters = entity.getEntity();
        assertThat(parameters.get("request"), is(encrypted));

        verify(encryption).encryptMessageChunk(unencryptedParamsArgumentCaptor.capture());
        parameters = new ObjectMapper().readValue(unencryptedParamsArgumentCaptor.getValue(), Map.class);
        assertThat(parameters.get("transaction_token"), is(token));
        assertThat(parameters.get("transaction_amount"), is(stringifiedAmount));
        assertThat(parameters.get("months"), is(String.valueOf(months)));
    }

    @Test
    public void shouldReturnTransactionObjectAfterDeferredChargingCard() throws NoSuchFieldException, IllegalAccessException, JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        String token = randomAlphabetic(10);
        Amount amount = TestsHelpers.getRandomAmount();
        Integer months = TestsHelpers.getRandomMonths();
        Invocation.Builder invocationBuilder = UnitTestsHelpers.mockClient(kushki,KushkiEnvironment.TESTING.getUrl(),  Kushki.DEFERRED_CHARGE_URL);
        Response response = mock(Response.class);
        when(invocationBuilder.post(any(Entity.class))).thenReturn(response);
        Transaction transaction = kushki.deferredCharge(token, amount, months);
        assertThat(transaction.getResponse(), is(response));
    }

    @Test
    public void shouldReturnTransactionObjectAfterDeferredChargingCardColombia() throws NoSuchFieldException, IllegalAccessException, JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        String token = randomAlphabetic(10);
        Amount amount = TestsHelpers.getRandomAmountColombia();
        Integer months = TestsHelpers.getRandomMonthsColombia();
        Invocation.Builder invocationBuilder = UnitTestsHelpers.mockClient(kushki,KushkiEnvironment.TESTING.getUrl(),  Kushki.DEFERRED_CHARGE_URL);
        Response response = mock(Response.class);
        when(invocationBuilder.post(any(Entity.class))).thenReturn(response);
        Transaction transaction = kushki.deferredChargeColombia(token, amount, months);
        assertThat(transaction.getResponse(), is(response));
    }

}

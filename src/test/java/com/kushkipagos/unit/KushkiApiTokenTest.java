package com.kushkipagos.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kushkipagos.AurusEncryption;
import com.kushkipagos.Card;
import com.kushkipagos.Kushki;
import com.kushkipagos.KushkiEnvironment;
import com.kushkipagos.Transaction;
import com.kushkipagos.commons.TestsHelpers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Response;
import java.util.Map;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class KushkiApiTokenTest {
    private static final String BASE_URL = KushkiEnvironment.TESTING.getUrl();
    private Kushki kushki;

    @Before
    public void setUp() throws Exception {
        String merchantId = randomAlphabetic(10);
        String language = randomAlphabetic(2);
        String currency = randomAlphabetic(10);
        kushki = new Kushki(merchantId, language, currency, KushkiEnvironment.TESTING);
    }

    @Test
    public void shouldSendRightParametersToRequestToken() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Card card = TestsHelpers.getValidCard();
        String stringifiedCard = objectMapper.writeValueAsString(card);
        Double totalAmount = 4d;
        String stringifiedAmount = "4.00";
        AurusEncryption encryption = mock(AurusEncryption.class);
        String encryptedParams = randomAlphabetic(10);
        ArgumentCaptor<Entity> entityArgumentCaptor = ArgumentCaptor.forClass(Entity.class);
        ArgumentCaptor<String> unencryptedParamsArgumentCaptor = ArgumentCaptor.forClass(String.class);
        UnitTestsHelpers.mockEncryption(kushki, encryption, encryptedParams);
        Invocation.Builder invocationBuilder = UnitTestsHelpers.mockInvocationBuilder(kushki, BASE_URL, Kushki.TOKENS_URL);

        kushki.requestToken(card, totalAmount);

        verify(invocationBuilder).post(entityArgumentCaptor.capture());
        Entity<Map<String, String>> entity = entityArgumentCaptor.getValue();
        Map<String, String> parameters = entity.getEntity();
        assertThat(parameters.get("request"), is(encryptedParams));
        verify(encryption).encryptMessageChunk(unencryptedParamsArgumentCaptor.capture());
        parameters = objectMapper.readValue(unencryptedParamsArgumentCaptor.getValue(), Map.class);
        assertThat(parameters.get("card"), is(stringifiedCard));
        assertThat(parameters.get("amount"), is(stringifiedAmount));
    }

    @Test
    public void shouldSendRightParametersToRequestTokenColombia() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Card card = TestsHelpers.getValidCard();
        String stringifiedCard = objectMapper.writeValueAsString(card);
        Double totalAmount = 3200d;
        String stringifiedAmount = "3200.00";
        AurusEncryption encryption = mock(AurusEncryption.class);
        String encryptedParams = randomAlphabetic(10);
        ArgumentCaptor<Entity> entityArgumentCaptor = ArgumentCaptor.forClass(Entity.class);
        ArgumentCaptor<String> unencryptedParamsArgumentCaptor = ArgumentCaptor.forClass(String.class);
        UnitTestsHelpers.mockEncryption(kushki, encryption, encryptedParams);
        Invocation.Builder invocationBuilder = UnitTestsHelpers.mockInvocationBuilder(kushki, BASE_URL, Kushki.TOKENS_URL);

        kushki.requestTokenColombia(card, totalAmount);

        verify(invocationBuilder).post(entityArgumentCaptor.capture());
        Entity<Map<String, String>> entity = entityArgumentCaptor.getValue();
        Map<String, String> parameters = entity.getEntity();
        assertThat(parameters.get("request"), is(encryptedParams));
        verify(encryption).encryptMessageChunk(unencryptedParamsArgumentCaptor.capture());
        parameters = objectMapper.readValue(unencryptedParamsArgumentCaptor.getValue(), Map.class);
        assertThat(parameters.get("card"), is(stringifiedCard));
        assertThat(parameters.get("amount"), is(stringifiedAmount));
    }

    @Test
    public void shouldReturnTransactionObjectAfterGettingToken() throws Exception {
        Invocation.Builder builder = UnitTestsHelpers.mockClient(kushki, BASE_URL, Kushki.TOKENS_URL);
        Response response = mock(Response.class);
        when(builder.post(any(Entity.class))).thenReturn(response);
        Card card = TestsHelpers.getValidCard();
        Transaction transaction = kushki.requestToken(card, 4d);
        assertThat(transaction.getResponse(), is(response));
    }

    @Test
    public void shouldReturnTransactionObjectAfterGettingTokenColombia() throws Exception {
        Invocation.Builder builder = UnitTestsHelpers.mockClient(kushki, BASE_URL, Kushki.TOKENS_URL);
        Response response = mock(Response.class);
        when(builder.post(any(Entity.class))).thenReturn(response);
        Card card = TestsHelpers.getValidCard();
        Transaction transaction = kushki.requestTokenColombia(card, 3200d);
        assertThat(transaction.getResponse(), is(response));
    }
}

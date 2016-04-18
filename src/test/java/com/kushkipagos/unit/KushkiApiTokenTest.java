package com.kushkipagos.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kushkipagos.Kushki;
import com.kushkipagos.KushkiException;
import com.kushkipagos.Transaction;
import com.kushkipagos.AurusEncryption;
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
 * Created by lmunda on 2/16/16 12:11.
 */
public class KushkiApiTokenTest {
    private Kushki kushki;

    @Before
    public void setUp() throws Exception {
        String merchantId = randomAlphabetic(10);
        String language = randomAlphabetic(2);
        String currency = randomAlphabetic(10);
        kushki = new Kushki(merchantId, language, currency);
    }

    @Test
    public void shouldRequestToken() throws IllegalBlockSizeException, IllegalAccessException, BadPaddingException, NoSuchFieldException, JsonProcessingException {
        Invocation.Builder invocationBuilder = UnitTestsHelpers.mockInvocationBuilder(kushki, Kushki.BASE_URL, Kushki.TOKENS_URL);
        Map<String, String> cardParams = TestsHelpers.getValidCardData();
        kushki.requestToken(cardParams);
        verify(invocationBuilder).post(any(Entity.class));
    }

    @Test
    public void shouldSendRightParametersToRequestToken() throws IllegalBlockSizeException, IllegalAccessException, BadPaddingException, NoSuchFieldException, IOException {
        Map<String, String> cardParams = TestsHelpers.getValidCardData();
        ObjectMapper mapper = new ObjectMapper();
        String params = mapper.writeValueAsString(cardParams);

        AurusEncryption encryption = mock(AurusEncryption.class);
        String encrypted = randomAlphabetic(10);
        UnitTestsHelpers.mockEncryption(kushki, encryption, encrypted);
        Invocation.Builder invocationBuilder = UnitTestsHelpers.mockInvocationBuilder(kushki, Kushki.BASE_URL, Kushki.TOKENS_URL);
        kushki.requestToken(cardParams);

        ArgumentCaptor<Entity> entityArgumentCaptor = ArgumentCaptor.forClass(Entity.class);
        ArgumentCaptor<String> unencryptedParamsArgumentCaptor = ArgumentCaptor.forClass(String.class);

        verify(invocationBuilder).post(entityArgumentCaptor.capture());
        Entity<Map<String, String>> entity = entityArgumentCaptor.getValue();
        Map<String, String> parameters = entity.getEntity();
        assertThat(parameters.get("request"), is(encrypted));

        verify(encryption).encryptMessageChunk(unencryptedParamsArgumentCaptor.capture());
        parameters = new ObjectMapper().readValue(unencryptedParamsArgumentCaptor.getValue(), Map.class);
        assertThat(parameters.get("card"), is(params));
    }

    @Test
    public void shouldReturnTransactionObjectAfterGettingToken() throws NoSuchFieldException, IllegalAccessException, IllegalBlockSizeException, KushkiException, BadPaddingException, JsonProcessingException {
        Invocation.Builder builder = UnitTestsHelpers.mockClient(kushki, Kushki.BASE_URL, Kushki.TOKENS_URL);
        Response response = mock(Response.class);
        when(builder.post(any(Entity.class))).thenReturn(response);
        Map<String, String> cardParams = TestsHelpers.getValidCardData();
        Transaction transaction = kushki.requestToken(cardParams);
        assertThat(transaction.getResponse(), is(response));
    }


}

package com.kushkipagos;

import com.fasterxml.jackson.databind.JsonNode;
import com.sun.jersey.api.client.ClientResponse;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TransactionTest {
    private ClientResponse response;
    private Transaction transaction;

    @Before
    public void setUp() throws Exception {
        response = mock(ClientResponse.class);
        transaction = new Transaction(response);
    }

    @Test
    public void shouldHaveHttpResponse() {
        assertThat(transaction.getResponse(), is(response));
    }

    @Test
    public void shouldReturnTrueIfResponseCodeIs200() {
        when(response.getStatus()).thenReturn(200);
        assertThat(transaction.isSuccessful(), is(true));
    }

    @Test
    public void shouldReturnFalseIfResponseCodeIsNot200() {
        when(response.getStatus()).thenReturn(402);
        assertThat(transaction.isSuccessful(), is(false));
    }

    @Test
    public void shouldReturnResponseBodyAsJsonObject() {
        JsonNode json = mock(JsonNode.class);
        when(response.getEntity(JsonNode.class)).thenReturn(json);
        assertThat(transaction.getResponseBody(), is(json));
    }
}
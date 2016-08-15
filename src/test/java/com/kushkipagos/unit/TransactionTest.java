package com.kushkipagos.unit;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kushkipagos.Transaction;
import com.kushkipagos.commons.TestsHelpers;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TransactionTest {
    private Response response;
    private Transaction transaction;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Before
    public void setUp() throws Exception {
        response = mock(Response.class);
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
        when(response.readEntity(JsonNode.class)).thenReturn(json);
        transaction = new Transaction(response);
        assertThat(transaction.getResponseBody(), is(json));
    }

    @Test
    public void shouldReturnTicketNumber() throws IOException {
        Map<String, String> jsonMap = new HashMap<>(1);
        String ticket_number = randomAlphabetic(10);
        jsonMap.put("ticket_number", ticket_number);
        JsonNode node = OBJECT_MAPPER.readValue(OBJECT_MAPPER.writeValueAsString(jsonMap), JsonNode.class);
        when(response.readEntity(JsonNode.class)).thenReturn(node);
        transaction = new Transaction(response);
        assertThat(transaction.getTicketNumber(), is(ticket_number));
    }

    @Test
    public void shouldReturnResponseText() throws IOException {
        Map<String, String> jsonMap = new HashMap<>(1);
        String response_text = randomAlphabetic(10);
        jsonMap.put("response_text", response_text);
        JsonNode node = OBJECT_MAPPER.readValue(OBJECT_MAPPER.writeValueAsString(jsonMap), JsonNode.class);
        when(response.readEntity(JsonNode.class)).thenReturn(node);
        transaction = new Transaction(response);
        assertThat(transaction.getResponseText(), is(response_text));
    }

    @Test
    public void shouldReturnToken() throws IOException {
        Map<String, String> jsonMap = new HashMap<>(1);
        String token = randomAlphabetic(10);
        jsonMap.put("transaction_token", token);
        JsonNode node = OBJECT_MAPPER.readValue(OBJECT_MAPPER.writeValueAsString(jsonMap), JsonNode.class);
        when(response.readEntity(JsonNode.class)).thenReturn(node);
        transaction = new Transaction(response);
        assertThat(transaction.getToken(), is(token));
    }

    @Test
    public void shouldReturnApprovedAmount() throws IOException {
        Map<String, String> jsonMap = new HashMap<>(1);
        Double amount = TestsHelpers.getRandomDoubleAmount();
        jsonMap.put("approved_amount", String.valueOf(amount));
        JsonNode node = OBJECT_MAPPER.readValue(OBJECT_MAPPER.writeValueAsString(jsonMap), JsonNode.class);
        when(response.readEntity(JsonNode.class)).thenReturn(node);
        transaction = new Transaction(response);
        assertThat(transaction.getApprovedAmount(), is(amount));
    }

    @Test
    public void shouldReturnResponseCode() throws IOException {
        Map<String, String> jsonMap = new HashMap<>(1);
        String responseCode = randomAlphabetic(3);
        jsonMap.put("response_code", responseCode);
        JsonNode node = OBJECT_MAPPER.readValue(OBJECT_MAPPER.writeValueAsString(jsonMap), JsonNode.class);
        when(response.readEntity(JsonNode.class)).thenReturn(node);
        transaction = new Transaction(response);
        assertThat(transaction.getResponseCode(), is(responseCode));
    }
}
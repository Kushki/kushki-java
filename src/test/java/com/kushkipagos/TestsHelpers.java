package com.kushkipagos;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.junit.Assert;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.ws.rs.core.MediaType;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomUtils.nextDouble;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by lmunda on 12/16/15 10:22.
 */
public final class TestsHelpers {

    private TestsHelpers() {

    }

    public static void mockEncryption(Kushki kushki, AurusEncryption encryption, String encrypted) throws NoSuchFieldException, IllegalAccessException, BadPaddingException, IllegalBlockSizeException {
        injectMockEncryption(kushki, encryption);
        when(encryption.encryptMessageChunk(any(String.class))).thenReturn(encrypted);
    }

    private static void injectMockEncryption(Kushki kushki, AurusEncryption encryption) throws NoSuchFieldException, IllegalAccessException {
        Class<?> klass = kushki.getClass();
        Field field = klass.getDeclaredField("encryption");
        field.setAccessible(true);
        field.set(kushki, encryption);
    }

    public static WebResource.Builder mockClient(Kushki kushki, String url) throws NoSuchFieldException, IllegalAccessException {
        Client client = mock(Client.class);
        injectMockClient(kushki, client);
        WebResource webResource = mock(WebResource.class);
        WebResource.Builder requestWithType = mock(WebResource.Builder.class);
        WebResource.Builder requestWithAccept = mock(WebResource.Builder.class);
        when(client.resource(url)).thenReturn(webResource);
        when(webResource.type(MediaType.APPLICATION_JSON_TYPE)).thenReturn(requestWithType);
        when(requestWithType.accept(MediaType.APPLICATION_JSON_TYPE)).thenReturn(requestWithAccept);
        return requestWithAccept;
    }

    public static WebResource.Builder mockWebBuilder(Kushki kushki, String url) throws NoSuchFieldException, IllegalAccessException, BadPaddingException, IllegalBlockSizeException {
        WebResource.Builder builder = TestsHelpers.mockClient(kushki, url);
        ClientResponse response = mock(ClientResponse.class);
        when(response.getEntity(JsonNode.class)).thenReturn(mock(JsonNode.class));
        when(builder.post(eq(ClientResponse.class), any(Map.class))).thenReturn(response);
        return builder;
    }

    private static void injectMockClient(Kushki kushki, Client client) throws NoSuchFieldException, IllegalAccessException {
        Class<?> klass = kushki.getClass();
        Field field = klass.getDeclaredField("client");
        field.setAccessible(true);
        field.set(kushki, client);
    }

    public static Double getRandomAmount(boolean valid) {
        double[] validCents = {0.0, 0.08, 0.11, 0.59, 0.6};
        double[] invalidCents = {0.05, 0.1, 0.21, 0.61, 0.62, 0.63};

        double cents;
        if (valid) {
            int centPosition = nextInt(0, validCents.length - 1);
            cents = validCents[centPosition];
        } else {
            int centPosition = nextInt(0, invalidCents.length - 1);
            cents = invalidCents[centPosition];
        }
        return nextInt(1, 9999) + cents;
    }

    public static Double getRandomAmount() {
        return getRandomAmount(true);
    }

    public static Map<String, String> getCardData() {
        Map<String, String> cardParams = new HashMap<>(5);
        cardParams.put("name", randomAlphabetic(20));
        cardParams.put("number", "4111111111111111");
        cardParams.put("expiry_month", "12");
        cardParams.put("expiry_year", "20");
        cardParams.put("cvv", "123");
        return cardParams;
    }

    public static void assertThatChargeThrowsExceptionWithInvalidAmount(Kushki kushki, Double amount, String exceptionMessage) {
        Exception exception = null;
        try {
            String token = randomAlphabetic(10);
            kushki.charge(token, amount);
        } catch (KushkiException | BadPaddingException | JsonProcessingException | IllegalBlockSizeException e) {
            exception = e;
        }
        Assert.assertNotNull(exception);
        assertThat(exception.getMessage(), is(exceptionMessage));
    }

}

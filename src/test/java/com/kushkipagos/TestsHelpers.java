package com.kushkipagos;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import org.junit.Assert;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.ws.rs.core.MediaType;
import java.lang.reflect.Field;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomUtils.nextDouble;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
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

    public static WebResource.Builder mockClient(Kushki kushki) throws NoSuchFieldException, IllegalAccessException {
        Client client = mock(Client.class);
        injectMockClient(kushki, client);
        WebResource webResource = mock(WebResource.class);
        WebResource.Builder requestWithType = mock(WebResource.Builder.class);
        WebResource.Builder requestWithAccept = mock(WebResource.Builder.class);
        when(client.resource(Kushki.URL)).thenReturn(webResource);
        when(webResource.type(MediaType.APPLICATION_JSON_TYPE)).thenReturn(requestWithType);
        when(requestWithType.accept(MediaType.APPLICATION_JSON_TYPE)).thenReturn(requestWithAccept);
        return requestWithAccept;
    }

    private static void injectMockClient(Kushki kushki, Client client) throws NoSuchFieldException, IllegalAccessException {
        Class<?> klass = kushki.getClass();
        Field field = klass.getDeclaredField("client");
        field.setAccessible(true);
        field.set(kushki, client);
    }

    public static Double getRandomAmount() {
        return Double.parseDouble(String.format("%.2f", nextDouble(1, 99)));
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

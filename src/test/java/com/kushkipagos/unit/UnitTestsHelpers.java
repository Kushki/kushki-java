package com.kushkipagos.unit;

import com.fasterxml.jackson.databind.JsonNode;
import com.kushkipagos.AurusEncryption;
import com.kushkipagos.Kushki;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.ws.rs.core.MediaType;
import java.lang.reflect.Field;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by lmunda on 2/16/16 14:55.
 */
public final class UnitTestsHelpers {
    private UnitTestsHelpers() {

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
        WebResource.Builder builder = mockClient(kushki, url);
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
}

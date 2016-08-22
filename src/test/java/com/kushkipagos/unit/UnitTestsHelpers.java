package com.kushkipagos.unit;

import com.fasterxml.jackson.databind.JsonNode;
import com.kushkipagos.AurusEncryption;
import com.kushkipagos.Kushki;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.reflect.Field;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

final class UnitTestsHelpers {
    private UnitTestsHelpers() {

    }

    static Invocation.Builder mockInvocationBuilder(Kushki kushki, String baseUrl, String url) throws NoSuchFieldException, IllegalAccessException {
        Invocation.Builder builder = mockClient(kushki, baseUrl, url);
        Response response = mock(Response.class);
        when(response.readEntity(JsonNode.class)).thenReturn(mock(JsonNode.class));
        when(builder.post(any(Entity.class))).thenReturn(response);

        return builder;
    }

    static Invocation.Builder mockClient(Kushki kushki, String baseUrl, String url) throws NoSuchFieldException, IllegalAccessException {
        Client client = mock(Client.class);
        injectMockClient(kushki, client);
        WebTarget webTarget = mock(WebTarget.class);
        WebTarget webTargetPath = mock(WebTarget.class);
        Invocation.Builder invocationBuilder = mock(Invocation.Builder.class);
        when(client.target(baseUrl)).thenReturn(webTarget);
        when(webTarget.path(url)).thenReturn(webTargetPath);
        when(webTargetPath.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);

        return invocationBuilder;
    }

    static void mockEncryption(Kushki kushki, AurusEncryption encryption, String encrypted) throws NoSuchFieldException, IllegalAccessException, BadPaddingException, IllegalBlockSizeException {
        injectMockEncryption(kushki, encryption);
        when(encryption.encryptMessageChunk(any(String.class))).thenReturn(encrypted);
    }

    private static void injectMockEncryption(Kushki kushki, AurusEncryption encryption) throws NoSuchFieldException, IllegalAccessException {
        Class<?> klass = kushki.getClass();
        Field field = klass.getDeclaredField("encryption");
        field.setAccessible(true);
        field.set(kushki, encryption);
    }

    private static void injectMockClient(Kushki kushki, Client client) throws NoSuchFieldException, IllegalAccessException {
        Class<?> klass = kushki.getClass();
        Field field = klass.getDeclaredField("client");
        field.setAccessible(true);
        field.set(kushki, client);
    }
}

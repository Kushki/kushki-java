package com.kushkipagos;

import com.fasterxml.jackson.databind.JsonNode;
import com.sun.jersey.api.client.ClientResponse;

public class Transaction {
    private ClientResponse response;

    public Transaction(ClientResponse response) {
        this.response = response;
    }

    public ClientResponse getResponse() {
        return response;
    }

    public boolean isSuccessful() {
        return response.getStatus() == 200;
    }

    public JsonNode getResponseBody() {
        return response.getEntity(JsonNode.class);
    }
}

package com.kushkipagos;

import com.fasterxml.jackson.databind.JsonNode;
import com.sun.jersey.api.client.ClientResponse;

public class Transaction {
    private ClientResponse response;
    private JsonNode body;

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
        body = response.getEntity(JsonNode.class);
        return body;
    }

    public String getTicketNumber() {
        return getResponseAttribute("ticket_number");
    }

    public String getResponseText() {
        return getResponseAttribute("response_text");
    }

    private String getResponseAttribute(String attribute) {
        return body.get(attribute).asText();
    }
}

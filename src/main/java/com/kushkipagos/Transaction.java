package com.kushkipagos;

import com.fasterxml.jackson.databind.JsonNode;
import com.sun.jersey.api.client.ClientResponse;

public class Transaction {
    private ClientResponse response;
    private JsonNode body;

    public Transaction(ClientResponse response) {
        this.response = response;
        body = response.getEntity(JsonNode.class);
    }

    public ClientResponse getResponse() {
        return response;}

    public boolean isSuccessful() {
        return response.getStatus() == 200;
    }

    public JsonNode getResponseBody() {
        return body;}

    public String getTicketNumber() {
        return getResponseAttribute("ticket_number");
    }

    public String getResponseText() {
        return getResponseAttribute("response_text");
    }

    public String getToken() {
        return getResponseAttribute("transaction_token");
    }

    public Double getApprovedAmount() {
        return Double.parseDouble(getResponseAttribute("approved_amount"));}

    public String getResponseCode() {
        return getResponseAttribute("response_code");
    }

    private String getResponseAttribute(String attribute) {
        return body.get(attribute).asText();
    }
}

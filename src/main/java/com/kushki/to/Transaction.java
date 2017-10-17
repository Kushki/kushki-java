package com.kushki.to;


import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;

public class Transaction {
    private JsonNode body;
    private int status;


    public Transaction(HttpResponse<JsonNode> response) {
        this.status = response.getStatus();
        body = response.getBody();
    }

    public Transaction(int statusCode, JsonNode body) {
        this.status = statusCode;
        this.body = body;
    }

    public Transaction(int statusCode) {
        this.status = statusCode;
        this.body = null;
    }


    public boolean isSuccessful() {
        return (status == 200 || status == 201 || status == 204);
    }

    public JsonNode getResponseBody() {
        return body;
    }

    public String getTicketNumber() {
        return getResponseAttribute("ticketNumber");
    }

    public String getSubscriptionId() {
        return getResponseAttribute("subscriptionId");
    }

    public String getResponseText() {
        return getResponseAttribute("message");
    }

    public String getToken() {
        return getResponseAttribute("transaction_token");
    }

    public String getResponseCode() {
        return getResponseAttribute("code");
    }

    private String getResponseAttribute(String attribute) {
        try {
            return body.getObject().getString(attribute);
        } catch (Exception e) {
            return "";
        }

    }

    @Override
    public String toString() {
        return "isSuccessful: " + isSuccessful() +
                ", getTicketNumber: " + getTicketNumber() +
                ", getResponseText: " + getResponseText() +
                ", getToken: " + getToken() +
                ", getResponseCode: " + getResponseCode() +
                ", getSubscriptionId: " + getSubscriptionId()
                ;
    }
}

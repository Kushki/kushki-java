package com.kushki;

import com.kushki.enums.KushkiEnvironment;
import com.kushki.to.Transaction;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;


import javax.ws.rs.client.*;


public class Gateway {

    public static final String PRIVATE_MERCHANT_ID = "Private-Merchant-Id";
    private KushkiEnvironment enviroment;
    private final Client client;

    public Gateway(KushkiEnvironment enviroment) {
        this.enviroment = enviroment;
        this.client = ClientBuilder.newClient();
    }

    public Transaction post(String url, JSONObject data, Kushki kushki) {
        try {
            com.mashape.unirest.http.HttpResponse<JsonNode> jsonResponse = Unirest.post(this.enviroment.getUrl() + url)
                    .header(PRIVATE_MERCHANT_ID, kushki.getMerchantId())
                    .header("Content-Type", "application/json")
                    .body(data.toString())
                    .asJson();
            return new Transaction(jsonResponse);
        } catch (UnirestException e) {
            return new Transaction(0);
        }
    }

    public Transaction patch(String url, JSONObject data, Kushki kushki) {
        try {
            com.mashape.unirest.http.HttpResponse<JsonNode> jsonResponse = Unirest.patch(this.enviroment.getUrl() + url)
                    .header(PRIVATE_MERCHANT_ID, kushki.getMerchantId())
                    .header("Content-Type", "application/json")
                    .body(data.toString())
                    .asJson();
            return new Transaction(jsonResponse.getStatus(), jsonResponse.getBody());
        } catch (UnirestException e) {
            return new Transaction(0);
        }
    }

    public Transaction put(String url, JSONObject data, Kushki kushki) {
        try {
            com.mashape.unirest.http.HttpResponse<JsonNode> jsonResponse = Unirest.put(this.enviroment.getUrl() + url)
                    .header(PRIVATE_MERCHANT_ID, kushki.getMerchantId())
                    .header("Content-Type", "application/json")
                    .body(data.toString())
                    .asJson();
            return new Transaction(jsonResponse.getStatus(), jsonResponse.getBody());
        } catch (UnirestException e) {
            return new Transaction(0);
        }
    }

    public Transaction delete(String url, String id, Kushki kushki) {
        try {
            com.mashape.unirest.http.HttpResponse<JsonNode> jsonResponse = Unirest.delete(this.enviroment.getUrl() + url + id)
                    .header(PRIVATE_MERCHANT_ID, kushki.getMerchantId())
                    .header("Content-Type", "application/json")
                    .asJson();
            return new Transaction(jsonResponse.getStatus(), jsonResponse.getBody());
        } catch (UnirestException e) {
            return new Transaction(0);
        }
    }

}

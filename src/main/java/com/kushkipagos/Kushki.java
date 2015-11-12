package com.kushkipagos;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

public class Kushki {
    public static final String URL = "https://kushki-api.herokuapp.com/v1/charge";
    private final Client client;
    private String merchantId;
    private String language;
    private String currency;

    public Kushki(String merchantId, String language, String currency) {
        this.merchantId = merchantId;
        this.language = language;
        this.currency = currency;
        this.client = Client.create();
    }

    public String getMerchantId() {
        return merchantId;
    }

    public Transaction charge(String token, String amount) {
        Map<String, String> parameters = buildParameters(token, amount);
        return post(parameters);
    }

    private Map<String, String> buildParameters(String token, String amount) {
        Map<String, String> parameters = new HashMap<String, String>(5);
        parameters.put("merchant_identifier", merchantId);
        parameters.put("language_indicator", language);
        parameters.put("transaction_token", token);
        parameters.put("currency_code", currency);
        parameters.put("transaction_amount", amount);
        return parameters;
    }

    private Transaction post(Map<String, String> parameters) {
        WebResource resource = client.resource(URL);
        WebResource.Builder builder = resource.type(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE);
        ClientResponse response = builder.post(ClientResponse.class, parameters);
        return new Transaction(response);
    }

    public String getLanguage() {
        return language;
    }
}

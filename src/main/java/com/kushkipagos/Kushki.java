package com.kushkipagos;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;

public class Kushki {
    public static final String URL = "https://ping.auruspay.com/kushki/api/v1/charge";
    private final Client client;
    private String merchantId;
    private String language;
    private String currency;
    private Encryption encryption;

    public Kushki(String merchantId, String language, String currency) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, InvalidKeySpecException {
        this.merchantId = merchantId;
        this.language = language;
        this.currency = currency;
        this.client = Client.create();
        this.encryption = new Encryption();
    }

    public String getMerchantId() {
        return merchantId;
    }

    public Transaction charge(String token, String amount) throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        if (amount.length() > 12 || amount.length() == 0) {
            throw new KushkiException("Amount must be 12 characters or less");
        } else if (amount.length() < 4) {
            if (amount.startsWith(".")) {
                amount = "0" + amount;
            }
            if (amount.contains(".")) {
                amount = amount + "0";
            } else {
                amount = amount + ".00";
            }
        }
        Map<String, String> parameters = buildParameters(token, amount);
        return post(parameters);
    }

    private Map<String, String> buildParameters(String token, String amount) throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException {
        String params = buildAndStringifyParameters(token, amount);
        String encString = encryption.encryptMessageChunk(params);
        Map<String, String> parameters = new HashMap<>(1);
        parameters.put("request", encString);
        return parameters;
    }

    private String buildAndStringifyParameters(String token, String amount) throws JsonProcessingException {
        Map<String, String> parameters = new HashMap<>(5);
        parameters.put("transaction_token", token);
        parameters.put("transaction_amount", amount);
        parameters.put("currency_code", currency);
        parameters.put("merchant_identifier", merchantId);
        parameters.put("language_indicator", language);
        ObjectMapper mapper = new ObjectMapper();

        return mapper.writeValueAsString(parameters);
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

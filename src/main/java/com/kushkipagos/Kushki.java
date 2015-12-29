package com.kushkipagos;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import java.util.Map;

public class Kushki {
    public static final String URL = "https://ping.auruspay.com/kushki/api/v1";

    public static final String TOKENS_URL = URL + "/tokens";
    public static final String CHARGE_URL = URL + "/charge";
    public static final String VOID_URL = URL + "/void";
    public static final String REFUND_URL = URL + "/refund";

    private final Client client;
    private String merchantId;
    private String language;
    private String currency;
    private AurusEncryption encryption;

    public Kushki(String merchantId, String language, String currency) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, InvalidKeySpecException {
        this.merchantId = merchantId;
        this.language = language;
        this.currency = currency;
        this.client = Client.create();
        this.encryption = new AurusEncryption();
    }

    public String getMerchantId() {
        return merchantId;
    }

    public String getLanguage() {
        return language;
    }

    public String getCurrency() {
        return currency;
    }

    public AurusEncryption getEncryption() {
        return encryption;
    }

    public Transaction requestToken(Map<String, String> params) throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException {
        Map<String, String> parameters = ParametersBuilder.getTokenParameters(this, params);
        return post(TOKENS_URL, parameters);
    }

    public Transaction charge(String token, Double amount) throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        String validAmount = validateAmount(amount);
        Map<String, String> parameters = ParametersBuilder.getChargeParameters(this, token, validAmount);
        return post(CHARGE_URL, parameters);
    }

    public Transaction voidCharge(String token, String ticket, Double amount) throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        String validAmount = validateAmount(amount);
        Map<String, String> parameters = ParametersBuilder.getVoidRefundParameters(this, token, ticket, validAmount);
        return post(VOID_URL, parameters);
    }

    public Transaction refundCharge(String token, String ticket, Double amount) throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        String validAmount = validateAmount(amount);
        Map<String, String> parameters = ParametersBuilder.getVoidRefundParameters(this, token, ticket, validAmount);
        return post(REFUND_URL, parameters);
    }

    private Transaction post(String url, Map<String, String> parameters) {
        WebResource resource = client.resource(url);

        WebResource.Builder builder = resource.type(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE);
        ClientResponse response = builder.post(ClientResponse.class, parameters);
        return new Transaction(response);
    }

    private String validateAmount(Double amount) throws KushkiException {
        if (amount == null) {
            throw new KushkiException("El monto no puede ser nulo");
        }
        if (amount <= 0) {
            throw new KushkiException("El monto debe ser superior a 0");
        }
        String validAmount = String.format("%.2f", amount);
        if (validAmount.length() > 12) {
            throw new KushkiException("El monto debe tener menos de 12 dígitos");
        }
        return validAmount;
    }
}

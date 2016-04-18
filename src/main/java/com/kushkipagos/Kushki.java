package com.kushkipagos;

import com.fasterxml.jackson.core.JsonProcessingException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

public class Kushki {
    public static final String BASE_URL = "https://ping.aurusinc.com/kushki/api/v1";

    public static final String TOKENS_URL = "tokens";
    public static final String CHARGE_URL = "charge";
    public static final String DEFERRED_CHARGE_URL = "deferred";
    public static final String VOID_URL = "void";
    public static final String REFUND_URL = "refund";

    private final Client client;
    private String merchantId;
    private String language;
    private String currency;
    private AurusEncryption encryption;

    public Kushki(String merchantId, String language, String currency) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, InvalidKeySpecException {
        this.merchantId = merchantId;
        this.language = language;
        this.currency = currency;
        this.encryption = new AurusEncryption();
        this.client = ClientBuilder.newClient();
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

    public Transaction requestToken(Map<String, String> cardParams) throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException {
        Map<String, String> parameters = ParametersBuilder.getTokenParameters(this, cardParams);
        return post(TOKENS_URL, parameters);
    }

    public Transaction charge(String token, Amount amount) throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        Map<String, String> parameters = ParametersBuilder.getChargeParameters(this, token, amount);
        return post(CHARGE_URL, parameters);
    }

    public Transaction deferredCharge(String token, Amount amount, Integer months) throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        String validMonths = Validations.validateMonths(months);
        Map<String, String> parameters = ParametersBuilder.getDeferredChargeParameters(this, token, amount, validMonths);
        return post(DEFERRED_CHARGE_URL, parameters);
    }

    public Transaction voidCharge(String ticket, Double amount) throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        String validAmount = Validations.validateAmount(amount);
        Map<String, String> parameters = ParametersBuilder.getVoidRefundParameters(this, ticket, validAmount);
        return post(VOID_URL, parameters);
    }

    public Transaction refundCharge(String ticket, Double amount) throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        String validAmount = Validations.validateAmount(amount);
        Map<String, String> parameters = ParametersBuilder.getVoidRefundParameters(this, ticket, validAmount);
        return post(REFUND_URL, parameters);
    }

    private Transaction post(String url, Map<String, String> parameters) {
        WebTarget target = client.target(BASE_URL).path(url);

        Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON_TYPE);
        Response response = invocationBuilder.post(Entity.entity(parameters, MediaType.APPLICATION_JSON_TYPE));

        return new Transaction(response);
    }
}

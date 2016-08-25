package com.kushkipagos;

import com.fasterxml.jackson.core.JsonProcessingException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

public class Kushki {
    public static final String TOKENS_URL = "tokens";
    public static final String CHARGE_URL = "charge";
    public static final String DEFERRED_CHARGE_URL = "deferred";
    public static final String VOID_URL = "void";
    public static final String REFUND_URL = "refund";

    private final Client client;
    private final KushkiEnvironment environment;

    private final String defaultLanguage = "es";
    private final String defaultCurrency = "USD";
    private final KushkiEnvironment defaultEnvironment = KushkiEnvironment.PRODUCTION;

    private final String merchantId;
    private final String language;
    private final String currency;
    private final AurusEncryption encryption;

    public Kushki(String merchantId, String language, String currency) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, InvalidKeySpecException {
        this.merchantId = merchantId;
        this.language = language;
        this.currency = currency;
        this.encryption = new AurusEncryption();
        this.client = ClientBuilder.newClient();
        this.environment = defaultEnvironment;
    }

    public Kushki(String merchantId, String language, String currency, KushkiEnvironment environment) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, InvalidKeySpecException {
        this.merchantId = merchantId;
        this.language = language;
        this.currency = currency;
        this.encryption = new AurusEncryption();
        this.client = ClientBuilder.newClient();
        this.environment = environment;
    }

    public Kushki(String merchantId) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, IOException {
        this.merchantId = merchantId;
        this.language = defaultLanguage;
        this.currency = defaultCurrency;
        this.encryption = new AurusEncryption();
        this.client = ClientBuilder.newClient();
        this.environment = defaultEnvironment;
    }

    public Kushki(String merchantId, KushkiEnvironment environment) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, IOException {
        this.merchantId = merchantId;
        this.language = defaultLanguage;
        this.currency = defaultCurrency;
        this.encryption = new AurusEncryption();
        this.client = ClientBuilder.newClient();
        this.environment = environment;
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

    public KushkiEnvironment getEnvironment() {
        return environment;
    }

    public AurusEncryption getEncryption() {
        return encryption;
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

    public Transaction voidCharge(String ticket, Amount amount) throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        Map<String, String> parameters = ParametersBuilder.getVoidRefundParameters(this, ticket, amount);
        return post(VOID_URL, parameters);
    }

    public Transaction requestToken(Card card, Amount amount) throws IllegalBlockSizeException, KushkiException, BadPaddingException, JsonProcessingException {
        Map<String, String> parameters = ParametersBuilder.getTokenParameters(this, card, amount);
        return post(TOKENS_URL, parameters);
    }

    private Transaction post(String url, Map<String, String> parameters) {
        WebTarget target = client.target(environment.getUrl()).path(url);
        Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON_TYPE);
        Response response = invocationBuilder.post(Entity.entity(parameters, MediaType.APPLICATION_JSON_TYPE));
        return new Transaction(response);
    }
}

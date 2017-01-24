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
    public static final String TOKENS_URL = "tokens";
    public static final String CHARGE_URL = "charge";
    public static final String DEFERRED_CHARGE_URL = "deferred";
    public static final String VOID_URL = "void";
    public static final String REFUND_URL = "refund";

    private final Client client;
    private final KushkiEnvironment environment;
    private static final String defaultLanguage = "es";
    private static final String defaultCurrency = "USD";
    private final KushkiEnvironment defaultEnvironment = KushkiEnvironment.PRODUCTION;
    private final String merchantId;
    private final String language;
    private final String currency;
    private final AurusEncryption encryption;

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

    public Kushki(String merchantId, KushkiEnvironment environment, String currency) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, IOException {
        this.merchantId = merchantId;
        this.language = defaultLanguage;
        this.currency = currency;
        this.encryption = new AurusEncryption();
        this.client = ClientBuilder.newClient();
        this.environment = environment;
    }

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

    AurusEncryption getEncryption() {
        return encryption;
    }

    /**
     * Perform a charge in Kushki using a valid token for the given amount.
     *
     * @param token  A token obtained from the frontend or using the {@link #requestToken} method.
     * @param amount The detailed {@link Amount} to be charged.
     * @return A {@link Transaction} which contains the result of the operation.
     * @throws JsonProcessingException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws KushkiException
     * @since 1.0.0
     */
    public Transaction charge(String token, Amount amount) throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        Map<String, String> parameters = ParametersBuilder.getChargeParameters(this, token, amount);
        return post(CHARGE_URL, parameters);
    }

    /**
     * Perform a deferred charge in Kushki using a valid token for the given amount.
     *
     * @param token  A token obtained from the frontend or using the {@link #requestToken} method.
     * @param amount The detailed {@link Amount} to be charged.
     * @param months The number of months to defer the payment.
     * @return A {@link Transaction} which contains the result of the operation.
     * @throws JsonProcessingException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws KushkiException
     * @since 1.0.0
     */
    public Transaction deferredCharge(String token, Amount amount, Integer months) throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        String validMonths = Validations.validateMonths(months);
        Map<String, String> parameters = ParametersBuilder.getDeferredChargeParameters(this, token, amount, validMonths);
        return post(DEFERRED_CHARGE_URL, parameters);
    }

    /**
     * Void a transaction previously performed in Kushki.
     *
     * @param ticket The ticket number of the transaction to be voided.
     * @param amount The detailed {@link Amount} to be voided.
     * @return A {@link Transaction} which contains the result of the operation.
     * @throws JsonProcessingException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws KushkiException
     * @since 1.0.0
     */
    public Transaction voidCharge(String ticket, Amount amount) throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        Map<String, String> parameters = ParametersBuilder.getVoidRefundParameters(this, ticket, amount);
        return post(VOID_URL, parameters);
    }

    /**
     * Request a token to later perform charge operations in Kushki using the {@link #charge} or {@link #deferredCharge}
     * methods. This must be done from a {@link Kushki} instance built with the public merchant ID, not the private one.
     * <br>
     * <strong>Note:</strong> If you are using this method from your backend, be sure to comply with all the PCI
     * requirements to handle card data on your servers.
     *
     * @param card        The {@link Card} to which the token will be associated.
     * @param totalAmount The total amount which will be authorized.
     * @return A {@link Transaction} from which, if successful, the token can be extracted with the
     * {@link Transaction#getToken()} method.
     * @throws IllegalBlockSizeException
     * @throws KushkiException
     * @throws BadPaddingException
     * @throws JsonProcessingException
     * @since 1.1.0
     */
    public Transaction requestToken(Card card, Double totalAmount) throws IllegalBlockSizeException, KushkiException, BadPaddingException, JsonProcessingException {
        Map<String, String> parameters = ParametersBuilder.getTokenParameters(this, card, totalAmount);
        return post(TOKENS_URL, parameters);
    }

    private Transaction post(String url, Map<String, String> parameters) {
        WebTarget target = client.target(environment.getUrl()).path(url);
        Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON_TYPE);
        Response response = invocationBuilder.post(Entity.entity(parameters, MediaType.APPLICATION_JSON_TYPE));
        return new Transaction(response);
    }
}

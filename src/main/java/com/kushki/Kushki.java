package com.kushki;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kushki.Enum.KushkiAdjustSuscriptionEnum;
import com.kushki.Enum.KushkiEnvironment;
import com.kushki.Enum.KushkiTransaccionEnum;
import com.kushki.TO.Amount;
import com.kushki.TO.Card;
import com.kushki.TO.SuscriptionInfo;
import com.kushki.TO.Transaction;
import org.json.JSONObject;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.ws.rs.client.*;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;

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
    private final KushkiEnvironment defaultEnvironment = KushkiEnvironment.WRAPPER_PRODUCTION;
    private final String merchantId;
    private final String language;
    private final String currency;
    private Gateway gateway;

    public Kushki(String merchantId) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, IOException {
        this.merchantId = merchantId;
        this.language = defaultLanguage;
        this.currency = defaultCurrency;
        this.client = ClientBuilder.newClient();
        this.environment = defaultEnvironment;
        this.gateway = new Gateway(this.environment);
    }

    public Kushki(String merchantId, KushkiEnvironment environment) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, IOException {
        this.merchantId = merchantId;
        this.language = defaultLanguage;
        this.currency = defaultCurrency;
        this.client = ClientBuilder.newClient();
        this.environment = environment;
        this.gateway = new Gateway(this.environment);
    }

    public Kushki(String merchantId, KushkiEnvironment environment, String currency) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, IOException {
        this.merchantId = merchantId;
        this.language = defaultLanguage;
        this.currency = currency;
        this.client = ClientBuilder.newClient();
        this.environment = environment;
        this.gateway = new Gateway(this.environment);
    }

    public Kushki(String merchantId, String language, String currency) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, InvalidKeySpecException {
        this.merchantId = merchantId;
        this.language = language;
        this.currency = currency;
        this.client = ClientBuilder.newClient();
        this.environment = defaultEnvironment;
        this.gateway = new Gateway(this.environment);
    }

    public Kushki(String merchantId, String language, String currency, KushkiEnvironment environment) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, InvalidKeySpecException {
        this.merchantId = merchantId;
        this.language = language;
        this.currency = currency;
        this.client = ClientBuilder.newClient();
        this.environment = environment;
        this.gateway = new Gateway(this.environment);
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

    @Deprecated
    public Transaction charge(String token, Amount amount) throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        return this.gateway.post(KushkiTransaccionEnum.CHARGE.toString()
                , ParametersBuilder.getChargeParameters(this, token, amount, null, null), this);
    }

    @Deprecated
    public Transaction charge(String token, Amount amount, JSONObject metadata) throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        return this.gateway.post(KushkiTransaccionEnum.CHARGE.toString()
                , ParametersBuilder.getChargeParameters(this, token, amount, null, metadata), this);
    }

    /**
     * Perform a deferred charge in com.kushki.Kushki using a valid token for the given amount.
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

    @Deprecated
    public Transaction deferredCharge(String token, Amount amount, Integer months) throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        return this.gateway.post(KushkiTransaccionEnum.CHARGE.toString()
                , ParametersBuilder.getChargeParameters(this, token, amount, months, null), this);
    }

    /**
     * Perform a deferred o normal charge in com.kushki.Kushki using a valid token for the given amount.
     *
     * @param token    A token obtained from the frontend or using the {@link #requestToken} method.
     * @param amount   The detailed {@link Amount} to be charged.
     * @param months   The number of months to defer the payment (could by null).
     * @param metadata JSONObject with Metadata (could by null).
     * @return A {@link Transaction} which contains the result of the operation.
     * @throws JsonProcessingException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws KushkiException
     * @since 1.0.0
     */
    public Transaction deferredCharge(String token, Amount amount, Integer months, JSONObject metadata) throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        return this.gateway.post(KushkiTransaccionEnum.CHARGE.toString()
                , ParametersBuilder.getChargeParameters(this, token, amount, months, metadata), this);
    }

    /**
     * Void a transaction previously performed in com.kushki.Kushki.
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
        return this.gateway.delete(KushkiTransaccionEnum.VOID.toString(), ticket, this);
    }

    public Transaction refundCharge(String ticket) throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        return this.gateway.delete(KushkiTransaccionEnum.REFUND.toString(), ticket, this);
    }

    /**
     * Request a token to later perform charge operations in com.kushki.Kushki using the {@link #charge} or {@link #deferredCharge}
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
    @Deprecated
    public Transaction requestToken(Card card, Double totalAmount) throws IllegalBlockSizeException, KushkiException, BadPaddingException, JsonProcessingException {
        //  Map<String, String> parameters = ParametersBuilder.getTokenParameters(this, card, totalAmount);
        //  return post(TOKENS_URL, parameters);
        return null;
    }

    public Transaction subscription(String token, Amount amount, JSONObject metadata, SuscriptionInfo suscriptionInfo) throws KushkiException {
        return this.gateway.post(KushkiTransaccionEnum.SUSCRIPTION.toString()
                , ParametersBuilder.getSubscriptionParams(this, token, amount, metadata, suscriptionInfo), this);
    }

    public Transaction updateSubscription(Amount amount, JSONObject metadata, SuscriptionInfo suscriptionInfo, String subscriptionId) throws KushkiException {
        return this.gateway.patch(KushkiTransaccionEnum.SUSCRIPTION.toString() + "/" + subscriptionId
                , ParametersBuilder.getUpdateSubscriptionParams(this, amount, metadata, suscriptionInfo), this);
    }

    public Transaction updateSubscriptionCard(String newToken, String subscriptionId) throws KushkiException {
        return this.gateway.put(KushkiTransaccionEnum.SUSCRIPTION.toString() + "/" + subscriptionId + KushkiTransaccionEnum.SUSCRIPTION_CARD
                , ParametersBuilder.getUpdateCardParams(newToken), this);
    }

    public Transaction adjustSubscription(String subscriptionId, Date date, int periods, Amount amount, KushkiAdjustSuscriptionEnum adjustPeriod) throws KushkiException {
        return this.gateway.put(KushkiTransaccionEnum.SUSCRIPTION + "/" + subscriptionId + KushkiTransaccionEnum.SUSCRIPTION_ADJUSTMENT,
                ParametersBuilder.getSubscriptionAdjustmentParams(this, date, periods, adjustPeriod, amount), this);

    }

    public Transaction subscriptionCharge(String cvv, Amount amount, JSONObject metadata, String subscriptionId) throws KushkiException {
        return this.gateway.post(KushkiTransaccionEnum.SUSCRIPTION.toString() + "/" + subscriptionId + KushkiTransaccionEnum.CHARGE
                , ParametersBuilder.getSubscriptionChargeParams(cvv, amount, metadata, this), this);
    }

    public Transaction deleteSubscription(String subscriptionId) throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        return this.gateway.delete(KushkiTransaccionEnum.SUSCRIPTION.toString(), subscriptionId, this);
    }

}

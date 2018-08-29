package com.kushki;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kushki.enums.KushkiAdjustSubscription;
import com.kushki.enums.KushkiEnvironment;
import com.kushki.enums.KushkiTransaccionType;
import com.kushki.to.Amount;
import com.kushki.to.SubscriptionInfo;
import com.kushki.to.Transaction;
import org.json.JSONObject;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
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
    private final KushkiEnvironment defaultEnvironment = KushkiEnvironment.PRODUCTION;
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

    /**
     * Perform a  charge in com.kushki.Kushki using a valid token for the given amount.
     * @deprecated this method  was replace by charge(String token, Amount amount, Integer months, JSONObject metadata)
     * @param token  A token obtained from the frontend or using the method.
     * @param amount The detailed {@link Amount} to be charged.
     * @return A {@link Transaction} which contains the result of the operation.
     * @throws KushkiException when the imput fail
     * @since 1.0.0
     */
    @Deprecated
    public Transaction charge(String token, Amount amount) throws KushkiException {
        return this.gateway.post(KushkiTransaccionType.CHARGE.toString()
                , ParametersBuilder.getChargeParameters(this, token, amount, null, null), this);
    }

    /**
     * Perform a deferred charge in com.kushki.Kushki using a valid token for the given amount.
     *
     * @deprecated this method  was replace by charge(String token, Amount amount, Integer months, JSONObject metadata)
     * @param token  A token obtained from the frontend or using the method.
     * @param amount The detailed {@link Amount} to be charged.
     * @param metadata The JSONObject with transaction's metadata
     * @return A {@link Transaction} which contains the result of the operation.
     * @throws KushkiException
     * @since 1.0.0
     */
    @Deprecated
    public Transaction charge(String token, Amount amount, JSONObject metadata) throws KushkiException {
        return this.gateway.post(KushkiTransaccionType.CHARGE.toString()
                , ParametersBuilder.getChargeParameters(this, token, amount, null, metadata), this);
    }

    /**
     * Perform a deferred charge in com.kushki.Kushki using a valid token for the given amount.
     *
     * @deprecated this method  was replace by charge(String token, Amount amount, Integer months, JSONObject metadata)
     * @param token  A token obtained from the frontend or using the method.
     * @param amount The detailed {@link Amount} to be charged.
     * @param months The number of months to defer the payment.
     * @return A {@link Transaction} which contains the result of the operation.
     * @throws KushkiException
     * @since 1.0.0
     */

    @Deprecated
    public Transaction charge(String token, Amount amount, Integer months) throws  KushkiException {
        return this.gateway.post(KushkiTransaccionType.CHARGE.toString()
                , ParametersBuilder.getChargeParameters(this, token, amount, months, null), this);
    }

    /**
     * Perform a deferred o normal charge in com.kushki.Kushki using a valid token for the given amount.
     *
     * @param token    A token obtained from the frontend or using the  method.
     * @param amount   The detailed {@link Amount} to be charged.
     * @param months   The number of months to defer the payment (could by null).
     * @param metadata JSONObject with Metadata (could by null).
     * @return A {@link Transaction} which contains the result of the operation.
     * @throws KushkiException
     * @since 1.0.0
     */
    public Transaction charge(String token, Amount amount, Integer months, JSONObject metadata) throws KushkiException {
        return this.gateway.post(KushkiTransaccionType.CHARGE.toString()
                , ParametersBuilder.getChargeParameters(this, token, amount, months, metadata), this);
    }

    /**
     * Void a transaction previously performed in com.kushki.Kushki.
     *
     * @param ticket The ticket number of the transaction to be voided.
     * @return A {@link Transaction} which contains the result of the operation.
     * @throws KushkiException
     * @since 1.0.0
     */
    public Transaction voidCharge(String ticket) throws KushkiException {
        return this.gateway.delete(KushkiTransaccionType.VOID.toString(), ticket, this);
    }

    public Transaction refundCharge(String ticket) throws  KushkiException {
        return this.gateway.delete(KushkiTransaccionType.REFUND.toString(), ticket, this);
    }

    public Transaction subscription(String token, Amount amount, JSONObject metadata, SubscriptionInfo subscriptionInfo) throws KushkiException {
        return this.gateway.post(KushkiTransaccionType.SUSCRIPTION.toString()
                , ParametersBuilder.getSubscriptionParams(this, token, amount, metadata, subscriptionInfo), this);
    }

    public Transaction updateSubscription(Amount amount, JSONObject metadata, SubscriptionInfo subscriptionInfo, String subscriptionId) throws KushkiException {
        return this.gateway.patch(KushkiTransaccionType.SUSCRIPTION.toString() + "/" + subscriptionId
                , ParametersBuilder.getUpdateSubscriptionParams(this, amount, metadata, subscriptionInfo), this);
    }

    public Transaction updateSubscriptionCard(String newToken, String subscriptionId) throws KushkiException {
        return this.gateway.put(KushkiTransaccionType.SUSCRIPTION.toString() + "/" + subscriptionId + KushkiTransaccionType.SUSCRIPTION_CARD
                , ParametersBuilder.getUpdateCardParams(newToken), this);
    }

    public Transaction adjustSubscription(String subscriptionId, Date date, int periods, Amount amount, KushkiAdjustSubscription adjustPeriod) throws KushkiException {
        return this.gateway.put(KushkiTransaccionType.SUSCRIPTION + "/" + subscriptionId + KushkiTransaccionType.SUSCRIPTION_ADJUSTMENT,
                ParametersBuilder.getSubscriptionAdjustmentParams(this, date, periods, adjustPeriod, amount), this);

    }

    public Transaction subscriptionCharge(String cvv, Amount amount, JSONObject metadata, String subscriptionId) throws KushkiException {
        return this.gateway.post(KushkiTransaccionType.SUSCRIPTION.toString() + "/" + subscriptionId + KushkiTransaccionType.CHARGE
                , ParametersBuilder.getSubscriptionChargeParams(cvv, amount, metadata, this), this);
    }

    public Transaction deleteSubscription(String subscriptionId) throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        return this.gateway.delete(KushkiTransaccionType.SUSCRIPTION.toString()+ "/" , subscriptionId, this);
    }

}

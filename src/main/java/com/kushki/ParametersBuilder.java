package com.kushki;

import com.kushki.enums.KushkiAdjustSubscription;
import com.kushki.to.Amount;
import com.kushki.to.ContactDetail;
import com.kushki.to.SubscriptionInfo;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class ParametersBuilder {


    public static final String TOKEN = "token";
    public static final String MONTHS = "months";
    public static final String METADATA = "metadata";
    public static final String AMOUNT = "amount";
    public static final String REQUIRED_FIELD_IS_NULL = "A required field is null";
    public static final String LANGUAGE = "language";
    public static final String START_DATE = "startDate";
    public static final String PERIODICITY = "periodicity";
    public static final String PLAN_NAME = "planName";
    public static final String CONTACT_DETAILS = "contactDetails";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";

    public static JSONObject getChargeParameters(Kushki kushki, String token, Amount amount, Integer month, JSONObject metadata) throws KushkiException {
        JSONObject responseObject = new JSONObject();
        try {
            responseObject.put(TOKEN, token);
            JSONObject amountObject = getAmountJson(kushki, amount);
            if (month != null)
                responseObject.put(MONTHS, (int) month);
            if (metadata != null) {
                responseObject.put(METADATA, metadata);
            }
            responseObject.put(AMOUNT, amountObject);
        } catch (Exception e) {
            throw new KushkiException(REQUIRED_FIELD_IS_NULL);
        }
        return responseObject;
    }

    private static JSONObject getAmountJson(Kushki kushki, Amount amount) {
        JSONObject amountObject = new JSONObject();
        amountObject.put("subtotalIva", amount.getSubtotalIVA());
        amountObject.put("subtotalIva0", amount.getSubtotalIVA0());
        amountObject.put("ice", amount.getIce());
        amountObject.put("iva", amount.getIva());
        if (kushki.getCurrency() != null && kushki.getCurrency().length() > 0)
            amountObject.put("currency", kushki.getCurrency());
        if (amount.getExtraTaxes() != null && getJSONExtraTax(amount).length() > 0)
            amountObject.put("extraTaxes", getJSONExtraTax(amount));
        return amountObject;
    }


    private static JSONObject getContactDetailJson(ContactDetail contactInfo) throws JSONException {
        JSONObject contactObject = new JSONObject();
        contactObject.put("firstName", contactInfo.getFirstName());
        contactObject.put("lastName", contactInfo.getLastName());
        contactObject.put("email", contactInfo.getEmail());
        return contactObject;
    }


    private static JSONObject getJSONExtraTax(Amount amount) throws JSONException {
        JSONObject extraTax = new JSONObject();
        if (amount.getExtraTaxes().getPropina().getAmount() != 0)
            extraTax.put("propina", amount.getExtraTaxes().getPropina().getAmount());
        if (amount.getExtraTaxes().getIac().getAmount() != 0)
            extraTax.put("iac", amount.getExtraTaxes().getIac().getAmount());
        if (amount.getExtraTaxes().getTasaAeroportuaria().getAmount() != 0)
            extraTax.put("tasaAeroportuaria", amount.getExtraTaxes().getTasaAeroportuaria().getAmount());
        if (amount.getExtraTaxes().getAgenciaDeViaje().getAmount() != 0)
            extraTax.put("agenciaDeViaje", amount.getExtraTaxes().getAgenciaDeViaje().getAmount());
        return extraTax;
    }

    public static JSONObject getSubscriptionChargeParams(String cvv, Amount amount, JSONObject metadata, Kushki kushki) throws KushkiException {
        JSONObject responseObject = new JSONObject();
        try {
            if (cvv != null && cvv.length() > 0)
                responseObject.put("cvv", cvv);
            if (kushki.getLanguage() != null)
                responseObject.put(LANGUAGE, kushki.getLanguage());
            if (metadata != null) {
                responseObject.put(METADATA, metadata);
            }
            if (amount != null) {
                JSONObject amountObject = getAmountJson(kushki, amount);
                responseObject.put(AMOUNT, amountObject);
            }
        } catch (Exception e) {
            throw new KushkiException(REQUIRED_FIELD_IS_NULL);
        }
        return responseObject;
    }

    public static JSONObject getSubscriptionParams(Kushki kushki, String token, Amount amount, JSONObject metadata, SubscriptionInfo subscriptionInfo) throws KushkiException {
        JSONObject responseObject = new JSONObject();
        try {
            responseObject.put(TOKEN, token);
            responseObject.put(PLAN_NAME, subscriptionInfo.getPlanName());
            responseObject.put(PERIODICITY, subscriptionInfo.getPeriodicity().getName());
            SimpleDateFormat dateFormat = new SimpleDateFormat(YYYY_MM_DD);
            responseObject.put(START_DATE, dateFormat.format(subscriptionInfo.getStartDate()));
            if (kushki.getLanguage() != null)
                responseObject.put(LANGUAGE, kushki.getLanguage());
            JSONObject contactObject = getContactDetailJson( subscriptionInfo.getContactDetail());
            JSONObject amountObject = getAmountJson(kushki, amount);
            if (metadata != null) {
                responseObject.put(METADATA, metadata);
            }
            responseObject.put(AMOUNT, amountObject);
            responseObject.put(CONTACT_DETAILS, contactObject);
        } catch (Exception e) {
            throw new KushkiException(REQUIRED_FIELD_IS_NULL);
        }
        return responseObject;
    }

    public static JSONObject getSubscriptionAdjustmentParams(Kushki kushki, Date date, int periods, KushkiAdjustSubscription type, Amount amount) throws KushkiException {
        JSONObject responseObject = new JSONObject();
        try {
            responseObject.put("type", type.getName());
            SimpleDateFormat dateFormat = new SimpleDateFormat(YYYY_MM_DD);
            responseObject.put("date", dateFormat.format(date));
            responseObject.put("periods", periods);
            JSONObject amountObject = getAmountJson(kushki, amount);
            responseObject.put(AMOUNT, amountObject);
        } catch (Exception e) {
            throw new KushkiException(REQUIRED_FIELD_IS_NULL);
        }
        return responseObject;
    }

    public static JSONObject getUpdateSubscriptionParams(Kushki kushki, Amount amount, JSONObject metadata, SubscriptionInfo subscriptionInfo) throws KushkiException {
        JSONObject responseObject = new JSONObject();
        try {
            if (subscriptionInfo != null) {
                JSONObject contactObject = getContactDetailJson( subscriptionInfo.getContactDetail());
                if (contactObject != null) {
                    responseObject.put(CONTACT_DETAILS, contactObject);
                }
                if (subscriptionInfo.getPlanName() != null && subscriptionInfo.getPlanName().length() > 0)
                    responseObject.put(PLAN_NAME, subscriptionInfo.getPlanName());
                if (subscriptionInfo.getPeriodicity() != null)
                    responseObject.put(PERIODICITY, subscriptionInfo.getPeriodicity().getName());
                SimpleDateFormat dateFormat = new SimpleDateFormat(YYYY_MM_DD);
                if (subscriptionInfo.getStartDate() != null)
                    responseObject.put(START_DATE, dateFormat.format(subscriptionInfo.getStartDate()));
            }
            if (kushki.getLanguage() != null)
                responseObject.put(LANGUAGE, kushki.getLanguage());

            if (metadata != null) {
                responseObject.put(METADATA, metadata);
            }
            if (amount != null) {
                JSONObject amountObject = getAmountJson(kushki, amount);
                responseObject.put(AMOUNT, amountObject);
            }

        } catch (Exception e) {
            throw new KushkiException(REQUIRED_FIELD_IS_NULL);
        }
        return responseObject;
    }

    public static JSONObject getUpdateCardParams(String subscriptionId) throws KushkiException {
        JSONObject responseObject = new JSONObject();
        try {
            responseObject.put(TOKEN, subscriptionId);
        } catch (Exception e) {
            throw new KushkiException(REQUIRED_FIELD_IS_NULL);
        }
        return responseObject;
    }
}

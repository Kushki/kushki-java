package com.kushki;

import com.kushki.Enum.KushkiAdjustSuscriptionEnum;
import com.kushki.TO.Amount;
import com.kushki.TO.ContactDetail;
import com.kushki.TO.SuscriptionInfo;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class ParametersBuilder {


    public static JSONObject getChargeParameters(Kushki kushki, String token, Amount amount, Integer month, JSONObject metadata) throws KushkiException {
        JSONObject responseObject = new JSONObject();
        try {
            responseObject.put("token", token);
            JSONObject amountObject = getAmountJson(kushki, amount);
            if (month != null)
                responseObject.put("months", (int) month);
            if (metadata != null) {
                responseObject.put("metadata", metadata);
            }
            responseObject.put("amount", amountObject);
        } catch (Exception e) {
            throw new KushkiException("A required field is null");
        }
        return responseObject;
    }

    private static JSONObject getAmountJson(Kushki kushki, Amount amount) throws JSONException {
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


    private static JSONObject getContactDetailJson(Kushki kushki, ContactDetail contactInfo) throws JSONException {
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
                responseObject.put("language", kushki.getLanguage());
            if (metadata != null) {
                responseObject.put("metadata", metadata);
            }
            if (amount != null) {
                JSONObject amountObject = getAmountJson(kushki, amount);
                responseObject.put("amount", amountObject);
            }
        } catch (Exception e) {
            throw new KushkiException("A required field is null");
        }
        return responseObject;
    }

    public static JSONObject getSubscriptionParams(Kushki kushki, String token, Amount amount, JSONObject metadata, SuscriptionInfo suscriptionInfo) throws KushkiException {
        JSONObject responseObject = new JSONObject();
        try {
            responseObject.put("token", token);
            responseObject.put("planName", suscriptionInfo.getPlanName());
            responseObject.put("periodicity", suscriptionInfo.getPeriodicity().getName());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            responseObject.put("startDate", dateFormat.format(suscriptionInfo.getStartDate()));
            if (kushki.getLanguage() != null)
                responseObject.put("language", kushki.getLanguage());
            JSONObject contactObject = getContactDetailJson(kushki, suscriptionInfo.getContactDetail());
            JSONObject amountObject = getAmountJson(kushki, amount);
            if (metadata != null) {
                responseObject.put("metadata", metadata);
            }
            responseObject.put("amount", amountObject);
            responseObject.put("contactDetails", contactObject);
        } catch (Exception e) {
            throw new KushkiException("A required field is null");
        }
        return responseObject;
    }

    public static JSONObject getSubscriptionAdjustmentParams(Kushki kushki, Date date, int periods, KushkiAdjustSuscriptionEnum type, Amount amount) throws KushkiException {
        JSONObject responseObject = new JSONObject();
        try {
            responseObject.put("type", type.getName());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            responseObject.put("date", dateFormat.format(date));
            responseObject.put("periods", periods);
            JSONObject amountObject = getAmountJson(kushki, amount);
            responseObject.put("amount", amountObject);
        } catch (Exception e) {
            throw new KushkiException("A required field is null");
        }
        return responseObject;
    }

    public static JSONObject getUpdateSubscriptionParams(Kushki kushki, Amount amount, JSONObject metadata, SuscriptionInfo suscriptionInfo) throws KushkiException {
        JSONObject responseObject = new JSONObject();
        try {
            if (suscriptionInfo != null) {
                JSONObject contactObject = getContactDetailJson(kushki, suscriptionInfo.getContactDetail());
                if (contactObject != null) {
                    responseObject.put("contactDetails", contactObject);
                }
                if (suscriptionInfo.getPlanName() != null && suscriptionInfo.getPlanName().length() > 0)
                    responseObject.put("planName", suscriptionInfo.getPlanName());
                if (suscriptionInfo.getPeriodicity() != null)
                    responseObject.put("periodicity", suscriptionInfo.getPeriodicity().getName());
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                if (suscriptionInfo.getStartDate() != null)
                    responseObject.put("startDate", dateFormat.format(suscriptionInfo.getStartDate()));
            }
            if (kushki.getLanguage() != null)
                responseObject.put("language", kushki.getLanguage());

            if (metadata != null) {
                responseObject.put("metadata", metadata);
            }
            if (amount != null) {
                JSONObject amountObject = getAmountJson(kushki, amount);
                responseObject.put("amount", amountObject);
            }

        } catch (Exception e) {
            throw new KushkiException("A required field is null");
        }
        return responseObject;
    }

    public static JSONObject getUpdateCardParams(String subscriptionId) throws KushkiException {
        JSONObject responseObject = new JSONObject();
        try {
            responseObject.put("token", subscriptionId);
        } catch (Exception e) {
            throw new KushkiException("A required field is null");
        }
        return responseObject;
    }
}

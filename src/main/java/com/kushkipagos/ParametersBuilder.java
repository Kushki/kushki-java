package com.kushkipagos;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.util.HashMap;
import java.util.Map;

public final class ParametersBuilder {

    private ParametersBuilder() {
    }

    static Map<String, String> getChargeParameters(Kushki kushki, String token, Amount amount) throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        String params = buildAndStringifyChargeParameters(kushki, token, amount);
        return encryptParams(kushki, params);
    }

    static Map<String, String> getDeferredChargeParameters(Kushki kushki, String token, Amount amount, String months) throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        String params = buildAndStringifyDeferredChargeParameters(kushki, token, amount, months);
        return encryptParams(kushki, params);
    }

    static Map<String, String> getVoidRefundParameters(Kushki kushki, String ticket, Amount amount) throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        String params = buildAndStringifyVoidRefundParameters(kushki, ticket, amount);
        return encryptParams(kushki, params);
    }

    static Map<String, String> getTokenParameters(Kushki kushki, Map<String, String> cardParams, Amount amount) throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        ObjectMapper mapper = new ObjectMapper();
        String params = mapper.writeValueAsString(cardParams);
        params = buildAndStringifyTokenParameters(kushki, params, amount);
        return encryptParams(kushki, params);
    }

    private static Map<String, String> encryptParams(Kushki kushki, String params) throws BadPaddingException, IllegalBlockSizeException {
        String encString = kushki.getEncryption().encryptMessageChunk(params);
        Map<String, String> encryptedParameters = new HashMap<>(1);
        encryptedParameters.put("request", encString);
        return encryptedParameters;
    }

    private static String buildAndStringifyChargeParameters(Kushki kushki, String token, Amount amount) throws JsonProcessingException, KushkiException {
        ObjectMapper mapperAmount = new ObjectMapper();
        String stringifiedAmount = mapperAmount.writeValueAsString(amount.toHash());

        Map<String, String> parameters = getCommonParameters(kushki);
        parameters.put("transaction_token", token);
        parameters.put("transaction_amount", stringifiedAmount);

        ObjectMapper mapperParameters = new ObjectMapper();
        return mapperParameters.writeValueAsString(parameters);
    }

    private static String buildAndStringifyDeferredChargeParameters(Kushki kushki, String token, Amount amount, String months) throws JsonProcessingException, KushkiException {
        ObjectMapper mapperAmount = new ObjectMapper();
        String stringifiedAmount = mapperAmount.writeValueAsString(amount.toHash());

        Map<String, String> parameters = getCommonParameters(kushki);
        parameters.put("transaction_token", token);
        parameters.put("transaction_amount", stringifiedAmount);
        parameters.put("months", months);

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(parameters);
    }

    private static String buildAndStringifyVoidRefundParameters(Kushki kushki, String ticket, Amount amount) throws JsonProcessingException, KushkiException {
        ObjectMapper mapperAmount = new ObjectMapper();
        String stringifiedAmount = mapperAmount.writeValueAsString(amount.toHash());

        Map<String, String> parameters = getCommonParameters(kushki);
        parameters.put("ticket_number", ticket);
        parameters.put("transaction_amount", stringifiedAmount);

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(parameters);
    }

    private static String buildAndStringifyTokenParameters(Kushki kushki, String cardParams, Amount amount) throws JsonProcessingException, KushkiException {
        Map<String, String> parameters = getCommonParameters(kushki);
        parameters.put("card", cardParams);
        parameters.put("amount", amount.toHash().get("Total_amount"));
        parameters.put("remember_me", "0");
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(parameters);
    }

    private static Map<String, String> getCommonParameters(Kushki kushki) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("currency_code", kushki.getCurrency());
        parameters.put("merchant_identifier", kushki.getMerchantId());
        parameters.put("language_indicator", kushki.getLanguage());
        return parameters;
    }
}

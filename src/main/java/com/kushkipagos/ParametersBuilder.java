package com.kushkipagos;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lmunda on 12/28/15 12:22.
 */
public class ParametersBuilder {

    public static Map<String, String> getTokenParameters(Kushki kushki, Map<String, String> cardParams) throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException {
        ObjectMapper mapper = new ObjectMapper();
        String params = mapper.writeValueAsString(cardParams);
        params = buildAndStringifyTokenParameters(kushki, params);
        return encryptParams(kushki, params);
    }

    public static Map<String, String> getChargeParameters(Kushki kushki, String token, String amount) throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException {
        String params = buildAndStringifyChargeParameters(kushki, token, amount);
        return encryptParams(kushki, params);
    }

    public static Map<String, String> getDeferredChargeParameters(Kushki kushki, String token, String amount, String months, String interest) throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException {
        String params = buildAndStringifyDeferredChargeParameters(kushki, token, amount, months, interest);
        return encryptParams(kushki, params);
    }

    public static Map<String, String> getVoidRefundParameters(Kushki kushki, String ticket, String amount) throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException {
        String params = buildAndStringifyVoidRefundParameters(kushki, ticket, amount);
        return encryptParams(kushki, params);
    }

    private static Map<String, String> encryptParams(Kushki kushki, String params) throws BadPaddingException, IllegalBlockSizeException {
        String encString = kushki.getEncryption().encryptMessageChunk(params);
        Map<String, String> encryptedParameters = new HashMap<>(1);
        encryptedParameters.put("request", encString);
        return encryptedParameters;
    }

    private static String buildAndStringifyTokenParameters(Kushki kushki, String cardParams) throws JsonProcessingException {
        Map<String, String> parameters = getCommonParameters(kushki);
        parameters.put("card", cardParams);

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(parameters);
    }

    private static String buildAndStringifyChargeParameters(Kushki kushki, String token, String amount) throws JsonProcessingException {
        Map<String, String> parameters = getCommonParameters(kushki);
        parameters.put("transaction_token", token);
        parameters.put("transaction_amount", amount);

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(parameters);
    }

    private static String buildAndStringifyDeferredChargeParameters(Kushki kushki, String token, String amount, String months, String interest) throws JsonProcessingException {
        Map<String, String> parameters = getCommonParameters(kushki);
        parameters.put("transaction_token", token);
        parameters.put("transaction_amount", amount);
        parameters.put("months", months);
        parameters.put("rate_of_interest", interest);

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(parameters);
    }

    private static String buildAndStringifyVoidRefundParameters(Kushki kushki, String ticket, String amount) throws JsonProcessingException {
        Map<String, String> parameters = getCommonParameters(kushki);
        parameters.put("ticket_number", ticket);
        parameters.put("transaction_amount", amount);

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

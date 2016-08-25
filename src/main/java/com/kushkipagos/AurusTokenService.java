package com.kushkipagos;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

import static com.kushkipagos.Kushki.TOKENS_URL;

public class AurusTokenService {

    public Transaction requestToken(Kushki kushki, Map<String, String> cardParams, Amount amount) throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        Map<String, String> parameters = getTokenParameters(kushki, cardParams, amount);
        return post(parameters);
    }

    private static Transaction post(Map<String, String> parameters) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(KushkiEnvironment.TESTING.getUrl()).path(TOKENS_URL);

        Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON_TYPE);
        Response response = invocationBuilder.post(Entity.entity(parameters, MediaType.APPLICATION_JSON_TYPE));

        return new Transaction(response);
    }

    private static Map<String, String> getTokenParameters(Kushki kushki, Map<String, String> cardParams, Amount amount) throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        ObjectMapper mapper = new ObjectMapper();
        String params = mapper.writeValueAsString(cardParams);
        params = buildAndStringifyTokenParameters(kushki, params, amount);
        return encryptParams(kushki, params);
    }

    private static String buildAndStringifyTokenParameters(Kushki kushki, String cardParams, Amount amount) throws JsonProcessingException, KushkiException {
        Map<String, String> parameters = getCommonParameters(kushki);
        parameters.put("card", cardParams);
        parameters.put("amount", amount.toHash().get("Total_amount"));

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(parameters);
    }

    private static Map<String, String> encryptParams(Kushki kushki, String params) throws BadPaddingException, IllegalBlockSizeException {
        String encString = kushki.getEncryption().encryptMessageChunk(params);
        Map<String, String> encryptedParameters = new HashMap<>(1);
        encryptedParameters.put("request", encString);
        return encryptedParameters;
    }

    private static Map<String, String> getCommonParameters(Kushki kushki) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("currency_code", kushki.getCurrency());
        parameters.put("merchant_identifier", kushki.getMerchantId());
        parameters.put("language_indicator", kushki.getLanguage());
        parameters.put("remember_me", "0");
        return parameters;
    }
}

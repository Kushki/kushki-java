package com.kushkipagos.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kushkipagos.Kushki;
import com.kushkipagos.KushkiEnvironment;
import com.kushkipagos.ParametersBuilder;
import com.kushkipagos.Transaction;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

import static com.kushkipagos.Kushki.TOKENS_URL;
import static com.kushkipagos.commons.TestsHelpers.getValidCardData;


/**
 * Created by dvillaci on 4/19/16.
 */

final class TokenHelper {

    private TokenHelper() {
    }

    static Transaction getValidTokenTransaction(Kushki kushki) throws BadPaddingException, IllegalBlockSizeException, JsonProcessingException {
        Map<String, String> cardParams = getValidCardData();
        return requestToken(kushki, cardParams);
    }

    static Transaction requestToken(Kushki kushki, Map<String, String> cardParams) throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException {
        Map<String, String> parameters = ParametersBuilder.getTokenParameters(kushki, cardParams);
        return post(parameters);
    }

    private static Transaction post(Map<String, String> parameters) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(KushkiEnvironment.TESTING.getUrl()).path(TOKENS_URL);

        Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON_TYPE);
        Response response = invocationBuilder.post(Entity.entity(parameters, MediaType.APPLICATION_JSON_TYPE));

        return new Transaction(response);
    }
}

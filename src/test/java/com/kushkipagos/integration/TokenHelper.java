package com.kushkipagos.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kushkipagos.Amount;
import com.kushkipagos.Kushki;
import com.kushkipagos.KushkiException;
import com.kushkipagos.Transaction;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.util.Map;

import static com.kushkipagos.commons.TestsHelpers.getRandomAmount;
import static com.kushkipagos.commons.TestsHelpers.getValidCardData;

final class TokenHelper {

    private TokenHelper() {
    }

    static Transaction getValidTokenTransaction(Kushki kushki) throws BadPaddingException, IllegalBlockSizeException, JsonProcessingException, KushkiException {
        return getValidTokenTransaction(kushki, getRandomAmount());
    }

    static Transaction getValidTokenTransaction(Kushki kushki, Amount amount) throws BadPaddingException, IllegalBlockSizeException, JsonProcessingException, KushkiException {
        Map<String, String> cardParams = getValidCardData();
        return kushki.requestToken(cardParams, amount);
    }
}

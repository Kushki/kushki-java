package com.kushkipagos;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Before;
import org.junit.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static com.kushkipagos.IntegrationTestsHelpers.*;

/**
 * Created by lmunda on 1/22/16 10:16.
 */
public class KushkiChargeIntegrationTest_006_008 {
    private Kushki kushki;

    Transaction tokenTransaction;
    Transaction chargeTransaction;
    private Double amount;
    private String token;

    @Before
    public void setup() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, IOException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        kushki = setupKushki();

        tokenTransaction = getValidTokenTransaction(kushki);
        amount = TestsHelpers.getRandomAmount();
        token = tokenTransaction.getToken();

        chargeTransaction = kushki.charge(token, amount);
    }

    @Test
    public void shouldReturnSuccessfulChargeTransaction_TC_006() throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        assertsValidTransaction(tokenTransaction);
        assertsValidTransaction(chargeTransaction);
    }

    @Test
    public void shouldReturnNonSuccessfulChargeTransactionUsedToken_TC_007() throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        Transaction secondChargeTransaction = kushki.charge(token, amount);

        assertsValidTransaction(tokenTransaction);
        assertsValidTransaction(chargeTransaction);
        assertsTransaction(secondChargeTransaction, false, "El token de la transacción ha expirado", "575");
    }

    @Test
    public void shouldReturnNonSuccessfulChargeTransactionInvalidToken_TC_008() throws JsonProcessingException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        String token = "k7jwynu59sd28wu81i2ygsyvllyfimju";

        Transaction chargeTransaction = kushki.charge(token, amount);

        assertsTransaction(chargeTransaction, false, "El token de la transacción no es válido", "574");
    }
}

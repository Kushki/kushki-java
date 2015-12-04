package testrun;

import com.kushkipagos.Kushki;
import com.kushkipagos.KushkiException;
import com.kushkipagos.Transaction;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by lmunda on 12/3/15 08:58.
 */
public final class Main {
    private static final Logger LOG = Logger.getLogger(Main.class.getName());

    private Main() {

    }

    public static void main(String args[]) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, InvalidKeySpecException, BadPaddingException, IllegalBlockSizeException, KushkiException {
        String merchant_id = "10000000123454545454546546";
        String language = "es";
        String currency = "USD";
        String token = "s25s784a87ad497af797a48sdg7rhy4d";
        String amount = "100.50";
        Kushki kushki = new Kushki(merchant_id, language, currency);

        Transaction testTransaction = kushki.charge(token, amount);
        LOG.log(Level.INFO, "success: " + testTransaction.isSuccessful());
        LOG.log(Level.INFO, "response: " + testTransaction.getResponseBody());
        if (testTransaction.isSuccessful()) {
            LOG.log(Level.INFO, "response_text: " + testTransaction.getResponseText());
            LOG.log(Level.INFO, "ticket_number: " + testTransaction.getTicketNumber());
        }
    }
}

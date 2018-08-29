package com.kushkipagos.integration;

import com.kushki.Kushki;
import com.kushki.enums.KushkiAdjustSubscription;
import com.kushki.enums.KushkiPeriodicitySubscriptionType;
import com.kushki.to.Amount;
import com.kushki.to.ContactDetail;
import com.kushki.to.SubscriptionInfo;
import com.kushki.to.Transaction;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static com.kushkipagos.integration.IntegrationHelper.getKushkiTESTECCommerce;
import static org.hamcrest.MatcherAssert.assertThat;

public class KushkiSuscriptionTest {


    private JSONObject longTestJSON;

    @Before
    public void setUp() throws Exception {
        longTestJSON = new JSONObject("{\"glossary\":{\"title\":\"example glossary\",\"GlossDiv\":{\"title\":\"S\",\"GlossList\":{\"GlossEntry\":{\"ID\":\"SGML\",\"SortAs\":\"SGML\",\"GlossTerm\":\"Standard Generalized Markup Language\",\"Acronym\":\"SGML\",\"Abbrev\":\"ISO 8879:1986\",\"GlossDef\":{\"para\":\"A meta-markup language, used to create markup languages such as DocBook.\",\"GlossSeeAlso\":[\"GML\",\"XML\"]},\"GlossSee\":\"markup\"}}}}}");
    }

    @Test
    public void goodSubscriptionTest() {
        Kushki kushki = getKushkiTESTECCommerce();
        Amount amount = new Amount(100d, 12d, 0d, 0d);
        String token = IntegrationHelper.getValidSubscriptionChargeToken(getKushkiTESTECCommerce());
        Date date = new Date();
        date.setYear(date.getYear()+1);
        try {
            Transaction transaction = kushki.subscription(token, amount, null, new SubscriptionInfo("SuperPLAN", KushkiPeriodicitySubscriptionType.MONTLY,
                    date, new ContactDetail("Heidi", "Nino", "user@user.com")));
            assertThat("Good subscription", transaction.isSuccessful());
            assertThat("get the subscription id", transaction.getSubscriptionId().length() > 0);
        } catch (Exception e) {
            assertThat("The test throw a exception", false);
            e.printStackTrace();
        }
    }

    @Test
    public void goodSubscriptionUpdateCardTest() {
        Kushki kushki = getKushkiTESTECCommerce();
        Amount amount = new Amount(100d, 12d, 0d, 0d);
        String token = IntegrationHelper.getValidSubscriptionChargeToken(getKushkiTESTECCommerce());
        Date date = new Date();
        date.setYear(date.getYear()+1);
        try {
            Transaction transaction = kushki.subscription(token, amount, null, new SubscriptionInfo("SuperPLAN", KushkiPeriodicitySubscriptionType.MONTLY,
                    date, new ContactDetail("Heidi", "Nino", "user@user.com")));
            assertThat("Good Subscription", transaction.isSuccessful());
            String subscriptionId = transaction.getSubscriptionId();
            assertThat("get the subscription id", transaction.getSubscriptionId().length() > 0);
            String tokenUpdate = IntegrationHelper.getValidSubscriptionChargeToken(getKushkiTESTECCommerce());
            transaction = kushki.updateSubscriptionCard(tokenUpdate, subscriptionId);
            assertThat("The transaction to update fail", transaction.isSuccessful());
        } catch (Exception e) {
            assertThat("The test throw a exception", false);
            e.printStackTrace();
        }
    }

    @Test
    public void goodSubscriptionAdjustmentTest() {
        Kushki kushki = getKushkiTESTECCommerce();
        Amount amount = new Amount(100d, 12d, 0d, 0d);
        String token = IntegrationHelper.getValidSubscriptionChargeToken(getKushkiTESTECCommerce());
        Date date = new Date();
        date.setYear(date.getYear()+1);
        try {
            Transaction transaction = kushki.subscription(token, amount, null, new SubscriptionInfo("SuperPLAN", KushkiPeriodicitySubscriptionType.MONTLY,
                    date, new ContactDetail("Heidi", "Nino", "user@user.com")));
            assertThat("Good Subscription", transaction.isSuccessful());
            String subscriptionId = transaction.getSubscriptionId();
            assertThat("get the subscription id", subscriptionId.length() > 0);
            Thread.sleep(5000);
            transaction = kushki.adjustSubscription(subscriptionId, new Date(), 2, new Amount(10d, 1d, 0d, 0d), KushkiAdjustSubscription.DISCOUNT);
            assertThat("Good subscription", transaction.isSuccessful());
        } catch (Exception e) {
            assertThat("The test throw a exception", false);
            e.printStackTrace();
        }
    }

    @Test
    public void goodUpdateSubscriptionTestNullMetadata() {
        Kushki kushki = getKushkiTESTECCommerce();
        Amount amount = new Amount(100d, 12d, 0d, 0d);
        String token = IntegrationHelper.getValidSubscriptionChargeToken(getKushkiTESTECCommerce());
        Date date = new Date();
        date.setYear(date.getYear()+1);
        try {
            Transaction transaction = kushki.subscription(token, amount, null, new SubscriptionInfo("SuperPLAN", KushkiPeriodicitySubscriptionType.MONTLY,
                    date, new ContactDetail("Heidi", "Nino", "user@user.com")));
            String transaccionId = transaction.getSubscriptionId();
            transaction = kushki.updateSubscription(amount, null, new SubscriptionInfo("SuperPLAN", KushkiPeriodicitySubscriptionType.MONTLY,
                    date, new ContactDetail("Heidi", "Nino", "user@user.com")), transaccionId);
            assertThat("Good update", transaction.isSuccessful());
        } catch (Exception e) {
            assertThat("The test throw a exception", false);
            e.printStackTrace();
        }
    }

    @Test
    public void goodUpdateSubscriptionTestAmountNull() {
        Kushki kushki = getKushkiTESTECCommerce();
        Amount amount = new Amount(100d, 12d, 0d, 0d);
        String token = IntegrationHelper.getValidSubscriptionChargeToken(getKushkiTESTECCommerce());
        Date date = new Date();
        date.setYear(date.getYear()+1);
        try {
            Transaction transaction = kushki.subscription(token, amount, null, new SubscriptionInfo("SuperPLAN", KushkiPeriodicitySubscriptionType.MONTLY,
                    date, new ContactDetail("Heidi", "Nino", "user@user.com")));
            String transaccionId = transaction.getSubscriptionId();
            transaction = kushki.updateSubscription(null, longTestJSON, new SubscriptionInfo("SuperPLAN", KushkiPeriodicitySubscriptionType.MONTLY,
                    date, new ContactDetail("Heidi", "Nino", "user@user.com")), transaccionId);
            assertThat("Good update", transaction.isSuccessful());
        } catch (Exception e) {
            assertThat("The test throw a exception", false);
            e.printStackTrace();
        }
    }



    @Test
    public void deleteSubscription() {
        try {
            Kushki kushki = getKushkiTESTECCommerce();
            Amount amount = new Amount(100d, 12d, 0d, 0d);
            String token = IntegrationHelper.getValidSubscriptionChargeToken(getKushkiTESTECCommerce());
            Date date = new Date();
            date.setYear(date.getYear()+1);
            Transaction transaction = kushki.subscription(token, amount, null, new SubscriptionInfo("SuperPLAN", KushkiPeriodicitySubscriptionType.MONTLY,
                    date, new ContactDetail("Heidi", "Nino", "user@user.com")));
            String subscriptionId = transaction.getSubscriptionId();
            Thread.sleep(10000);
            Transaction deleteSubscription = kushki.deleteSubscription(subscriptionId);
            assertThat("Error in delete transaction:"+transaction+" / subsc:"+deleteSubscription, deleteSubscription.isSuccessful());
        } catch (Exception e) {
            assertThat("The test throw a exception", false);
            e.printStackTrace();
        }
    }


    @Test
    public void goodUpdateSubscriptionTestNullSubscriptionInfo() {
        Kushki kushki = getKushkiTESTECCommerce();
        Amount amount = new Amount(100d, 12d, 0d, 0d);
        String token = IntegrationHelper.getValidSubscriptionChargeToken(getKushkiTESTECCommerce());
        try {
            Transaction transaction = kushki.subscription(token, amount, longTestJSON,  new SubscriptionInfo("SuperPLAN", KushkiPeriodicitySubscriptionType.MONTLY,
                    new Date(), new ContactDetail("Heidi", "Nino", "user@user.com")));
            String transaccionId = transaction.getSubscriptionId();
            transaction = kushki.updateSubscription(amount, longTestJSON, null, transaccionId);
            assertThat("Good update", transaction.isSuccessful());
        } catch (Exception e) {
            assertThat("The test throw a exception", false);
            e.printStackTrace();
        }
    }


    @Test
    public void badUpdateSubscriptionTest() {
        Kushki kushki = getKushkiTESTECCommerce();
        Amount amount = new Amount(100d, 12d, 0d, 0d);
        String token = IntegrationHelper.getValidSubscriptionChargeToken(getKushkiTESTECCommerce());
        try {
            Transaction transaction = kushki.updateSubscription(amount, null, new SubscriptionInfo("SuperPLAN", KushkiPeriodicitySubscriptionType.MONTLY,
                    new Date(), new ContactDetail("Heidi", "Nino", "user@user.com")), "123");
            assertThat("Good Charge", !transaction.isSuccessful());
        } catch (Exception e) {
            assertThat("The test throw a exception", false);
            e.printStackTrace();
        }
    }

    @Test
    public void goodSubscriptionChargeTest() {
        Kushki kushki = getKushkiTESTECCommerce();
        Amount amount = new Amount(100d, 12d, 0d, 0d);
        String token = IntegrationHelper.getValidSubscriptionChargeToken(getKushkiTESTECCommerce());
        try {
            Transaction transaction = kushki.subscription(token, amount, null, new SubscriptionInfo("SuperPLAN", KushkiPeriodicitySubscriptionType.MONTLY,
                    new Date(), new ContactDetail("Heidi", "Nino", "user@user.com")));
            assertThat("Good Charge", transaction.isSuccessful());
            assertThat("get the ticket of a wonderful charge", transaction.getSubscriptionId().length() > 0);
            String subscriptionId = transaction.getSubscriptionId();
            transaction = kushki.subscriptionCharge("123", new Amount(100d, 12d, 0d, 0d), longTestJSON, subscriptionId);
            assertThat("Good Charge", transaction.isSuccessful());
            assertThat("get the ticket of a wonderful charge", transaction.getTicketNumber().length() > 0);
            transaction = kushki.subscriptionCharge(null, new Amount(100d, 12d, 0d, 0d), longTestJSON, subscriptionId);
            assertThat("Good Charge", transaction.isSuccessful());
            assertThat("get the ticket of a wonderful charge", transaction.getTicketNumber().length() > 0);
            transaction = kushki.subscriptionCharge("123", null, longTestJSON, subscriptionId);
            assertThat("Good Charge", transaction.isSuccessful());
            transaction = kushki.subscriptionCharge("123", new Amount(100d, 12d, 0d, 0d), null, subscriptionId);
            assertThat("Good Charge", transaction.isSuccessful());
        } catch (Exception e) {
            assertThat("The test throw a exception", false);
            e.printStackTrace();
        }
    }

    @Test
    public void badSubscriptionChargeTest() {
        Kushki kushki = getKushkiTESTECCommerce();
        Amount amount = new Amount(100d, 12d, 0d, 0d);
        String token = IntegrationHelper.getValidSubscriptionChargeToken(getKushkiTESTECCommerce());
        try {
            Transaction transaction = kushki.subscription(token, amount, null, new SubscriptionInfo("SuperPLAN", KushkiPeriodicitySubscriptionType.MONTLY,
                    new Date(), new ContactDetail("Heidi", "Nino", "user@user.com")));
            assertThat("Good Charge", transaction.isSuccessful());
            assertThat("get the ticket of a wonderful charge", transaction.getSubscriptionId().length() > 0);
            transaction = kushki.subscriptionCharge("123", new Amount(100d, 12d, 0d, 0d), longTestJSON, null);
            assertThat("normal fail Charge", !transaction.isSuccessful());
            assertThat("must be Empty thischarge", transaction.getTicketNumber() == "");
        } catch (Exception e) {
            assertThat("The test throw a exception", false);
            e.printStackTrace();
        }
    }

}

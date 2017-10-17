package com.kushkipagos.unit;


import com.kushki.enums.KushkiEnvironment;
import com.kushki.enums.KushkiPeriodicitySubscriptionType;
import com.kushki.Kushki;
import com.kushki.ParametersBuilder;
import com.kushki.to.Amount;
import com.kushki.to.ContactDetail;
import com.kushki.to.SubscriptionInfo;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.MatcherAssert.assertThat;

public class KushkiSuscriptionParameterBuilderTest {
    private Kushki kushki;
    private JSONObject longTestJSON;

    @Before
    public void setUp() throws Exception {
        String merchantId = randomAlphabetic(10);
        String language = randomAlphabetic(2);
        String currency = randomAlphabetic(3);
        kushki = new Kushki(merchantId, language, currency, KushkiEnvironment.TESTING);
        longTestJSON = new JSONObject("{\"glossary\":{\"title\":\"example glossary\",\"GlossDiv\":{\"title\":\"S\",\"GlossList\":{\"GlossEntry\":{\"ID\":\"SGML\",\"SortAs\":\"SGML\",\"GlossTerm\":\"Standard Generalized Markup Language\",\"Acronym\":\"SGML\",\"Abbrev\":\"ISO 8879:1986\",\"GlossDef\":{\"para\":\"A meta-markup language, used to create markup languages such as DocBook.\",\"GlossSeeAlso\":[\"GML\",\"XML\"]},\"GlossSee\":\"markup\"}}}}}");

    }


    @Test
    public void happySubscriptionTest() {
        String token = randomAlphabetic(10);
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date date = formatter.parse("30/07/1987");
            JSONObject response = ParametersBuilder.getSubscriptionParams(
                    this.kushki, token, new Amount(100d, 12d, 0d, 0d), longTestJSON,
                    new SubscriptionInfo("SuperPLAN", KushkiPeriodicitySubscriptionType.MONTLY,
                            date, new ContactDetail("Heidi", "Nino", "user@user.com")));
            JSONAssert.assertEquals("Bad JSON answer. The answer is " + response.toString(),
                    "{\"amount\":{\"subtotalIva0\":0,\"iva\":12,\"subtotalIva\":100,\"ice\":0,\"currency\":\"" + kushki.getCurrency() + "\"}," +
                            "\"periodicity\":\"monthly\",\"planName\":\"SuperPLAN\",\"language\":\"" + kushki.getLanguage() + "\"," +
                            "\"contactDetails\":{\"firstName\":\"Heidi\",\"lastName\":\"Nino\",\"email\":\"user@user.com\"}," +
                            "\"startDate\":\"1987-07-30\",\"token\":\"" + token + "\"," +
                            " \"metadata\":{\"glossary\":{\"title\":\"example glossary\",\"GlossDiv\":{\"title\":\"S\",\"GlossList\":{\"GlossEntry\":{\"ID\":\"SGML\",\"SortAs\":\"SGML\",\"GlossTerm\":\"Standard Generalized Markup Language\",\"Acronym\":\"SGML\",\"Abbrev\":\"ISO 8879:1986\",\"GlossDef\":{\"para\":\"A meta-markup language, used to create markup languages such as DocBook.\",\"GlossSeeAlso\":[\"GML\",\"XML\"]},\"GlossSee\":\"markup\"}}}}}" +
                            "}", response, true);
        } catch (Exception e) {
            assertThat("this line must be unreachable", false);
        }
    }

    @Test
    public void happyChargeSubscriptionTestWithOptionalData() {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            JSONObject response = ParametersBuilder.getSubscriptionChargeParams("123", new Amount(100d, 12d, 0d, 0d), longTestJSON, kushki);
            JSONAssert.assertEquals("Bad JSON answer. The answer is " + response.toString(),
                    "{\"cvv\":\"123\"" +
                            ",\"amount\":{\"subtotalIva0\":0,\"iva\":12,\"subtotalIva\":100,\"ice\":0,\"currency\":\""+kushki.getCurrency()+"\"}" +
                            ",\"metadata\":{\"glossary\":{\"title\":\"example glossary\",\"GlossDiv\":{\"GlossList\":{\"GlossEntry\":{\"GlossTerm\":\"Standard Generalized Markup Language\",\"GlossSee\":\"markup\",\"SortAs\":\"SGML\",\"GlossDef\":{\"para\":\"A meta-markup language, used to create markup languages such as DocBook.\",\"GlossSeeAlso\":[\"GML\",\"XML\"]},\"ID\":\"SGML\",\"Acronym\":\"SGML\",\"Abbrev\":\"ISO 8879:1986\"}},\"title\":\"S\"}}}" +
                            ",\"language\":\""+kushki.getLanguage()+"\"}"
                    , response, true);
        } catch (Exception e) {
            assertThat("this line must be unreachable", false);
        }
    }
    @Test
    public void happyChargeSubscriptionTestWithoutOptionalData() {
        try {
            JSONObject response = ParametersBuilder.getSubscriptionChargeParams(null,null,null, kushki);
            JSONAssert.assertEquals("Bad JSON answer. The answer is " + response.toString(),
                    "{\"language\":\""+kushki.getLanguage()+"\"}\n"
                    , response, true);
        } catch (Exception e) {
            assertThat("this line must be unreachable", false);
        }
    }

    @Test
    public void happySubscriptionTestWithoutMetadata() {
        String token = randomAlphabetic(10);
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date date = formatter.parse("30/07/1987");
            JSONObject response = ParametersBuilder.getSubscriptionParams(
                    this.kushki, token, new Amount(100d, 12d, 0d, 0d), null,
                    new SubscriptionInfo("SuperPLAN", KushkiPeriodicitySubscriptionType.MONTLY,
                            date, new ContactDetail("Heidi", "Nino", "user@user.com")));
            JSONAssert.assertEquals("Bad JSON answer. The answer is " + response.toString(),
                    "{\"amount\":{\"subtotalIva0\":0,\"iva\":12,\"subtotalIva\":100,\"ice\":0,\"currency\":\"" + kushki.getCurrency() + "\"}," +
                            "\"periodicity\":\"monthly\",\"planName\":\"SuperPLAN\",\"language\":\"" + kushki.getLanguage() + "\"," +
                            "\"contactDetails\":{\"firstName\":\"Heidi\",\"lastName\":\"Nino\",\"email\":\"user@user.com\"}," +
                            "\"startDate\":\"1987-07-30\",\"token\":\"" + token + "\"" +
                            "}", response, true);
        } catch (Exception e) {
            assertThat("this line must be unreachable", false);
        }
    }

    @Test
    public void SubscriptionTestWithoutDate() {
        String token = randomAlphabetic(10);
        try {
            JSONObject response = ParametersBuilder.getSubscriptionParams(
                    this.kushki, token, new Amount(100d, 12d, 0d, 0d), null,
                    new SubscriptionInfo("SuperPLAN", KushkiPeriodicitySubscriptionType.MONTLY,
                            null, new ContactDetail("Heidi", "Nino", "user@user.com")));
            assertThat("this line must be unreachable", false);
        } catch (Exception e) {
            assertThat("this line must be execute because Date is null", true);
        }
    }

    @Test
    public void SubscriptionTestWithoutSubscription() {
        String token = randomAlphabetic(10);
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date date = formatter.parse("30/07/1987");
            JSONObject response = ParametersBuilder.getSubscriptionParams(
                    this.kushki, token, new Amount(100d, 12d, 0d, 0d), null,
                    null);
            assertThat("this line must be unreachable", false);
        } catch (Exception e) {
            assertThat("this line must be execute because Date is null", true);
        }
    }

    @Test
    public void SubscriptionTestWithoutAmount() {
        String token = randomAlphabetic(10);
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date date = formatter.parse("30/07/1987");
            JSONObject response = ParametersBuilder.getSubscriptionParams(
                    this.kushki, token, null, null,
                    new SubscriptionInfo("SuperPLAN", KushkiPeriodicitySubscriptionType.MONTLY,
                            date, new ContactDetail("Heidi", "Nino", "user@user.com")));
            assertThat("this line must be unreachable", false);
        } catch (Exception e) {
            assertThat("this line must be execute because Date is null", true);
        }
    }

}


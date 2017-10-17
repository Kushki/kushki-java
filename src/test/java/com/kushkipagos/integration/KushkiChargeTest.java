package com.kushkipagos.integration;

import com.kushki.*;
import com.kushki.to.Amount;
import com.kushki.to.ExtraTaxes;
import com.kushki.to.Transaction;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static com.kushkipagos.integration.IntegrationHelper.getKushkiTESTCOCommerce;
import static org.hamcrest.MatcherAssert.*;

import static com.kushkipagos.integration.IntegrationHelper.getKushkiTESTECCommerce;

public class KushkiChargeTest {


    private JSONObject longTestJSON;

    @Before
    public void setUp() throws Exception {
        longTestJSON = new JSONObject("{\"glossary\":{\"title\":\"example glossary\",\"GlossDiv\":{\"title\":\"S\",\"GlossList\":{\"GlossEntry\":{\"ID\":\"SGML\",\"SortAs\":\"SGML\",\"GlossTerm\":\"Standard Generalized Markup Language\",\"Acronym\":\"SGML\",\"Abbrev\":\"ISO 8879:1986\",\"GlossDef\":{\"para\":\"A meta-markup language, used to create markup languages such as DocBook.\",\"GlossSeeAlso\":[\"GML\",\"XML\"]},\"GlossSee\":\"markup\"}}}}}");
    }


    @Test
    public void goodChargeTest() {
        Kushki kushki = getKushkiTESTECCommerce();
        Amount ammount = new Amount(100d, 12d, 0d, 0d);
        String token = IntegrationHelper.getValidChargeToken(getKushkiTESTECCommerce(), ammount);
        try {
            Transaction charge = kushki.charge(token, ammount, null, null);
            assertThat("Good Charge", charge.isSuccessful());
            assertThat("get the ticket of a wonderfull charge", charge.getTicketNumber().length() > 0);
        } catch (Exception e) {
            assertThat("The test break", false);
            e.printStackTrace();
        }
    }

    @Test
    public void goodChargeTestWithExtraTax() {
        Kushki kushki = getKushkiTESTCOCommerce();
        Amount amount = new Amount(90d, 12d, 0d, 0d);
        amount.setExtraTaxes(new ExtraTaxes(1d, 2d, 3d, 4d));
        String token = IntegrationHelper.getValidChargeToken(getKushkiTESTCOCommerce(), amount);
        try {
            Transaction charge = kushki.charge(token, amount, null, null);
            assertThat("Good Charge with Extra Tax" + amount.getTotalAmount(), charge.isSuccessful());
            assertThat("get the ticket of a wonderful charge with extra tax", charge.getTicketNumber().length() > 0);
        } catch (Exception e) {
            assertThat("The test break", false);
            e.printStackTrace();
        }
    }

    @Test
    public void goodChargeTestWithMetadata() {
        Kushki kushki = getKushkiTESTECCommerce();
        Amount amount = new Amount(100d, 12d, 0d, 0d);
        String token = IntegrationHelper.getValidChargeToken(getKushkiTESTECCommerce(), amount);
        try {
            Transaction charge = kushki.charge(token, amount, null, longTestJSON);
            assertThat("Good Charge", charge.isSuccessful());
        } catch (Exception e) {
            assertThat("The test break", false);
            e.printStackTrace();
        }
    }

    @Test
    public void goodChargeWithDeferredTest() {
        Kushki kushki = getKushkiTESTCOCommerce();
        Amount ammount = new Amount(100d, 12d, 0d, 0d);
        String token = IntegrationHelper.getValidChargeToken(getKushkiTESTCOCommerce(), ammount);
        try {
            Transaction charge = kushki.charge(token, ammount, 12, null);
            assertThat("Good Charge", charge.isSuccessful());
        } catch (Exception e) {
            assertThat("The test break", false);
            e.printStackTrace();
        }
    }

    @Test
    public void goodChargeWithDeferredAndMetadataTest() {
        Kushki kushki = getKushkiTESTCOCommerce();
        Amount ammount = new Amount(100d, 12d, 0d, 0d);
        String token = IntegrationHelper.getValidChargeToken(getKushkiTESTCOCommerce(), ammount);
        try {
            Transaction charge = kushki.charge(token, ammount, 12, longTestJSON);
            assertThat("Good Charge", charge.isSuccessful());
        } catch (Exception e) {
            assertThat("The test break", false);
            e.printStackTrace();
        }
    }

    @Test
    public void badCommerceCharge() {
        Kushki kushki = getKushkiTESTECCommerce();
        Amount ammount = new Amount(100d, 12d, 0d, 0d);
        String token = IntegrationHelper.getValidChargeToken(getKushkiTESTCOCommerce(), ammount);
        try {
            Transaction charge = kushki.charge(token, ammount, 12, null);
            assertThat("Good Charge", !charge.isSuccessful());
        } catch (Exception e) {
            assertThat("The test break", false);
            e.printStackTrace();
        }
    }


    @Test
    public void invalidTotalChargeTest() {
        Kushki kushki = getKushkiTESTECCommerce();
        Amount ammount = new Amount(100d, 12d, 0d, 0d);
        Amount ammountChanged = new Amount(120d, 12d, 0d, 0d);
        String token = IntegrationHelper.getValidChargeToken(getKushkiTESTECCommerce(), ammount);
        try {
            Transaction charge = kushki.charge(token, ammountChanged, null, null);
            assertThat("The total Amount of the Charge was broken", !charge.isSuccessful());
        } catch (Exception e) {
            assertThat("The test break", false);
            e.printStackTrace();
        }
    }

    @Test
    public void invalidTokenChargeTest() {
        Kushki kushki = getKushkiTESTECCommerce();
        Amount amount = new Amount(100d, 12d, 0d, 0d);
        String token = IntegrationHelper.getValidChargeToken(getKushkiTESTECCommerce(), amount);
        try {
            Transaction charge = kushki.charge(token.concat("AA"), amount, null, null);
            assertThat("The total Amount of the Charge was broken", !charge.isSuccessful());
        } catch (Exception e) {
            assertThat("The test break", false);
            e.printStackTrace();
        }
    }

    @Test
    public void testGoodVoidCharge() {
        Kushki kushki = getKushkiTESTECCommerce();
        Amount amount = new Amount(100d, 12d, 0d, 0d);
        String token = IntegrationHelper.getValidChargeToken(getKushkiTESTECCommerce(), amount);
        try {
            Transaction charge = kushki.charge(token, amount, null, null);
            assertThat("Good Charge", charge.isSuccessful());
            Thread.sleep(5000);
            Transaction voidCharge = kushki.voidCharge(charge.getTicketNumber());
            assertThat("Good Void", voidCharge.isSuccessful());
        } catch (Exception e) {
            assertThat("The test break", false);
            e.printStackTrace();
        }
    }

    //TODO this test get Timeout at uat environment
    //@Test
    public void testRefundCharge() {
        Kushki kushki = getKushkiTESTECCommerce();
        Amount amount = new Amount(100d, 12d, 0d, 0d);
        String token = IntegrationHelper.getValidChargeToken(getKushkiTESTECCommerce(), amount);
        try {
            Transaction charge = kushki.charge(token, amount, null, null);
            assertThat("Fail the charge", charge.isSuccessful());
            Thread.sleep(5000);
            Transaction refundCharge = kushki.refundCharge(charge.getTicketNumber());
            System.out.println(refundCharge);
            assertThat("Fail de refund", refundCharge.isSuccessful());
        } catch (Exception e) {
            assertThat("The test break", false);
            e.printStackTrace();
        }
    }


}

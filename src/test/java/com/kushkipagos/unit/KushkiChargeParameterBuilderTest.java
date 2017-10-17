package com.kushkipagos.unit;


import com.kushki.*;
import com.kushki.enums.KushkiEnvironment;
import com.kushki.to.Amount;
import com.kushki.to.ExtraTaxes;
import org.junit.Before;
import org.junit.Test;

import org.skyscreamer.jsonassert.JSONAssert;
import org.json.JSONObject;

import java.util.HashMap;

import static org.hamcrest.MatcherAssert.*;


import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

public class KushkiChargeParameterBuilderTest {
    private Kushki kushki;

    @Before
    public void setUp() throws Exception {
        String merchantId = randomAlphabetic(10);
        String language = randomAlphabetic(2);
        String currency = randomAlphabetic(3);
        kushki = new Kushki(merchantId, language, currency, KushkiEnvironment.TESTING);
    }


    @Test
    public void happyTestWithoutMetadataMonthAndCurrency() {
        String token = randomAlphabetic(10);
        try {
            JSONObject response = ParametersBuilder.getChargeParameters(this.kushki, token, new Amount(100d, 12d, 0d, 0d), null, null);
            JSONAssert.assertEquals("Bad JSON answer. The answer is " + response.toString(),
                    "{\"amount\":{\"subtotalIva0\":0,\"iva\":12,\"subtotalIva\":100,\"ice\":0,\"currency\":\"" + kushki.getCurrency() + "\"},\"token\":\"" + token + "\"}", response, true);
        } catch (Exception e) {
            assertThat("this line must be unreachable", false);
        }
    }

    @Test
    public void happyTestWithExtraTax() {
        String token = randomAlphabetic(10);
        try {
            Amount amount = new Amount(100d, 12d, 0d, 0d);
            amount.setExtraTaxes(new ExtraTaxes(1d, 2d, 3d, 4d));
            JSONObject response = ParametersBuilder.getChargeParameters(this.kushki, token, amount, null, null);
            JSONAssert.assertEquals("Bad JSON answer. The answer is " + response.toString(),
                    "{\"amount\":{\"subtotalIva0\":0,\"iva\":12,\"subtotalIva\":100,\"ice\":0,\"extraTaxes\":" +
                            "{\"agenciaDeViaje\":3,\"propina\":1,\"iac\":4,\"tasaAeroportuaria\":2},\"currency\":\"" + kushki.getCurrency() + "\"},\"token\":\"" + token + "\"}", response, true);
        } catch (Exception e) {
            assertThat("this line must be unreachable", false);
        }
    }


    @Test
    public void happyTestWithoutMetadataAndMonthWithCurrency() {
        String token = randomAlphabetic(10);
        try {
            JSONObject response = ParametersBuilder.getChargeParameters(this.kushki, token, new Amount(100d, 12d, 0d, 0d), null, null);
            JSONAssert.assertEquals("Bad JSON answer. The answer is " + response.toString(),
                    "{\"amount\":{\"subtotalIva0\":0,\"iva\":12,\"subtotalIva\":100,\"ice\":0,\"currency\":\"" + kushki.getCurrency() + "\"},\"token\":\"" + token + "\"}", response, true);
        } catch (Exception e) {
            assertThat("this line must be unreachable", false);
        }
    }

    @Test
    public void happyTestMetadataAndMonth() {
        String token = randomAlphabetic(10);
        try {

            HashMap<String, String> metadata = new HashMap<String, String>();
            metadata.put("a", "b");
            metadata.put("c", "d");
            metadata.put("c", "d");
            JSONObject response = ParametersBuilder.getChargeParameters(this.kushki, token, new Amount(100d, 12d, 0d, 0d), 3, new JSONObject(metadata));
            JSONAssert.assertEquals("Bad JSON answer. The answer is " + response.toString(),
                    "{\"months\":3,\"amount\":{\"subtotalIva0\":0,\"iva\":12,\"subtotalIva\":100,\"ice\":0,\"currency\":\"" + kushki.getCurrency() + "\"},\"token\":\"" + token + "\",\"metadata\":{\"a\":\"b\",\"c\":\"d\"}}", response, true);
        } catch (Exception e) {
            assertThat("this line must be unreachable", false);
        }
    }

    @Test
    public void ARequiredFieldIsNull() {
        String token = randomAlphabetic(10);
        try {
            HashMap<String, String> metadata = new HashMap<String, String>();
            metadata.put("a", "b");
            metadata.put("c", "d");
            metadata.put("c", "d");
            JSONObject response = ParametersBuilder.getChargeParameters(this.kushki, token, null, null, new JSONObject(metadata));
            assertThat("this line must be unreachable", false);
        } catch (KushkiException e) {
            assertThat("this must fire a exception", true);
        }
    }
}


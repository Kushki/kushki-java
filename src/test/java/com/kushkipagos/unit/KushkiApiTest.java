package com.kushkipagos.unit;

import com.kushkipagos.Kushki;
import org.junit.Before;
import org.junit.Test;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by lmunda on 2/16/16 12:09.
 */
public class KushkiApiTest {
    private String merchantId;
    private Kushki kushki;
    private String language;

    private static final String URL = "https://ping.auruspay.com/kushki/api/v1";

    @Before
    public void setUp() throws Exception {
        merchantId = randomAlphabetic(10);
        language = randomAlphabetic(2);
        String currency = randomAlphabetic(10);
        kushki = new Kushki(merchantId, language, currency);
    }


    @Test
    public void shouldCreateInstanceWithMerchantId() {
        assertThat(kushki.getMerchantId(), is(merchantId));
    }

    @Test
    public void shouldHaveAPIURL() {
        assertThat(Kushki.URL, is(URL));
    }

    @Test
    public void shouldHaveTokensURL() {
        assertThat(Kushki.TOKENS_URL, is(URL + "/tokens"));
    }

    @Test
    public void shouldHaveChargeURL() {
        assertThat(Kushki.CHARGE_URL, is(URL + "/charge"));
    }

    @Test
    public void shouldHaveDeferredChargeURL() {
        assertThat(Kushki.DEFERRED_CHARGE_URL, is(URL + "/deferred"));
    }

    @Test
    public void shouldHaveVoidURL() {
        assertThat(Kushki.VOID_URL, is(URL + "/void"));
    }

    @Test
    public void shouldHaveRefundURL() {
        assertThat(Kushki.REFUND_URL, is(URL + "/refund"));
    }

    @Test
    public void shouldCreateInstanceWithLanguage() {
        assertThat(kushki.getLanguage(), is(language));
    }

}

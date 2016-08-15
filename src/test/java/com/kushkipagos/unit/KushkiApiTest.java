package com.kushkipagos.unit;

import com.kushkipagos.Kushki;
import com.kushkipagos.KushkiEnvironment;
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
    private String currency;

    @Before
    public void setUp() throws Exception {
        merchantId = randomAlphabetic(10);
        language = randomAlphabetic(2);
        currency = randomAlphabetic(10);
        kushki = new Kushki(merchantId, language, currency);
    }

    @Test
    public void shouldCreateInstanceWithAllDefaultValues() throws Exception {
        kushki = new Kushki(merchantId);
        assertThat(kushki.getMerchantId(), is(merchantId));
        assertThat(kushki.getLanguage(), is("es"));
        assertThat(kushki.getCurrency(), is("USD"));
        assertThat(kushki.getEnvironment(), is(KushkiEnvironment.PRODUCTION));
    }

    @Test
    public void shouldCreateInstanceWithDefaultLanguageAndCurrency() throws Exception {
        kushki = new Kushki(merchantId, KushkiEnvironment.STAGING);
        assertThat(kushki.getMerchantId(), is(merchantId));
        assertThat(kushki.getLanguage(), is("es"));
        assertThat(kushki.getCurrency(), is("USD"));
        assertThat(kushki.getEnvironment(), is(KushkiEnvironment.STAGING));
    }

    @Test
    public void shouldCreateInstanceWithDefaultEnvironment() throws Exception {
        assertThat(kushki.getMerchantId(), is(merchantId));
        assertThat(kushki.getLanguage(), is(language));
        assertThat(kushki.getCurrency(), is(currency));
        assertThat(kushki.getEnvironment(), is(KushkiEnvironment.PRODUCTION));
    }

    @Test
    public void shouldCreateInstanceWithProvidedValues() throws Exception {
        kushki = new Kushki(merchantId, language, currency, KushkiEnvironment.STAGING);
        assertThat(kushki.getMerchantId(), is(merchantId));
        assertThat(kushki.getLanguage(), is(language));
        assertThat(kushki.getCurrency(), is(currency));
        assertThat(kushki.getEnvironment(), is(KushkiEnvironment.STAGING));
    }

    @Test
    public void shouldCreateInstanceWithMerchantId() {
        assertThat(kushki.getMerchantId(), is(merchantId));
    }

    @Test
    public void shouldHaveChargeURL() {
        assertThat(Kushki.CHARGE_URL, is("charge"));
    }

    @Test
    public void shouldHaveDeferredChargeURL() {
        assertThat(Kushki.DEFERRED_CHARGE_URL, is("deferred"));
    }

    @Test
    public void shouldHaveVoidURL() {
        assertThat(Kushki.VOID_URL, is("void"));
    }

    @Test
    public void shouldHaveRefundURL() {
        assertThat(Kushki.REFUND_URL, is("refund"));
    }

    @Test
    public void shouldCreateInstanceWithLanguage() {
        assertThat(kushki.getLanguage(), is(language));
    }

}

package com.kushkipagos.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kushkipagos.Kushki;
import com.kushkipagos.KushkiException;
import com.kushkipagos.Validations;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import static com.kushkipagos.commons.TestsHelpers.getRandomDouble;
import static com.kushkipagos.commons.TestsHelpers.getRandomInteger;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by lmunda on 1/21/16 14:55.
 */

@RunWith(JUnitParamsRunner.class)
public class ValidationsTest {
    private Kushki kushki;

    @Before
    public void setUp() throws Exception {
        String merchantId = randomAlphabetic(10);
        String language = randomAlphabetic(2);
        String currency = randomAlphabetic(10);
        kushki = new Kushki(merchantId, language, currency);
    }

    @Test
    @Parameters(method = "invalidNumbersAndExceptionMessages")
    public void shouldThrowKushkiExceptionIfNumberIsInvalid(Double valor, String exceptionMessage) {
        Exception exception = null;
        try {
            Validations.validateNumber(valor, 1, 12, "El valor");
        } catch (KushkiException e) {
            exception = e;
        }
        Assert.assertNotNull(exception);
        assertThat(exception.getMessage(), is(exceptionMessage));
    }

    @Test
    @Parameters(method = "invalidAmountsAndExceptionMessages")
    public void shouldThrowKushkiExceptionIfVoidChargeAmountIsInvalid(Double amount, String exceptionMessage) {
        Exception exception = null;
        try {
            String token = randomAlphabetic(10);
            kushki.voidCharge(token, amount);
        } catch (KushkiException | BadPaddingException | JsonProcessingException | IllegalBlockSizeException e) {
            exception = e;
        }
        Assert.assertNotNull(exception);
        assertThat(exception.getMessage(), is(exceptionMessage));
    }

    @Test
    @Parameters(method = "invalidAmountsAndExceptionMessages")
    public void shouldThrowKushkiExceptionIfRefundChargeAmountIsInvalid(Double amount, String exceptionMessage) {
        Exception exception = null;
        try {
            String token = randomAlphabetic(10);
            kushki.refundCharge(token, amount);
        } catch (KushkiException | BadPaddingException | JsonProcessingException | IllegalBlockSizeException e) {
            exception = e;
        }
        Assert.assertNotNull(exception);
        assertThat(exception.getMessage(), is(exceptionMessage));
    }

    @SuppressWarnings("unused")
    private Object[][] invalidNumbersAndExceptionMessages() {
        Double tooLong = getRandomDouble(1000000000000.0, 9999999999999999999.9);
        Double negative = -getRandomDouble(1.0, 150.0);
        return new Object[][]{
                {tooLong, "El valor debe tener 12 o menos dígitos"},
                {negative, "El valor debe ser superior o igual a 1"},
                {0.0, "El valor debe ser superior o igual a 1"},
                {null, "El valor no puede ser un valor nulo"}
        };
    }

    @SuppressWarnings("unused")
    private Object[][] invalidAmountsAndExceptionMessages() {
        Double tooLongAmount = getRandomDouble(1000000000000.0, 9999999999999999999.9);
        Double negativeAmount = -getRandomDouble(1.0, 150.0);
        return new Object[][]{
                {tooLongAmount, "El monto debe tener 12 o menos dígitos"},
                {negativeAmount, "El monto debe ser superior o igual a 0"},
//                {0.0, "El monto debe ser superior o igual a 0"},
                {null, "El monto no puede ser un valor nulo"}
        };
    }

    @SuppressWarnings("unused")
    private Object[][] invalidParametersAndExceptionMessages() {
        Double tooLongAmount = getRandomDouble(1000000000000.0, 9999999999999999999.9);
        Double negativeAmount = -getRandomDouble(1.0, 150000000.0);
        Double validAmount = getRandomDouble(1.0, 150000000.0);

        Integer tooLongMonths = getRandomInteger(100, 9999);
        Integer negativeMonths = -getRandomInteger(1, 99);
        Integer validMonths = getRandomInteger(1, 99);

        Double tooLongInterest = getRandomDouble(100.0, 9999999.9);
        Double negativeInterest = -getRandomDouble(0.01, 99.99);
        Double validInterest = getRandomDouble(0.01, 99.99);
        return new Object[][]{
                {tooLongAmount, validMonths, validInterest, "El monto debe tener 12 o menos dígitos"},
                {negativeAmount, validMonths, validInterest, "El monto debe ser superior o igual a 0"},
//                {0.0, validMonths, validInterest, "El monto debe ser superior o igual a 0"},
                {null, validMonths, validInterest, "El monto no puede ser un valor nulo"},

                {validAmount, tooLongMonths, validInterest, "El número de meses debe tener 2 o menos dígitos"},
                {validAmount, negativeMonths, validInterest, "El número de meses debe ser superior o igual a 0"},
//                {validAmount, 0, validInterest, "El número de meses debe ser superior o igual a 0"},
                {validAmount, null, validInterest, "El número de meses no puede ser un valor nulo"},

                {validAmount, validMonths, tooLongInterest, "La tasa de interés debe tener 5 o menos dígitos"},
                {validAmount, validMonths, negativeInterest, "La tasa de interés debe ser superior o igual a 0"},
//                {validAmount, validMonths, 0.0, "La tasa de interés debe ser superior o igual a 0"},
                {validAmount, validMonths, null, "La tasa de interés no puede ser un valor nulo"}
        };
    }
}

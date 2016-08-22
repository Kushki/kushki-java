package com.kushkipagos.unit;

import com.kushkipagos.KushkiException;
import com.kushkipagos.Validations;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.kushkipagos.commons.TestsHelpers.getRandomDouble;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(JUnitParamsRunner.class)
public class ValidationsTest {

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
}

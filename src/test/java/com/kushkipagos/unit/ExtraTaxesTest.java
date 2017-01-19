package com.kushkipagos.unit;

import com.kushkipagos.ExtraTaxes;
import com.kushkipagos.Tax;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class ExtraTaxesTest {
    @Test
    public void shouldCalculateTotalTax() throws Exception {
        ExtraTaxes extraTaxes = new ExtraTaxes(1d, 4d, 5d, 7d);
        Double expectedTotal = 17d;
        assertEquals(extraTaxes.getTotalExtraTaxes(), expectedTotal);
    }

    @Test
    public void shouldTransformTaxToHashArray() throws Exception {
        ExtraTaxes extraTaxes = new ExtraTaxes(1d, 2d, 3d, 4d);
        List<Map<String, String>> expectedExtraTaxes = new ArrayList<>();
        Tax propina = new Tax("3", "PROPINA", 1d);
        Tax tasaAeroportuaria = new Tax("4", "TASA_AERO", 2d);
        Tax agenciaDeViaje = new Tax("5", "TASA_ADMIN_AGEN_COD", 3d);
        Tax iac = new Tax("6", "IAC", 4d);
        expectedExtraTaxes.add(propina.toHash());
        expectedExtraTaxes.add(tasaAeroportuaria.toHash());
        expectedExtraTaxes.add(agenciaDeViaje.toHash());
        expectedExtraTaxes.add(iac.toHash());
        assertThat(extraTaxes.toHashArray(), is(expectedExtraTaxes));
    }

    @Test
    public void shouldTransformTaxToEmptyHashArrayWhenAllTaxesAreZero() throws Exception {
        ExtraTaxes extraTaxes = new ExtraTaxes(0d, 0d, 0d, 0d);
        assertThat(extraTaxes.toHashArray().size(), is(0));
    }

}
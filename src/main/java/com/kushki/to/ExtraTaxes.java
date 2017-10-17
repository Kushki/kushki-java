package com.kushki.to;

import com.kushki.KushkiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExtraTaxes {
    private Tax propina = new Tax("3", "PROPINA", 0d, "La propina");
    private Tax tasaAeroportuaria = new Tax("4", "TASA_AERO", 0d, "La tasa aeroportuaria");
    private Tax agenciaDeViaje = new Tax("5", "TASA_ADMIN_AGEN_COD", 0d, "La agencia de viaje");
    private Tax iac = new Tax("6", "IAC", 0d, "El IAC");

    public ExtraTaxes(Double propina, Double tasaAeroportuaria, Double agenciaDeViaje, Double iac) {
        this.propina.setAmount(propina);
        this.tasaAeroportuaria.setAmount(tasaAeroportuaria);
        this.agenciaDeViaje.setAmount(agenciaDeViaje);
        this.iac.setAmount(iac);
    }

    public Double getTotalExtraTaxes() {
        return propina.getAmount() + tasaAeroportuaria.getAmount() + agenciaDeViaje.getAmount() + iac.getAmount();
    }


    public List<Map<String, String>> toHashArray() throws KushkiException {
        List<Map<String, String>> extraTaxes = new ArrayList<>();
        if (propina.getAmount() > 0) extraTaxes.add(propina.toHash());
        if (tasaAeroportuaria.getAmount() > 0) extraTaxes.add(tasaAeroportuaria.toHash());
        if (agenciaDeViaje.getAmount() > 0) extraTaxes.add(agenciaDeViaje.toHash());
        if (iac.getAmount() > 0) extraTaxes.add(iac.toHash());
        return extraTaxes;
    }

    public Tax getPropina() {
        return propina;
    }

    public Tax getTasaAeroportuaria() {
        return tasaAeroportuaria;
    }

    public Tax getAgenciaDeViaje() {
        return agenciaDeViaje;
    }

    public Tax getIac() {
        return iac;
    }
}

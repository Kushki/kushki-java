package com.kushkipagos;

import java.util.*;

public class ExtraTaxes {
    private Tax propina = new Tax("3", "PROPINA", 0d);
    private Tax tasaAeroportuaria = new Tax("4", "TASA_AERO", 0d);
    private Tax agenciaDeViaje = new Tax("5", "TASA_ADMIN_AGEN_COD", 0d);
    private Tax iac = new Tax("6", "IAC", 0d);

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
        // TODO: Test this
//        String validatedPropina = Validations.validateNumber(propina.getAmount(), 0, 12, "La propina");
//        String validatedTasaAeroportuaria = Validations.validateNumber(tasaAeroportuaria.getAmount(), 0, 12, "La tasa aeroportuaria");
//        String validatedAgenciaDeViaje = Validations.validateNumber(agenciaDeViaje.getAmount(), 0, 12, "La agencia de viaje");
//        String validatedIac = Validations.validateNumber(iac.getAmount(), 0, 12, "El IAC");
        List<Map<String, String>> extraTaxes = new ArrayList<>();
        if (propina.getAmount() > 0) extraTaxes.add(propina.toHash());
        if (tasaAeroportuaria.getAmount() > 0) extraTaxes.add(tasaAeroportuaria.toHash());
        if (agenciaDeViaje.getAmount() > 0) extraTaxes.add(agenciaDeViaje.toHash());
        if (iac.getAmount() > 0) extraTaxes.add(iac.toHash());
        return extraTaxes;
    }
}

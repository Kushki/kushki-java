package com.kushki.to;

import com.kushki.enums.KushkiPeriodicitySubscriptionType;

import java.util.Date;

public class SubscriptionInfo {
    private String planName;
    private KushkiPeriodicitySubscriptionType periodicity;
    private Date startDate;
    private com.kushki.to.ContactDetail contactDetail;


    public SubscriptionInfo(String planName, KushkiPeriodicitySubscriptionType periodicity, Date startDate, ContactDetail contactDetail) {
        this.planName = planName;
        this.periodicity = periodicity;
        this.startDate = startDate;
        this.contactDetail = contactDetail;
    }

    public KushkiPeriodicitySubscriptionType getPeriodicity() {
        return periodicity;
    }

    public void setPeriodicity(KushkiPeriodicitySubscriptionType periodicity) {
        this.periodicity = periodicity;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public ContactDetail getContactDetail() {
        return contactDetail;
    }

    public void setContactDetail(ContactDetail contactDetail) {
        this.contactDetail = contactDetail;
    }
}
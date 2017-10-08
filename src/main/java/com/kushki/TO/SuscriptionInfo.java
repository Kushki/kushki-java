package com.kushki.TO;

import com.kushki.Enum.KushkiPeriodicitySuscriptionEnum;

import java.util.Date;

public class SuscriptionInfo {
    private String planName;
    private KushkiPeriodicitySuscriptionEnum periodicity;
    private Date startDate;
    private com.kushki.TO.ContactDetail contactDetail;


    public SuscriptionInfo(String planName, KushkiPeriodicitySuscriptionEnum periodicity, Date startDate, ContactDetail contactDetail) {
        this.planName = planName;
        this.periodicity = periodicity;
        this.startDate = startDate;
        this.contactDetail = contactDetail;
    }

    public KushkiPeriodicitySuscriptionEnum getPeriodicity() {
        return periodicity;
    }

    public void setPeriodicity(KushkiPeriodicitySuscriptionEnum periodicity) {
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
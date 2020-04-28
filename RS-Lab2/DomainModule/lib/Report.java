/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs_domain.lib;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author tiagogomes
 */
public class Report {
    private String id;
    private Date referenceDate;
    private String state;
    private Contract contract;

    public Report(String id, Date referenceDate, Contract contract) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(referenceDate);
        this.referenceDate = referenceDate; 
        this.id = id;
        int lastPaymentDay = calendar.get(Calendar.DAY_OF_MONTH);
        String contractState = "";
        if (lastPaymentDay == 1) {
            this.state = "NORMAL";
        } else if (lastPaymentDay >= 2 && lastPaymentDay < 31) {
            this.state = "COMPLIANCE";
        } else {
            this.state = "DEFAULT";
        }
        this.contract = contract;
    }
    
    public Report(String id, Date referenceDate, Contract contract, String state) {
        this.id = id;
        this.referenceDate = referenceDate;
        this.contract = contract;
        this.state = state;
    }

    public Date getReferenceDate() {
        return referenceDate;
    }

    public String getState() {
        return state;
    }
    
    public String getId() {
        return id;
    }

    public void setReferenceDate(Date referenceDate) {
        this.referenceDate = referenceDate;
    }

    @Override
    public String toString() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return "Report{" + "referenceDate=" + formatter.format(referenceDate) + ", state=" + state + ", contract=" + Math.round(contract.getAmount()) + "€, "+ "description: "+ contract.getDescription() + '}';
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public Contract getContract() {
        return contract;
    }
}

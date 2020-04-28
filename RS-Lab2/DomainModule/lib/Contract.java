/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs_domain.lib;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs_domain.lib.exceptions.InvalidArgumentException;

/**
 *
 * @author tiagogomes
 */
public class Contract {
    private String id;
    private String description;
    private double amount;
    private Date startDate;
    private Date endDate;
    private ArrayList<Cashflow> cashflows;
    
    public Contract(String id, String description, double amount, Date startDate, Date endDate) {
        this.description = description;
        this.amount = amount;
        this.startDate = startDate;
        this.id = id;
        this.endDate = endDate;
    }
    
    public void addCashFlow(Date referenceDate) {
        Cashflow cashflow = new Cashflow(referenceDate);
        this.cashflows.add(cashflow);
        
        ArrayList<String> fields = new ArrayList<>();
        fields.add("referenceDate");
        
        ArrayList<String> values = new ArrayList<>();
        fields.add(referenceDate.toString());
        
        try {
            Database.insert("CashFlows", fields, values);
        } catch (InvalidArgumentException ex) {
            Logger.getLogger(Contract.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getDescription() {
        return description;
    }
    
    public String getId() {
        return id;
    }
    
    public void setCashflows(ArrayList<Cashflow> cashflows) {
        this.cashflows = cashflows;
    }

    public double getAmount() {
        return amount;
    }
    
    public ArrayList<Cashflow> getCashflows() {
        return cashflows; 
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "Contract{ description=" + description + ", amount=" + amount + ", startDate=" + startDate + ", endDate=" + endDate + '}';
    }
    
    
    
}

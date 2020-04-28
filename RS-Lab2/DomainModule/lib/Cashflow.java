/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs_domain.lib;

import java.util.Date;

/**
 *
 * @author tiagogomes
 */
public class Cashflow {
    private Date referenceDate;

    public Cashflow(Date referenceDate) {
        this.referenceDate = referenceDate;
    }

    @Override
    public String toString() {
        return "Cashflow{ referenceDate=" + referenceDate +" + '}';";
    }
    
    
    
    public Date getReferenceDate() {
        return this.referenceDate;
    }

    public void setReferenceDate(Date referenceDate) {
        this.referenceDate = referenceDate;
    }

}

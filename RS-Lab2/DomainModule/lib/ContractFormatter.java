/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs_domain.lib;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tiagogomes
 */
public class ContractFormatter {
    
    public ArrayList<Contract> getContracts() {
        ArrayList<Contract> contracts = new ArrayList<>();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            ResultSet contractsSet = Database.select("Contracts", null, null);
            
            while(contractsSet.next()) {
                contracts.add(new Contract(contractsSet.getString("id"),
                        contractsSet.getString("description"),
                        contractsSet.getFloat("amount"), null, null));
            }
            
            
            for(int i = 0 ; i < contracts.size(); i++) {
                    Contract contract = contracts.get(i);
                    ArrayList<Cashflow> cashflows = new ArrayList<>();

                    String startDate = "2020-03-01";
                    String endDate = "2020-03-30";

                    ResultSet cashflowResultSet = Database.select("Cashflows", null, "WHERE reference_date BETWEEN '" + startDate + 
                        "' AND '" + endDate + "' AND contract_id = " + contract.getId() + " ORDER BY date(reference_date) DESC LIMIT 1 ");

                    while(cashflowResultSet.next()) {
                        cashflows.add(new Cashflow(dateFormatter.parse(cashflowResultSet.getString("reference_date"))));
                    }
                    contract.setCashflows(cashflows);
            }
            
            
        } catch (Exception ex) {
            Logger.getLogger(ContractFormatter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return contracts;
    }
    
    
}

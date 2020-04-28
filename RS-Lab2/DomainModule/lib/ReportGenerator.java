/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs_domain.lib;

import com.sun.istack.internal.Nullable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs_domain.lib.cli.IActionable;
import rs_domain.lib.exceptions.InvalidArgumentException;

/**
 *
 * @author tiagogomes
 */
public class ReportGenerator implements IActionable {
    private String type;
    
    public ReportGenerator(String type) {
        this.type = type;
    }
    
    private void saveReports() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        int generatedReports = 0;
        try {
            ContractFormatter contractFormatter = new ContractFormatter();
            ArrayList<Contract> contracts = contractFormatter.getContracts();
            
            for(Contract contract : contracts) {
                ArrayList<Cashflow> cashflows = contract.getCashflows();
                
                for(int j = 0; j < cashflows.size(); j++) {
                    Cashflow cashflow = cashflows.get(j);

                    Report report = new Report(generatedReports+"", cashflow.getReferenceDate(), contract);

                    Date now = new Date(System.currentTimeMillis());
                    ArrayList<String> fields = new ArrayList<>();
                    fields.add("id"); fields.add("reference_date"); fields.add("state"); fields.add("contract_id");
                    ArrayList<String> values = new ArrayList<>();
                    values.add(report.getId()); values.add("'"+dateFormatter.format(report.getReferenceDate())+"'"); values.add("'"+report.getState()+"'"); values.add(report.getContract().getId());
                    Database.insert("Reports", fields , values);

                    generatedReports++;
                }   
            }

  
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println("Generated "+generatedReports+" Reports");
    }
    
    private Contract formatContract(String id) {
        Contract contract;
        ResultSet contractSet = Database.select("Contracts", null, "WHERE id = "+id);
        
        try {
            contract = new Contract(contractSet.getString("id"), contractSet.getString("description"), contractSet.getFloat("amount"), null, null);
        } catch (SQLException ex) {
            Logger.getLogger(ReportGenerator.class.getName()).log(Level.SEVERE, null, ex);
            contract = null;
        }
        
        return contract;
    }
    
    private void listReports() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            ResultSet reportSet = Database.select("Reports", null, null);
            ArrayList<Report> reports = new ArrayList<>();
            while(reportSet.next()) {
                reports.add(new Report(reportSet.getString("id"),
                        dateFormatter.parse(reportSet.getString("reference_date")),
                         this.formatContract(reportSet.getString("contract_id")), reportSet.getString("state")));
            }
            
            System.out.println("Reports - ("+reports.size()+")");
            for(int i = 0; i < reports.size(); i++) {
                System.out.println(reports.get(i).toString());
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public boolean doAction() {
        if("save".equals(this.type)) {
            saveReports();
        } else {
            listReports();
        }
        return true;
    }
    
}

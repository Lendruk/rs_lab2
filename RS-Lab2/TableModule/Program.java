/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TableModule;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tiagocardoso
 */
public class Program {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            Contracts contractsTable = new Contracts();
            Cashflows cashflowsTable = new Cashflows();
            Reports reportsTablet = new Reports();

            //Clean the reports before running the process
            reportsTablet.deleteReportsInTable(null);

            //Find all the relevant contracts ids
            ArrayList<Integer> ids = contractsTable.getAllContractsIds();

            String startDate = "2020-03-01";
            String endDate = "2020-03-31";

            //For inserting new reports
            int idCounter = 0;

            for (Integer contractId : ids) {

                //Find the most recent cashflow date for each contract(based on a time interval)
                Date date = cashflowsTable.getCashflowsForContract(contractId, startDate, endDate);

                //Create Report for Each Contract based on the last cashflow date
                reportsTablet.calculateState(++idCounter, contractId, date);

            }

            //Show all the reports updated
            reportsTablet.printAllReports();

        } catch (SQLException | ParseException ex) {
            Logger.getLogger(Program.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

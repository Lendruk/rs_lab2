/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TransactionScript;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tiagocardoso
 */
public class Program {

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            DBConnection database = DBConnection.getInstance();
            Connection conn = database.getConnection();

            runTransactionScript(conn);
        } catch (SQLException | ParseException ex) {
            Logger.getLogger(Program.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void runTransactionScript(Connection connection) throws SQLException, ParseException {
        //Get all Contracts Available
        ArrayList<String> contractIds = new ArrayList<>();
        Statement stmt1 = connection.createStatement();
        ResultSet contracts = stmt1.executeQuery("SELECT id FROM Contracts");

        //Clean Reports before running
        Statement stmt5 = connection.createStatement();
        stmt5.executeUpdate("DELETE FROM Reports");

        while (contracts.next()) {
            String contractID = contracts.getString("id");
            contractIds.add(contractID);
        }

        int idCounter = 0;

        //Get all the Cashflows for the available Contracts
        for (int i = 0; i < contractIds.size(); i++) {
            String startDate = "2020-03-01";
            String endDate = "2020-03-31";

            //Filter all Cashflows from last month and make sure we get the most recent one
            String query = "SELECT * FROM Cashflows WHERE reference_date BETWEEN '" + startDate
                    + "' AND '" + endDate + "' AND contract_id = " + contractIds.get(i)
                    + " ORDER BY date(reference_date) DESC Limit 1";

            Statement stmt2 = connection.createStatement();
            ResultSet cashflows = stmt2.executeQuery(query);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            while (cashflows.next()) {
                //Convert the SQL Date to Java Date for Comparisons
                String cashflowDate = cashflows.getString("reference_date");
                java.util.Date paymentDate = formatter.parse(cashflowDate);

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(paymentDate);

                //Handle State Validation
                int lastPaymentDay = calendar.get(Calendar.DAY_OF_MONTH);
                String contractState = "";

                if (lastPaymentDay == 1) {
                    contractState = "NORMAL";
                } else if (lastPaymentDay >= 2 && lastPaymentDay < 31) {
                    contractState = "COMPLIANCE";
                } else {
                    contractState = "DEFAULT";
                }

                //Create a Record for each Contract based on the State
                Date now = new Date(System.currentTimeMillis());
                Statement stmt3 = connection.createStatement();

                String insertQuery = "INSERT INTO "
                        + "Reports(id, reference_date, state, contract_id) VALUES "
                        + "(" + ++idCounter + ",'"
                        + formatter.format(now) + "','"
                        + contractState + "','"
                        + contractIds.get(i) + "')";
                stmt3.executeUpdate(insertQuery);
            }
        }

        //Check Results
        Statement stmt4 = connection.createStatement();
        ResultSet reports = stmt4.executeQuery("SELECT * FROM Reports");
        while (reports.next()) {
            String id = reports.getString("id");
            String date = reports.getString("reference_date");
            String state = reports.getString("state");
            String contractID = reports.getString("contract_id");

            System.out.println("REPORT: => "
                    + "ID: " + id + " | Updated on: " + date + " | State: " + state + " | Contract ID: " + contractID);
        }

        connection.close();
    }
}

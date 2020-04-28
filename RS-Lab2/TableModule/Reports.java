/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TableModule;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author tiagocardoso
 */
public class Reports {

    private Connection connection = null;

    /**
     * Setups the connection to the database for this module
     *
     * @throws SQLException
     * @throws ParseException
     */
    public Reports() throws SQLException, ParseException {
        DBConnection database = DBConnection.getInstance();
        connection = database.getConnection();
    }

    /**
     * Convenient method that is responsible for executing queries
     *
     * @param query The raw string of the query
     * @param isUpdate A boolean used for executing queries that return a result
     * set versus queries that don't return anything like a delete
     * @return The result set associated with the request query
     * @throws SQLException
     */
    private ResultSet queryDatabase(String query, boolean isUpdate) throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet set = null;
        if (isUpdate) {
            stmt.executeUpdate(query);
        } else {
            set = stmt.executeQuery(query);
        }
        return set;
    }

    /**
     * Inserts a new report into the table
     *
     * @param referenceDate The date of the report
     * @param state The state of the report
     * @param contractId The id of the contract associated with the report
     * @throws SQLException
     */
    public void addReportToTable(Date referenceDate, String state, int contractId) throws SQLException {
        int id = getAllReportsIds().size() + 1;
        queryDatabase("INSERT INTO "
                + "Reports(id, reference_date, state, contract_id) VALUES "
                + "(" + id + ",'"
                + referenceDate + "','"
                + state + "','"
                + contractId + "')", true);
    }

    /**
     * Method for quickly deleting either a specific report or all depending on
     * the argument
     *
     * @param id
     * @throws SQLException
     */
    public void deleteReportsInTable(Integer id) throws SQLException {
        if (id == null) {
            queryDatabase("DELETE FROM Reports", true);
        } else {
            queryDatabase("DELETE FROM Reports WHERE id = " + id, true);
        }
    }

    /**
     * The method that calculates the state of the contract based on the last
     * payment date that is obtained from the cash flows
     *
     * @param id The id of the new report that is going to be inserted
     * @param contractId The id of the contract that is associated with the
     * report
     * @param cashflowDate The payment date from the most recent cash flow
     * @throws ParseException
     * @throws SQLException
     */
    public void calculateState(int id, int contractId, Date cashflowDate) throws ParseException, SQLException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(cashflowDate);

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

        Date now = new Date(System.currentTimeMillis());

        String insertQuery = "INSERT INTO "
                + "Reports(id, reference_date, state, contract_id) VALUES "
                + "(" + id + ",'"
                + formatter.format(now) + "','"
                + contractState + "','"
                + contractId + "')";

        queryDatabase(insertQuery, true);
    }

    /**
     * Convenient method that returns all the id's of the reports
     *
     * @return @throws SQLException
     */
    public ArrayList<Integer> getAllReportsIds() throws SQLException {
        ArrayList<Integer> ids = new ArrayList<>();
        ResultSet reportsSet = queryDatabase("SELECT id FROM Reports", false);

        while (reportsSet.next()) {
            int reportId = reportsSet.getInt("id");
            ids.add(reportId);
        }

        return ids;
    }

    /**
     * Convenient method that returns all the dates of the reports
     *
     * @return @throws SQLException
     * @throws ParseException
     */
    public ArrayList<Date> getAllReportsDates() throws SQLException, ParseException {
        ArrayList<Date> dates = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ResultSet reportsSet = queryDatabase("SELECT reference_date FROM Reports", false);

        while (reportsSet.next()) {
            String referenceDateString = reportsSet.getString("reference_date");
            Date referenceDate = formatter.parse(referenceDateString);
            dates.add(referenceDate);
        }

        return dates;
    }

    /**
     * Convenient method that returns all the states of the reports
     *
     * @return @throws SQLException
     */
    public ArrayList<String> getAllReportsStates() throws SQLException {
        ArrayList<String> states = new ArrayList<>();
        ResultSet reportsSet = queryDatabase("SELECT state FROM Reports", false);

        while (reportsSet.next()) {
            String state = reportsSet.getString("state");
            states.add(state);
        }

        return states;
    }

    /**
     * Convenient method that returns all the contract id's of the reports
     *
     * @return @throws SQLException
     */
    public ArrayList<String> getAllReportsContractIds() throws SQLException {
        ArrayList<String> contractIds = new ArrayList<>();
        ResultSet reportsSet = queryDatabase("SELECT contract_id FROM Reports", false);

        while (reportsSet.next()) {
            String contractId = reportsSet.getString("contract_id");
            contractIds.add(contractId);
        }

        return contractIds;
    }

    /**
     * Method that prints all the reports in the console
     *
     * @throws SQLException
     * @throws ParseException
     */
    public void printAllReports() throws SQLException, ParseException {
        ResultSet reportsSet = queryDatabase("SELECT * FROM Reports", false);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        while (reportsSet.next()) {
            int reportId = reportsSet.getInt("id");
            String referenceDateString = reportsSet.getString("reference_date");
            Date referenceDate = formatter.parse(referenceDateString);
            int cashflowContractId = reportsSet.getInt("contract_id");
            String state = reportsSet.getString("state");

            System.out.println("Report => Id: " + reportId
                    + " | Reference Date: " + formatter.format(referenceDate)
                    + " | State: " + state
                    + " | Contract Id: " + cashflowContractId);
        }
    }
}

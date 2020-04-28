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
import java.util.Date;

/**
 *
 * @author tiagocardoso
 */
public class Cashflows {

    private Connection connection = null;

    /**
     * Setups the connection to the database for this module
     *
     * @throws SQLException
     * @throws ParseException
     *
     */
    public Cashflows() throws SQLException, ParseException {
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
     * This method simply prints on the console all the information of the cash
     * flow that has the requested id
     *
     * @param id The id of the cash flow that is going to be printed
     * @throws SQLException
     * @throws ParseException
     */
    public void printCashflowById(int id) throws SQLException, ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ResultSet cashflowsSet = queryDatabase("SELECT * FROM Cashflows", false);

        while (cashflowsSet.next()) {
            int cashflowId = cashflowsSet.getInt("id");
            if (cashflowId == id) {
                String referenceDateString = cashflowsSet.getString("reference_date");
                Date referenceDate = formatter.parse(referenceDateString);
                int cashflowContractId = cashflowsSet.getInt("contract_id");

                System.out.println("Cashflow => Id: " + cashflowId
                        + " | Reference Date: " + formatter.format(referenceDate)
                        + " | Contract Id: " + cashflowContractId);
            }
        }
    }

    /**
     * Method for quickly adding a new cash flow entry in the table
     *
     * @param referenceDate The date of the cash flow
     * @param contractId The id of the contract that the cash flow
     * @throws SQLException
     */
    public void addCashflowToTable(Date referenceDate, int contractId) throws SQLException {
        int id = getAllCashflowsIds().size() + 1;
        queryDatabase("INSERT INTO "
                + "Cashflows(id, reference_date, contract_id) VALUES "
                + "(" + id + ",'"
                + referenceDate + "','"
                + contractId + "')", true);
    }

    /**
     * Method for quickly deleting either a specific cash flow or all depending
     * on the argument
     *
     * @param id
     * @throws SQLException
     */
    public void deleteCashflowInTable(Integer id) throws SQLException {
        if (id == null) {
            queryDatabase("DELETE FROM Cashflows", true);
        } else {
            queryDatabase("DELETE FROM Cashflows WHERE id = " + id, true);
        }
    }

    /**
     * Method that returns a list containing all the id's for the available cash
     * flows
     *
     * @return The list of id's for all the contracts
     * @throws SQLException
     */
    public ArrayList<Integer> getAllCashflowsIds() throws SQLException {
        ArrayList<Integer> ids = new ArrayList<>();
        ResultSet cashflowsSet = queryDatabase("SELECT id FROM Cashflows", false);

        while (cashflowsSet.next()) {
            int contractId = cashflowsSet.getInt("id");
            ids.add(contractId);
        }

        return ids;
    }

    /**
     * Method that returns a list of all the dates for the available cash flows
     *
     * @return
     * @throws SQLException
     * @throws ParseException
     */
    public ArrayList<Date> getAllCashflowsDates() throws SQLException, ParseException {
        ArrayList<Date> dates = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ResultSet cashflowsSet = queryDatabase("SELECT reference_date FROM Cashflows", false);

        while (cashflowsSet.next()) {
            String referenceDateString = cashflowsSet.getString("reference_date");
            Date referenceDate = formatter.parse(referenceDateString);
            dates.add(referenceDate);
        }

        return dates;
    }

    /**
     * Method that returns a list of all the contract id's for the available
     * cash flows
     *
     * @return
     * @throws SQLException
     */
    public ArrayList<String> getAllCashflowsContractIds() throws SQLException {
        ArrayList<String> contractIds = new ArrayList<>();
        ResultSet cashflowsSet = queryDatabase("SELECT contract_id FROM Cashflows", false);

        while (cashflowsSet.next()) {
            String contractId = cashflowsSet.getString("contract_id");
            contractIds.add(contractId);
        }

        return contractIds;
    }

    /**
     * Method that returns the cash flow with most recent date for a certain
     * contract
     *
     * @param contractId The id of the contract
     * @param dateStart The date interval start
     * @param dateEnd The date interval end
     * @return
     * @throws SQLException
     * @throws ParseException
     */
    public Date getCashflowsForContract(int contractId, String dateStart, String dateEnd) throws SQLException, ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        String query = "SELECT reference_date FROM Cashflows WHERE reference_date BETWEEN '" + dateStart
                + "' AND '" + dateEnd + "' AND contract_id = " + contractId
                + " ORDER BY date(reference_date) DESC Limit 1";

        ResultSet cashflowsSet = queryDatabase(query, false);

        String dateString = cashflowsSet.getString("reference_date");
        Date paymentDate = formatter.parse(dateString);

        return paymentDate;
    }

}

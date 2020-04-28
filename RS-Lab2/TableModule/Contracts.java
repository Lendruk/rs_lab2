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
public class Contracts {

    private Connection connection = null;

    /**
     *
     * @throws SQLException
     * @throws ParseException
     */
    public Contracts() throws SQLException, ParseException {
        DBConnection database = DBConnection.getInstance();
        connection = database.getConnection();
    }

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
     *
     * @param id
     * @throws SQLException
     * @throws ParseException
     */
    public void printContractById(int id) throws SQLException, ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ResultSet contractsSet = queryDatabase("SELECT * FROM Contracts", false);

        while (contractsSet.next()) {
            int contractId = contractsSet.getInt("id");
            if (contractId == id) {
                String description = contractsSet.getString("description");
                double amount = contractsSet.getDouble("amount");
                String startDateString = contractsSet.getString("start_date");
                Date startDate = formatter.parse(startDateString);
                String endDateString = contractsSet.getString("end_date");
                Date endDate = formatter.parse(endDateString);

                System.out.println("Contract => Id: " + contractId
                        + " | Amount: " + amount
                        + " | Description: " + description
                        + " | Start Date: " + formatter.format(startDate)
                        + " | End Date: " + formatter.format(endDate));
            }
        }
    }

    /**
     *
     * @param description
     * @param amount
     * @param startDate
     * @param endDate
     * @throws SQLException
     */
    public void addContractToTable(String description, double amount, Date startDate, Date endDate) throws SQLException {
        int id = getAllContractsIds().size() + 1;
        queryDatabase("INSERT INTO "
                + "Contracts(id, description, amount, start_date, end_date) VALUES "
                + "(" + id + ",'"
                + description + "','"
                + amount + "','"
                + startDate + "','"
                + endDate + "')", true);
    }

    /**
     *
     * @param id
     * @throws SQLException
     */
    public void deleteContractInTable(Integer id) throws SQLException {
        if (id == null) {
            queryDatabase("DELETE FROM Contracts", true);
        } else {
            queryDatabase("DELETE FROM Contracts WHERE id = " + id, true);
        }
    }

    /**
     *
     * @return @throws SQLException
     */
    public ArrayList<Integer> getAllContractsIds() throws SQLException {
        ArrayList<Integer> ids = new ArrayList<>();
        ResultSet contractsSet = queryDatabase("SELECT id FROM Contracts", false);

        while (contractsSet.next()) {
            int contractId = contractsSet.getInt("id");
            ids.add(contractId);
        }

        return ids;
    }

    /**
     *
     * @return @throws SQLException
     */
    public ArrayList<String> getAllContractsDescriptions() throws SQLException {
        ArrayList<String> descriptions = new ArrayList<>();
        ResultSet contractsSet = queryDatabase("SELECT description FROM Contracts", false);

        while (contractsSet.next()) {
            String description = contractsSet.getString("description");
            descriptions.add(description);
        }

        return descriptions;
    }

    /**
     *
     * @return @throws SQLException
     */
    public ArrayList<Double> getAllContractsAmounts() throws SQLException {
        ArrayList<Double> amounts = new ArrayList<>();
        ResultSet contractsSet = queryDatabase("SELECT amount FROM Contracts", false);

        while (contractsSet.next()) {
            double amount = contractsSet.getDouble("amount");
            amounts.add(amount);
        }

        return amounts;
    }

    /**
     *
     * @return @throws SQLException
     * @throws ParseException
     */
    public ArrayList<Date> getAllContractsStartDates() throws SQLException, ParseException {
        ArrayList<Date> startDates = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ResultSet contractsSet = queryDatabase("SELECT start_date FROM Contracts", false);

        while (contractsSet.next()) {
            String startDateString = contractsSet.getString("start_date");
            Date startDate = formatter.parse(startDateString);
            startDates.add(startDate);
        }

        return startDates;
    }

    /**
     *
     * @return @throws SQLException
     * @throws ParseException
     */
    public ArrayList<Date> getAllContractsEndDates() throws SQLException, ParseException {
        ArrayList<Date> endDates = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ResultSet contractsSet = queryDatabase("SELECT end_date FROM Contracts", false);

        while (contractsSet.next()) {
            String endDateString = contractsSet.getString("start_date");
            Date endDate = formatter.parse(endDateString);
            endDates.add(endDate);
        }

        return endDates;
    }
}

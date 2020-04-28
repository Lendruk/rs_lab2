/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs_domain;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs_domain.lib.Cashflow;
import rs_domain.lib.Contract;
import rs_domain.lib.Database;
import rs_domain.lib.ReportGenerator;
import rs_domain.lib.cli.Cli;
import rs_domain.lib.cli.Menu;
import rs_domain.lib.cli.Option;
import rs_domain.lib.cli.ResultSetParser;

/**
 *
 * @author tiagogomes
 */
public class Program {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Database.connectDatabase();
        
        Database.delete("Reports", null);
        // Build CLI
        ArrayList<Menu> menus = new ArrayList<>();
        ArrayList<Option> mainMenuOptions = new ArrayList<>();
        mainMenuOptions.add(new Option("Generate Reports", new ReportGenerator("save")));
        mainMenuOptions.add(new Option("View Reports", new ReportGenerator("list")));
        Menu mainMenu = new Menu("main_menu","Main Menu", mainMenuOptions);
        menus.add(mainMenu);
        Cli cli = new Cli("Contract Reporter", menus);
        cli.initialize();
        System.out.println("Program ran");
        Database.closeConnection();
    }
    
}

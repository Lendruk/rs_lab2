/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs_domain.lib.cli;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import rs_domain.lib.exceptions.InvalidArgumentException;

/**
 *
 * @author tiagogomes
 */
public class Cli {
    private boolean active = false;
    private String headerTitle;
    
    private ArrayList<Menu> menus;
    private Menu currentMenu;

    public Cli(String title, ArrayList<Menu> menus) {
        this.headerTitle = title;
        this.menus = menus;
        
        this.currentMenu = menus.get(0);
    }
    
    public void initialize() {
        active = true;
        Scanner reader = new Scanner(System.in);
        
        while(active) {
            try {
                System.out.println(this.printHeader());    
                System.out.println(this.printOptions());


                int option = reader.nextInt();
                
                if(option == this.currentMenu.getOptions().size()) {
                    active = false;
                }
                
                if(option < 0) throw  new InvalidArgumentException("Option must be greater than 0");
                
                this.currentMenu.getOptions().get(option).executeAction();
            } catch(Exception ex) {
                System.out.println(ex.getMessage());
            }

        }
        
    }
    
    private void selectMenu(String id) {
        for(Menu menu : menus) {
            if(menu.getId().equals(id)) {
                this.currentMenu = menu;
                this.currentMenu.setCli(this);
                break;
            }
        }
    }
    
    private String printOptions() {
        String options = "";
        
        for(int i = 0; i < this.currentMenu.getOptions().size(); i+=1) {
            Option option = this.currentMenu.getOptions().get(i);
            
            options += i + " -> " + option.getName() + "\n";
        }
        options += this.currentMenu.getOptions().size() + " -> Exit";
        return options;
    }
    
    private String printHeader() {
        return " ==== " + this.currentMenu.getName() + " ====";
    }
    
    public void shutDown() {
        
    }
    
}

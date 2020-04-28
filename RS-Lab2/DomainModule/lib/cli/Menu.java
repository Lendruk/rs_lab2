/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs_domain.lib.cli;

import java.util.ArrayList;

/**
 *
 * @author tiagogomes
 */
public class Menu {
    private String menuId;
    private ArrayList<Option> options;
    private String menuName;
    
    private Cli cli;
    
    public Menu(String id,String menuName,ArrayList<Option> options) {
        this.menuId = id;
        this.options = options;
        this.menuName = menuName;
    }
    
    public String getId() {
        return this.menuId;
    }
    
    public String getName() {
        return this.menuName;
    }
    
    public void setCli(Cli cli) {
        this.cli = cli;
    }
    
    public ArrayList<Option> getOptions() {
        return this.options;
    }
}

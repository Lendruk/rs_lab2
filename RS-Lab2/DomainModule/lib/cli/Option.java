/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs_domain.lib.cli;

/**
 *
 * @author tiagogomes
 */
public class Option {
    private String name;
    private IActionable action;
    
    public Option(String name, IActionable action) {
        this.name = name;
        this.action = action;
    }
    
    public void executeAction() {
        this.action.doAction();
    }
    
    public String getName() {
        return this.name;
    }
}

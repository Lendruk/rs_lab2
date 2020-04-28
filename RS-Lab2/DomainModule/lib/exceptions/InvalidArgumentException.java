/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs_domain.lib.exceptions;

/**
 *
 * @author tiagogomes
 */
public class InvalidArgumentException extends Exception {
    
    public String message;
    
    public InvalidArgumentException(String msg) {
        this.message = msg;
    }
    
    @Override
    public String getMessage() {
        return this.message;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zippasswordbreaker;

/**
 * object that represents a password
 * @author Connor
 */
public class Password {
    private String password;
    private boolean found;
    private boolean badFile;
    
    /**
     * Defines a password object that does not have a valid password
     */
    public Password() {
        found = false;
        password = "";
        badFile = false;
    }
    
    public Password(boolean badFile) {
        this.badFile = badFile;
        password = "";
        found = false;
    }
    
    /**
     * Defines a password object that does have a valid password
     * @param password the password as a String
     */
    public Password(String password) {
        this.password = password;
        found = true;
        badFile = true;
    }
    
    /**
     * Returns whether or not the Zip file is missing or corrupt
     * @return the status, true = broke, false = OK
     */
    public boolean getFileIsBad() {
        return badFile;
    }
    
    /**
     * retrieves the password
     * @return the valid password, or an empty string if none
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * returns whether the object has a valid password or not
     * @return whether there is a valid stored password or not
     */
    public boolean foundPassword() {
        return found;
    }
    
    /**
     * sets the password stored in the object
     * @param password the password to update to
     */
    public void setPassword(String password) {
        this.password = password;
        found = true;
    }
}

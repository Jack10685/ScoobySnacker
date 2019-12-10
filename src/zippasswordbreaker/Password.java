/*
   Copyright [2019] [Connor Jack and Jevon Erickson]

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
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

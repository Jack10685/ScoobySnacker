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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

/**
 * Collection of tools for ScoobySnacker to break open a zip file
 * @author Connor Jack
 */
public class PasswordTester {
    
    private static final Character[] UPPER = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    private static final Character[] LOWER = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    private static final Character[] NUMBER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    private static final Character[] SYMBOL = {'!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '~', '`', '-', '_', '+', '=', '{', '[', '}', ']', '|', '\\', ':', ';', '"', '\'', '<', ',', '.', '>', '?', '/'};
    
    /**
     * Stores the password, when one has been found. Remains an empty string until that point.
     */
    private static Password password;
    
    /**
     * Used to return whether a password has been found or not
     * @return whether password has been found or not
     */
    
    /**
     * Used to retrieve the password, assuming one has been found (or it will return an empty string)
     * This is best used after calling {@link zippasswordbreaker.PasswordTester.foundPassword()}
     * @return a String object containing the password
     */
    public static Password getPassword() {
        return password;
    }
    
    /**
     * Used to perform brute-force attack on a passworded zip file
     * @param zipFile the zip file to find the password for
     * @param lowerBound where to begin the attack (fewest digits)
     * @param upperBound where to end the attack (most digits)
     * @param uppers add uppercase characters to potential password?
     * @param lowers add lowercase characters to potential password?
     * @param numeric add numbers to potential password?
     * @param symbols add symbols (!,?,#,@, etc.) to potential password?
     * @return status of brute-force attack
     */
    public static void bruteforceAttack(File zipFile, int lowerBound, int upperBound, boolean uppers, boolean lowers, boolean numeric, boolean symbols) {
        ZipPasswordBreaker.log("Attempting brute-force attack...");
        if (lowerBound > upperBound) {
           ZipPasswordBreaker.logError("ERROR: brute-force bounds incorrectly set");
           return;
        }
        if (!testZipIsValid(zipFile)) {
            ZipPasswordBreaker.logError("ERROR: zip file specified does not exist or is not a zip file");
            return;
        }
        ZipFile zip = new ZipFile(zipFile);
        if (!testZipHasPassword(zip)) {
            ZipPasswordBreaker.logError("ERROR: zip file specified does not have a password");
            return;
        }
        
        /*
            define possible characters
        */
        ZipPasswordBreaker.log("Building character array...");
        ArrayList<Character> chars = new ArrayList<>();
        if (uppers) {
            chars.addAll(Arrays.asList(UPPER));
        }
        if (lowers) {
            chars.addAll(Arrays.asList(LOWER));
        }
        if (numeric) {
            chars.addAll(Arrays.asList(NUMBER));
        }
        if (symbols) {
            chars.addAll(Arrays.asList(SYMBOL));
        }
        if (chars.isEmpty()) {
            ZipPasswordBreaker.logError("ERROR: No checkboxes selected for brute-force attack");
            return;
        } else {
            BruteForceAttack bfa = new BruteForceAttack(zip, lowerBound, upperBound, chars);
            Thread thread = new Thread(bfa);
            thread.start();
        }
    }
    
    /**
     * Used to perform a dictionary attack on a passworded zip file
     * @param zipFile zip file to find the password for
     * @param passwordList dictionary to use for attack
     * @return status of dictionary attack
     */
    public static Password dictionaryAttack(File zipFile, File passwordList) {
        ZipPasswordBreaker.log("Attempting dictionary attack...");
        if (!testZipIsValid(zipFile)) {
            ZipPasswordBreaker.logError("ERROR: zip file specified does not exist or is not a zip file");
            return new Password(true);
        }
        ZipFile zip = new ZipFile(zipFile);
        if (!testZipHasPassword(zip)) {
            ZipPasswordBreaker.logError("ERROR: zip file specified does not have a password");
            return new Password(true);
        }
        try {
            FileReader fr = new FileReader(passwordList);
            BufferedReader br = new BufferedReader(fr);
            String line = "";
            while ((line = br.readLine()) != null) {
                if (ButtonHandler.shouldShowPasswords()) {
                    ZipPasswordBreaker.log(line);
                }
                boolean found = testPassword(zip, line);
                if (found) {
                    ZipPasswordBreaker.setPassword(line);
                    ZipPasswordBreaker.logSuccess("PASSWORD FOUND:"+line);
                    ZipPasswordBreaker.log("File has been extracted to extracted/"+zip.getFile().getName()+"/");
                    return new Password(line);
                }
            }
            ZipPasswordBreaker.logError("PASSWORD NOT FOUND");
            return new Password();
        } catch (FileNotFoundException e) {
            ZipPasswordBreaker.logError("ERROR: password list file does not exist");
            return new Password();
        } catch (IOException e) {
            ZipPasswordBreaker.logError("ERROR: password list file is corrupted or unreadable");
            return new Password();
        }
    }
    
    /**
     * Tests to see if the file specified both exists and is a valid zip file
     * @param zip file to test
     * @return if the file is a valid zip file
     */
    private static boolean testZipIsValid(File zip) {
        if (zip == null) {
            return false;
        }
        if (!zip.exists()) {
            return false;
        } 
        ZipFile actual = new ZipFile(zip);
        if (!actual.isValidZipFile()) {
            return false;
        }
        return true;
    }
    
    /**
     * Tests to see if a provided zip file is encrypted with a password
     * @param zip zip file to test
     * @return whether the file is encrypted with a password or not
     */
    private static boolean testZipHasPassword(ZipFile zip) {
        try {
            if (!zip.isEncrypted()) {
                return false;
            }
        } catch(ZipException e) {
            return false;
        }
        return true;
    }
    
    /**
     * Called by the attacks to test if a password is correct or not
     * @param zip zip file to test
     * @param password password to test on it
     * @return whether the password is correct or not
     */
    public static boolean testPassword(ZipFile zip, String password) {
        char[] pass = password.toCharArray();
        zip.setPassword(pass);
        File file = zip.getFile();
        String name = file.getName();
        String output = "extracted/"+name+"/";
        if (!file.exists()) {
            File out = new File(output);
            out.mkdir();
        }
        try {
        zip.extractAll(output);
        } catch(ZipException e) {
            return false;
        }
        return true;
    }
}

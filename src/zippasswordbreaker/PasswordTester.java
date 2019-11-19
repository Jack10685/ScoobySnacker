/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
           //return new Password(); 
        }
        if (!testZipIsValid(zipFile)) {
            ZipPasswordBreaker.logError("ERROR: zip file specified does not exist or is not a zip file");
            //return new Password();
        }
        ZipFile zip = new ZipFile(zipFile);
        if (!testZipHasPassword(zip)) {
            ZipPasswordBreaker.logError("ERROR: zip file specified does not have a password");
            //return new Password();
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
            //return new Password();
        } else {
            BruteForceAttack bfa = new BruteForceAttack(zip, lowerBound, upperBound, chars);
            Thread thread = new Thread(bfa);
            thread.start();
        }
        
        
        
        /*
            initialize password char array
        */
        /*//ArrayList<Character> password = new ArrayList<>();
        ArrayList<Integer> position = new ArrayList<>();
        for (int i = 0; i < lowerBound; i++) {
            //password.add(chars.get(0));
            position.add(0);
            ZipPasswordBreaker.log(""+i);
        }
        
        while (position.size() <= upperBound) {
            position.set(0, position.get(0)+1);
            if (position.get(0) >= chars.size()) {
                position.set(0, 0);
                int digit = 1;
                while (digit != 0) {
                    position.set(digit, position.get(digit)+1);
                    if (position.get(digit) >= chars.size()) {
                        position.set(digit, 0);
                        digit++;
                        if (digit >= position.size()) {
                            position.add(0);
                            digit = 0;
                        }
                    }
                    else {
                        digit = 0;
                    }
                }
            }
            
            String test = "";
            for (int i = 0; i < position.size(); i++) {
                test+=chars.get(position.get(i));
            }
            System.out.println(test);
            boolean isCorrect = testPassword(zip, test);
            if (isCorrect) {
                ZipPasswordBreaker.logSuccess("PASSWORD FOUND:"+test);
                ZipPasswordBreaker.log("File has been extracted to extracted/"+zip.getFile().getName()+"/");
                //return new Password(test);
            }
        }*/
        
        //return new Password();
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
            return new Password();
        }
        ZipFile zip = new ZipFile(zipFile);
        if (!testZipHasPassword(zip)) {
            ZipPasswordBreaker.logError("ERROR: zip file specified does not have a password");
            return new Password();
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

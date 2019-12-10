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

import java.util.ArrayList;
import net.lingala.zip4j.ZipFile;

/**
 * class used to run the brute-force attack in a separate thread from the gui
 * @author Connor
 */
public class BruteForceAttack implements Runnable{
    private int upperBound, lowerBound;
    private ZipFile zip;
    private ArrayList<Character> chars;
    
     /**
      * Defined all the information needed for the brute-force attack to run correctly 
      * @param zip the zip file to be tested
      * @param lowerBound the lower length bound for the password
      * @param upperBound the higher length bound for the password
      * @param chars an ArrayList of possible characters for the password
      */
    public BruteForceAttack(ZipFile zip, int lowerBound, int upperBound, ArrayList<Character> chars) {
        this.zip = zip;
        this.upperBound = upperBound;
        this.lowerBound = lowerBound;
        this.chars = chars;
    }

    /**
     * called when a new thread is created with an object this type is passed into the constructor and the start method is called on it
     */
    @Override
    public void run() {
        boolean passFound = false;
        ZipPasswordBreaker.log("Starting brute-force...");
        ZipPasswordBreaker.log("Testing "+lowerBound+"-length passwords...");
        ArrayList<Integer> position = new ArrayList<>();
        for (int i = 0; i < lowerBound; i++) {
            position.add(0);
        }
        
        while (position.size() <= upperBound) {
            position.set(0, position.get(0)+1);
            if (position.get(0) >= chars.size()) {
                position.set(0, 0);
                int digit = 1;
                while (digit != 0) {
                    if (position.size() == 1) {
                        ZipPasswordBreaker.log("Testing 2-length passwords...");
                        position.add(0);
                        digit = 0;
                    } else {
                        position.set(digit, position.get(digit)+1);
                        if (position.get(digit) >= chars.size()) {
                            position.set(digit, 0);
                            digit++;
                            if (digit >= position.size()) {
                                position.add(0);
                                if (digit < upperBound)
                                    ZipPasswordBreaker.log("Testing "+(digit+1)+"-length passwords...");
                                digit = 0;
                            }
                        }
                        else {
                            digit = 0;
                        }
                    }
                }
            }
            
            String test = "";
            for (int i = 0; i < position.size(); i++) {
                test+=chars.get(position.get(i));
            }
            System.out.println(test);
            if (ButtonHandler.shouldShowPasswords()) {
                ZipPasswordBreaker.log(test);
            }
            boolean isCorrect = PasswordTester.testPassword(zip, test);
            if (isCorrect) {
                ZipPasswordBreaker.logSuccess("PASSWORD FOUND:"+test);
                ZipPasswordBreaker.log("File has been extracted to extracted/"+zip.getFile().getName()+"/");
                passFound = true;
                ZipPasswordBreaker.setPassword(test);
                break;
            }
        }
        if (!passFound) {
            ZipPasswordBreaker.logError("PASSWORD NOT FOUND");
        }
    }

}

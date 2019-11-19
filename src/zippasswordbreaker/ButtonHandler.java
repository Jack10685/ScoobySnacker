/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zippasswordbreaker;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Used to handle the actions of the buttons on the main GUI screen of ScoobySnacker
 * @author Connor
 */
public class ButtonHandler implements ActionListener{
    private JButton zipButton;
    private JButton dictButton;
    
    private File zipFile;
    private File dictFile;
    
    private static boolean showPasswords;
    
    public ButtonHandler(JButton zip, JButton dict) {
        zipButton = zip;
        dictButton = dict;
    }
    
    public static boolean shouldShowPasswords() {
        return showPasswords;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("openzip")) {
            JFileChooser zipChooser = new JFileChooser(System.getProperty("user.home")+"\\Desktop");
            FileNameExtensionFilter fnefZIP = new FileNameExtensionFilter("ZIP Files", "zip");
            zipChooser.setFileFilter(fnefZIP);
            
            String jic = zipButton.getText();
            
            zipChooser.showOpenDialog(zipButton);
            zipButton.setText(zipChooser.getName(zipChooser.getSelectedFile()));
            zipFile = zipChooser.getSelectedFile();
            if (zipButton.getText() == null) {
                zipButton.setText(jic);
            }
        }
        if (e.getActionCommand().equals("opendict")) {
            JFileChooser dictChooser = new JFileChooser(System.getProperty("user.home")+"\\Desktop");
            FileNameExtensionFilter fnefZIP = new FileNameExtensionFilter("Text Files", "txt");
            dictChooser.setFileFilter(fnefZIP);
            
            String jic = dictButton.getText();
            
            dictChooser.showOpenDialog(dictButton);
            dictButton.setText(dictChooser.getName(dictChooser.getSelectedFile()));
            dictFile = dictChooser.getSelectedFile();
            if (dictButton.getText() == null) {
                dictButton.setText(jic);
            }
        }
        if (e.getActionCommand().equals("gangway")) {
            if (dictFile != null) {
                Password pass = PasswordTester.dictionaryAttack(zipFile, dictFile);
                if (!pass.foundPassword()) {
                    PasswordTester.bruteforceAttack(zipFile, (int) ZipPasswordBreaker.startnum.getValue(), (int) ZipPasswordBreaker.endnum.getValue(), ZipPasswordBreaker.upperbox.isSelected(), ZipPasswordBreaker.lowerbox.isSelected(), ZipPasswordBreaker.numbox.isSelected(), ZipPasswordBreaker.symbox.isSelected());
                }
            } else {
                PasswordTester.bruteforceAttack(zipFile, (int) ZipPasswordBreaker.startnum.getValue(), (int) ZipPasswordBreaker.endnum.getValue(), ZipPasswordBreaker.upperbox.isSelected(), ZipPasswordBreaker.lowerbox.isSelected(), ZipPasswordBreaker.numbox.isSelected(), ZipPasswordBreaker.symbox.isSelected());
            }
        }
        if (e.getActionCommand().equals("spass")) {
            JCheckBox src = (JCheckBox) e.getSource();
            showPasswords = src.isSelected();
        }
    }
    
}

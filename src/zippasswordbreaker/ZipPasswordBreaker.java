package zippasswordbreaker;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Connor Jack
 */
public class ZipPasswordBreaker {
    private static ActionListener buttonHandler;
    private static PrintStream console;
    private static JLabel logger;
    private static String consoleText = "";
    private static JTextField passwordBox;
    public static JSpinner startnum;
    public static JSpinner endnum;
    public static JCheckBox upperbox;
    public static JCheckBox lowerbox;
    public static JCheckBox symbox;
    public static JCheckBox numbox;
    public static JCheckBox showPass;
    
    
    public static void main(String[] args) {
        JButton openZip = new JButton("Open Zip File");
        JButton openDict = new JButton("Open Dictionary");
        
        buttonHandler = new ButtonHandler(openZip, openDict);
        
        JFrame frame = new JFrame("ScoobySnacker");
        frame.setLayout(null);
        frame.setSize(500,630);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.getContentPane().setBackground(Color.red);
        
        JPanelOutlined p1 = new JPanelOutlined(2, Color.BLUE);
        p1.setSize(480, 50);
        p1.setLocation(2, 2);
        frame.add(p1);
        
        JLabel zipchooselabel = new JLabel("Like, choose a ZIP file, scoob:");
        zipchooselabel.setLocation(10,10);
        p1.add(zipchooselabel);
        
        openZip.setSize(100, 50);
        openZip.addActionListener(buttonHandler);
        openZip.setActionCommand("openzip");
        p1.add(openZip);
        
        JPanelOutlined p2 = new JPanelOutlined(2, Color.BLUE);
        p2.setSize(480, 50);
        p2.setLocation(2, 54);
        frame.add(p2);
        
        JLabel dictchooselabel = new JLabel("Ruh roh raggy, are we using a dictionary attack?");
        p2.add(dictchooselabel);
        
        openDict.setSize(100, 50);
        openDict.addActionListener(buttonHandler);
        openDict.setActionCommand("opendict");
        p2.add(openDict);
        
        JPanelOutlined p3 = new JPanelOutlined(2, Color.BLUE);
        p3.setSize(480, 150);
        p3.setLocation(2, 106);
        p3.setLayout(null);
        frame.add(p3);
        
        JLabel bruteforcelabel = new JLabel("Ok gang, how much should our bruteforce attack cover?");
        bruteforcelabel.setSize(p3.getSize());
        bruteforcelabel.setLocation(0,10);
        bruteforcelabel.setHorizontalAlignment(JLabel.CENTER);
        bruteforcelabel.setVerticalAlignment(JLabel.TOP);
        p3.add(bruteforcelabel);
        
        JLabel start = new JLabel("Password length lowerbound?");
        start.setSize(200, 20);
        start.setLocation(10, 45);
        p3.add(start);
        
        startnum = new JSpinner();
        startnum.setSize(50, 30);
        startnum.setLocation(190, 40);
        startnum.setValue(1);
        p3.add(startnum);
        
        
        JLabel end = new JLabel("Password length upperbound?");
        end.setSize(200, 20);
        end.setLocation(10, 95);
        p3.add(end);
        
        endnum = new JSpinner();
        endnum.setSize(50, 30);
        endnum.setLocation(190, 90);
        endnum.setValue(20);
        p3.add(endnum);
        
        JLabel upper = new JLabel("Uppercase?");
        upper.setSize(100, 20);
        upper.setLocation(250, 45);
        p3.add(upper);
        
        upperbox = new JCheckBox();
        upperbox.setLocation(330, 45);
        upperbox.setSize(20,20);
        p3.add(upperbox);
        
        JLabel lower = new JLabel("Lowercase?");
        lower.setSize(100, 20);
        lower.setLocation(250, 95);
        p3.add(lower);
        
        lowerbox = new JCheckBox();
        lowerbox.setLocation(330, 95);
        lowerbox.setSize(20,20);
        p3.add(lowerbox);
        
        JLabel numeric = new JLabel("Numeric?");
        numeric.setSize(100, 20);
        numeric.setLocation(370, 45);
        p3.add(numeric);
        
        numbox = new JCheckBox();
        numbox.setLocation(435, 45);
        numbox.setSize(20,20);
        p3.add(numbox);
        
        JLabel symbols = new JLabel("Symbols?");
        symbols.setSize(100, 20);
        symbols.setLocation(370, 95);
        p3.add(symbols);
        
        symbox = new JCheckBox();
        symbox.setLocation(435, 95);
        symbox.setSize(20,20);
        p3.add(symbox);
        
        JPanelOutlined p4 = new JPanelOutlined(2, Color.BLUE);
        p4.setSize(480, 75);
        p4.setLocation(2, 258);
        p4.setLayout(null);
        frame.add(p4);
        
        JButton begin = new JButton("Gangway!");
        begin.setSize(200,55);
        begin.setLocation(140, 10);
        begin.addActionListener(buttonHandler);
        begin.setActionCommand("gangway");
        p4.add(begin);
        
        JPanelOutlined p5 = new JPanelOutlined(2, Color.BLUE);
        p5.setSize(480, 254);
        p5.setLocation(2, 335);
        p5.setLayout(null);
        frame.add(p5);
        
        JScrollPane spane = new JScrollPane();
        spane.setSize(470, 214);
        spane.setLocation(5,5);
        spane.getViewport().setBackground(Color.BLACK);
        p5.add(spane);
        
        logger = new JLabel();
        logger.setMinimumSize(new Dimension(470, 214));
        logger.setMaximumSize(new Dimension(470, 10000));
        logger.setLocation(0,0);
        logger.setBackground(Color.BLACK);
        logger.setVerticalAlignment(JLabel.TOP);
        spane.getViewport().add(logger);
        
        log("Welcome to ScoobySnacker<br>Created by Connor Jack");
        
        showPass = new JCheckBox();
        showPass.setSize(20,20);
        showPass.setLocation(166, 224);
        showPass.addActionListener(buttonHandler);
        showPass.setActionCommand("spass");
        p5.add(showPass);
        
        JLabel sps = new JLabel("Let's get a closer look, gang");
        sps.setSize(200,20);
        sps.setLocation(5, 224);
        p5.add(sps);
        
        JLabel psw = new JLabel("Jinkies! it's the password:");
        psw.setSize(200,20);
        psw.setLocation(200, 224);
        p5.add(psw);
        
        passwordBox = new JTextField();
        passwordBox.setSize(100,20);
        passwordBox.setLocation(355, 224);
        passwordBox.setEditable(false);
        p5.add(passwordBox);
        
        
        
        frame.setVisible(true);
    }
    
    /**
     * Used to log updates to the in-GUI console
     * @param text the update message
     */
    public static void log(String text) {
       consoleText = "<span style=\"color:white;\">"+text+"</span><br>"+consoleText;
       logger.setText("<html>"+consoleText+"</html>");
    }
    
    /**
     * Used to log errors to the in-GUI console
     * @param text the error message
     */
    public static void logError(String text) {
       consoleText = "<span style=\"color:red;\">"+text+"</span><br>"+consoleText;
       logger.setText("<html>"+consoleText+"</html>");
    }
    
    /**
     * Used to add positive updates to the in-GUI console
     * @param text the success message
     */
    public static void logSuccess(String text) {
       consoleText = "<span style=\"color:green;\">"+text+"</span><br>"+consoleText;
       logger.setText("<html>"+consoleText+"</html>");
    }
    
    /**
     * Used to set the password in the GUI's JTextField
     * @param text the password found
     */
    public static void setPassword(String text) {
        passwordBox.setText(text);
    }
}

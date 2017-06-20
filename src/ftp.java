
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.FileOutputStream;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.text.*;


import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPCmd;
import com.ibm.as400.access.AS400FTP;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.AS400SecurityException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import java.util.Date;
//import AS400CommandCallTest;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ftp extends JFrame implements ActionListener {
    JLabel lblName;
    JLabel lblCommand;
    JLabel lblHost;
    JLabel lblUser;
    JLabel lblPass;
    JLabel lblPort;
    JTextField txtName;
    JTextField txtCommand;
    JTextField txtHost;
    JTextField txtUser;
    JTextField txtPass;
    JTextField txtPort;
    JButton btnProcess;
    JButton btnConnect;
    JTextArea txtS;
    String newline = "\n";
    FTPClient ftpClient;
    String commandStr;
    CommandCall command;
    AS400FTP   ftp;
    AS400 system;

    public void actionPerformed(ActionEvent e) {

        if (e.getSource().equals(btnConnect)) {
            try {
                connectFTP();
            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        if (e.getSource().equals(btnProcess)) {
            try {
                processInformation();
            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public void connectFTP() throws UnknownHostException, IOException {

        AS400 system = new AS400();
        AS400FTP   ftp    = new AS400FTP(system);



       // ftp.issueCommand("RCMD CALL CGBINF/RCMD2JSHL PARM('CRTLIB HACK400Z6')");


        //ftp.issueCommand("help");

        //String ftpoutput ="";

        ftp.connect();
        System.out.println(ftp.getLastMessage());
        txtS.setText(ftp.getLastMessage());





    }

    public void processInformation() throws UnknownHostException, IOException {

/*
        String server = txtHost.getText();
        int port = Integer.parseInt(txtPort.getText());
        String user = txtUser.getText();
        String pass = txtPass.getText();

*/
        AS400 system = new AS400();
        AS400FTP   ftp    = new AS400FTP(system);

        //AS400FTP ftp2 = ftp;
        System.out.println("Process Information Clicked");
        System.out.println(ftp.getLastMessage());

        //write command to ftpinput.txt
        writeToFile(txtCommand.getText());

        //upload command box to ascii
        ftp.put("ftpinput.txt", "/home/hack400b/ftpinput.txt");

        //execute rcmd2jshll
        ftp.issueCommand("RCMD CALL HACK400B/RCMD2JSHLI");

        //download ascii output file
        ftp.get("/home/hack400b/ftpoutput.txt", "ftpoutput.txt");

        BufferedReader buff = null;
        txtS.setText("");
        try {
            buff = new BufferedReader(new FileReader("ftpoutput.txt"));
            String str;
            while ((str = buff.readLine()) != null) {
                txtS.append("\n"+str);
            }
        } catch (IOException e) {
        } finally {
            try { buff.close(); } catch (Exception ex) { }
        }



    }



    public void showServerReply(FTPClient ftpClient) {
        String[] replies = ftpClient.getReplyStrings();
        if (replies != null && replies.length > 0) {
            for (String aReply : replies) {
              //  System.out.println("SERVER: " + aReply);
                txtS.append("SERVER: " + aReply + newline);
            }
        }
    }

    public void AS400CommandCallTest(){
        String server="blfscdv1";
        String user = "dstec";
        String pass = "asdf1@345";

        String commandStr = "crtlib hack400c";

        AS400 as400 = null;
        try  {
            // Create an AS400 object
            as400 = new AS400(server, user, pass);

            // Create a Command object
            CommandCall command = new CommandCall(as400);

            // Run the command.
            System.out.println("Executing: " + commandStr);
            boolean success = command.run(commandStr);

            if (success) {
                System.out.println("Command Executed Successfully.");
            }else{
                System.out.println("Command Failed!");
            }

            // Get the command results
            AS400Message[] messageList = command.getMessageList();
            for (AS400Message message : messageList){
                System.out.println(message.getText());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try{
                // Make sure to disconnect
                as400.disconnectAllServices();
            }catch(Exception e){}
        }

    }
    private void writeToFile(String list) throws IOException{
///
        File f = new File("ftpinput.txt");
        System.out.println(f);
        FileWriter fw = new FileWriter(f,false);
        System.out.println(fw);
        try{
            BufferedWriter bw = new BufferedWriter(fw);
            System.out.println(bw);
           // bw.newLine();
            bw.write(list);
            bw.flush();
            bw.close();
        }
        catch(Exception e){
            System.out.println(e);
        }
        ///
    }
    public ftp() {


        this.setTitle("AS400 IBM FTP Command Interpreter");
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);



        //add labels and text boxes
        lblHost = new JLabel("Host: ");
        lblHost.setBounds(10, 10, 90, 21);
        add(lblHost);

        txtHost = new JTextField();
        txtHost.setBounds(105, 10, 90, 21);
        add(txtHost);

        txtPort = new JTextField();
        txtPort.setBounds(105, 35, 90, 21);
        add(txtPort);

        lblPort = new JLabel("Port: ");
        lblPort.setBounds(10, 35, 90, 21);
        add(lblPort);

        lblUser = new JLabel("Username: ");
        lblUser.setBounds(10, 60, 90, 21);
        add(lblUser);

        txtUser = new JTextField();
        txtUser.setBounds(105, 60, 90, 21);
        add(txtUser);

        lblPass = new JLabel("Password: ");
        lblPass.setBounds(10, 85, 90, 21);
        add(lblPass);

        txtPass = new JTextField();
        txtPass.setBounds(105, 85, 90, 21);
        add(txtPass);

        lblCommand = new JLabel("Command: ");
        lblCommand.setBounds(10, 135, 90, 21);
        add(lblCommand);

        txtCommand = new JTextField();
        txtCommand.setBounds(105, 135, 550, 40);
        add(txtCommand);


/*
        lblName = new JLabel("Name: ");
        lblName.setBounds(10, 10, 90, 21);
        add(lblName);

        txtName = new JTextField();
        txtName.setBounds(105, 10, 90, 21);
        add(txtName);
*/


        //lblMark = new JLabel("Mark: ");
       // lblMark.setBounds(10, 60, 90, 21);
       // add(lblMark);

      //  txtMark = new JTextField();
      //  txtMark.setBounds(105, 60, 90, 21);
      //  add(txtMark);

        btnConnect = new JButton("Initiate FTP");
        btnConnect.setBounds(200, 40, 160, 40);
        btnConnect.addActionListener(this);
        add(btnConnect);


        btnProcess = new JButton("Process Command");
        btnProcess.setBounds(10, 180, 160, 40);
        btnProcess.addActionListener(this);
        add(btnProcess);




        txtS = new JTextArea();
        txtS.setBounds(10, 245, 750, 520);
        add(txtS);

        //set testing values
        txtHost.setText("192.168.69.69");
        txtPort.setText("21");
        txtUser.setText("thehump");
        txtPass.setText("asdasd");




        this.setVisible(true);

    }

    public static void main(String[] args) {


        new ftp();
    }




}
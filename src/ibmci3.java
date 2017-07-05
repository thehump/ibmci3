/*  ibmci3.java client program
David Stec, david.m.stec@gmail.com
Version 1.0, June 2017
 */


import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400FTP;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.UnknownHostException;

public class ibmci3 extends JFrame implements ActionListener {
    JLabel lblCommand;
    JLabel lblPort;
    JTextField txtCommand;
    JTextField txtPort;
    JButton btnProcess;
    JButton btnProcessJshl;
    JButton btnConnect;
    JButton btnExit;
    JTextArea txtS;
    JScrollPane scrollS;
    String newline = "\n";

    public void actionPerformed(ActionEvent e) {
        AS400 system = new AS400();
        AS400FTP   ftp    = new AS400FTP(system);
        //Initiate FTP Connection
        if (e.getSource().equals(btnConnect)) {
              try {
                connectFTP(ftp);
            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        //Process Display Command
        if (e.getSource().equals(btnProcess)) {


            try {
                processInformation(ftp);
            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        //Process Jshell Command
        if (e.getSource().equals(btnProcessJshl)) {


            try {
                processInformationJshl(ftp);
            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        //Delete and Disconnect
        if (e.getSource().equals(btnExit)) {
            try {
                exitFTP(ftp);
            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    ////Initiate FTP Connection
    public void connectFTP(AS400FTP ftp) throws UnknownHostException, IOException {
        //upload and setup required server side programs
        ftp.connect();
        txtS.setText(ftp.getLastMessage());
        ftp.issueCommand("RCMD CRTLIB HACK400B");
        txtS.append(newline + ftp.getLastMessage());
        ftp.issueCommand("RCMD CRTSAVF HACK400B/hack400B");
        txtS.append(newline + ftp.getLastMessage());
        ftp.setDataTransferType(1);
        txtS.append(newline + ftp.getLastMessage());
        ftp.put("hack400b.savf", "/qsys.lib/hack400b.lib/hack400b.file");
        txtS.append(newline + ftp.getLastMessage());
        ftp.issueCommand("RCMD RSTOBJ OBJ(*ALL) SAVLIB(HACK400Z) DEV(*SAVF) SAVF(HACK400B/HACK400B) MBROPT(*ALL) ALWOBJDIF(*ALL) RSTLIB(HACK400B) ");
        ftp.issueCommand("RCMD CALL HACK400B/rcmdsetup");
        txtS.append(newline + ftp.getLastMessage());
        ftp.put("jshell3.class", "/home/hack400b/jshell3.class");
        txtS.append(newline + ftp.getLastMessage());
        ftp.put("./lib/jt400.jar", "/home/hack400b/lib/jt400.jar");
        txtS.append(newline + ftp.getLastMessage());
        ftp.setDataTransferType(0);
        txtS.append(newline + ftp.getLastMessage());
        ftp.put("ibmci3.iml", "/home/hack400b/ibmci3.iml");
        txtS.append(newline + "FTP Connected succesfully to : " + ftp.getServer() + " on port: " + ftp.getPort());

    }
    //Delete and Disconnect
    public void exitFTP(AS400FTP ftp) throws UnknownHostException, IOException {
        ftp.connect();
        System.out.println(ftp.getLastMessage());
        ftp.issueCommand("RCMD CALL HACK400B/RCMDEXIT");
        txtS.setText(ftp.getLastMessage() );
        ftp.issueCommand("RCMD DLTLIB HACK400B");
        txtS.append(newline + ftp.getLastMessage());
        ftp.disconnect();
        txtS.append(newline + ftp.getLastMessage() + newline + "  FTP Disconnected");
    }
    //Process Display Command
    public void processInformation(AS400FTP ftp) throws UnknownHostException, IOException {
        System.out.println("Proess Information Clicked");
        writeToFile(txtCommand.getText());
        txtS.append(newline);
        System.out.println(ftp.getLastMessage());
        //upload command box to ascii
        ftp.put("ftpinput.txt", "/home/hack400b/ftpinput.txt");
        //execute RCMD2QSHL CLP
        ftp.issueCommand("RCMD CALL HACK400B/RCMD2QSHL");
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
    //Process Jshell Command
    public void processInformationJshl(AS400FTP ftp) throws UnknownHostException, IOException {
        System.out.println("Process Information Clicked");
        writeToFile(txtCommand.getText());
        System.out.println(ftp.getLastMessage());

        //upload command box to ascii
        ftp.put("ftpinput.txt", "/home/hack400b/ftpinput.txt");

        //execute RCMD2JSHLI
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
        txtS.append(newline);
    }

    //method to write desired command to ftpinput.txt on AS/400 for later processing
    private void writeToFile(String list) throws IOException{
        File f = new File("ftpinput.txt");
        System.out.println(f);
        FileWriter fw = new FileWriter(f,false);
        System.out.println(fw);
        try{
            BufferedWriter bw = new BufferedWriter(fw);
            System.out.println(bw);
            bw.write(list);
            bw.flush();
            bw.close();
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    //set up Java GUI window
    public ibmci3() {

        this.setTitle("AS400 IBM FTP Command Interpreter");
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        //add labels and text boxes
        txtPort = new JTextField();
        txtPort.setBounds(105, 10, 90, 21);
        txtPort.setText("21");
        add(txtPort);

        lblPort = new JLabel("Port: ");
        lblPort.setBounds(10, 10, 90, 21);
        add(lblPort);

        lblCommand = new JLabel("Command: ");
        lblCommand.setBounds(10, 135, 90, 21);
        add(lblCommand);

        txtCommand = new JTextField();
        txtCommand.setBounds(105, 135, 550, 40);
        add(txtCommand);

        btnConnect = new JButton("Initiate FTP Connection");
        btnConnect.setForeground(Color.GREEN);
        btnConnect.setBackground(Color.black);
        btnConnect.setBounds(10, 40, 200, 40);
        btnConnect.addActionListener(this);
        add(btnConnect);

        btnProcess = new JButton("Process Display Command");
        btnProcess.setBounds(10, 185, 250, 40);
        btnProcess.addActionListener(this);
        add(btnProcess);

        btnProcessJshl = new JButton("<HTML><i>Process Jshell Command</i></HTML>");
        btnProcessJshl.setBounds(400, 185, 250, 40);
        btnProcessJshl.addActionListener(this);
        add(btnProcessJshl);

        btnExit = new JButton("Delete and Disconnect");
        btnExit.setForeground(Color.RED);
        btnExit.setBounds(10, 85, 200, 40);
        btnExit.addActionListener(this);
        add(btnExit);

        txtS = new JTextArea();
        scrollS = new JScrollPane(txtS);
        scrollS.setBounds(10, 245, 765, 300);
        add(scrollS);

        this.setVisible(true);
    }
    //main
    public static void main(String[] args) {
        new ibmci3();
    }
}
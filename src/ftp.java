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

import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPCmd;
import com.ibm.as400.access.AS400FTP;
import com.ibm.as400.access.AS400;
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

    public void processInformation() throws UnknownHostException, IOException {

        AS400 system = new AS400();

        AS400FTP   ftp    = new AS400FTP(system);
        ftp.issueCommand("RCMD CRTLIB HACK400B");
        //ftp.issueCommand("help");
        System.out.println(ftp.getLastMessage());

        ftp.issueCommand("RCMD DSPJOBLOG OUTPUT(*OUTFILE) OUTFILE(HACK400B/FTPOUTPUT)");
        //ftp.issueCommand("help");
        System.out.println(ftp.getLastMessage());

        ftp.issueCommand("RCMD RUNSQL SQL('create view v4 as SELECT QMHMDT from HACK400b/FTPOUTPUT') ");
        //ftp.issueCommand("help");
        System.out.println(ftp.getLastMessage());

        ftp.issueCommand("RCMD CPYTOIMPF FROMFILE(HACK400/FTPOUTPUT or HACK400/v3) TOSTMF(‘/home/hack400b/ftpoutput.txt)’ RCDDLM(*CRLF) ");
        //ftp.issueCommand("help");
        System.out.println(ftp.getLastMessage());



       // Socket s = new Socket("192.168.69.69", 21);
   //   ObjectOutputStream p = new ObjectOutputStream(s.getOutputStream());

      //  String name = txtName.getText();
     //   String command = txtCommand.getText();
    //    String rcmdtext= "crtlib hack400b";
    //    String server = txtHost.getText();
    //    int port = Integer.parseInt(txtPort.getText());
    //    String user = txtUser.getText();
     //   String pass = txtPass.getText();
     //   FTPClient ftpClient = new FTPClient();

     //   ftpClient.sendCommand(FTPCmd.CWD, "/home/thehump");

                //int age = Integer.parseInt(txtAge.getText());

        //p.writeObject(test');

        //FileOutputStream fileOutputStream = new FileOutputStream("C:\\wut\\ibmci3\\src\\ftp.txt");
        //ObjectOutputStream output = new ObjectOutputStream(fileOutputStream);
        //String ftptxt = "pwd";
       // p.writeObject(ftptxt);
        //p.close();

       // p.flush();

        // Here we read the details from server
      //  BufferedReader response = new BufferedReader(new InputStreamReader(
      //          s.getInputStream()));
      //  txtS.setText("The server respond: " + response.readLine());
      //  p.close();
     //   response.close();
     //   s.close();
    }

    public void connectFTP() throws UnknownHostException, IOException {



        String server = txtHost.getText();
        int port = Integer.parseInt(txtPort.getText());
        String user = txtUser.getText();
        String pass = txtPass.getText();
        FTPClient ftpClient = new FTPClient();


        try {

            ftpClient.connect(server, port);
            showServerReply(ftpClient);

            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                txtS.append("Operation failed. Server reply code: " + replyCode + newline);

                return;
            }

            boolean success = ftpClient.login(user, pass);
            showServerReply(ftpClient);

            if (!success) {
            //    txtS.appendText("Could not login to the server" + newline);
            //    System.out.println("Could not login to the server");
                return;
            } else {
             //   System.out.println("LOGGED IN SERVER");
                txtS.append("LOGGED IN SERVER" + newline);
            }

        } catch (IOException ex) {
            txtS.append("Oops! Something wrong happened"+ newline);
            ex.printStackTrace();
        }
        //ftpClient.sendCommand(FTPCmd.CWD, "/home");
        //ftpClient.sendCommand(FTPCmd.PRINT_WORKING_DIRECTORY);

        ftpClient.sendCommand("pwd");

       // ftpClient.sendCommand(txtCommand.getText());
        //ftpClient.sendCommand(FTPCmd.MKD, "pwd");

        System.out.println(ftpClient.getReplyString());

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
        txtS.setBounds(10, 245, 750, 120);
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
/**
 * Created by thehump on 6/18/2017.
 */

import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.AS400;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class jshell2 extends Object {


    public static void main() throws IOException {
         System.out.println("jshell2 started");
        String commandString = "crtlib hacktest8";
        AS400 as400 = new AS400();
        CommandCall command = new CommandCall(as400);

        File f = new File("ftpinput.txt");

        try{
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            while(br.ready()){
                commandString = br.readLine();
                System.out.println(br.readLine());

            }
        }catch(Exception e){
            System.out.println(e);
        }




        try{
            if (command.run(commandString))
                System.out.println("jshell2 successful");
            else
                System.out.println("jshell2 failed");

            AS400Message[] messagelist = command.getMessageList();

            for (int i=0; i < messagelist.length; i++){
                //System.out.print(messagelist[i].getID());

                System.out.print(messagelist[i].getText());

            }

        }
        catch (Exception e)
        {
            System.out.println("mjor failre");
        }
    }

}

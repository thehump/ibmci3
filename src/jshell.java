/**
 * Created by thehump on 6/18/2017.
 */

import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.AS400;

public class jshell extends Object {

    public static void main(String[] args){
        String commandString = " ";

        for (int i = 0; i < args.length ; i++){
            commandString += args[i]+ " ";


        }

        AS400 as400 = new AS400();

        CommandCall command = new CommandCall(as400);


        try{
            if (command.run(commandString))
                System.out.println("jshell successful");
            else
                System.out.println("jshell failed");

            AS400Message[] messagelist = command.getMessageList();

            for (int i=0; i < messagelist.length; i++){
                System.out.print(messagelist[i].getID());
                
                System.out.print(messagelist[i].getText());

            }

        }
        catch (Exception e)
        {
            System.out.println("mjor failre");
        }
    }

}

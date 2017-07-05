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
                                                           
public class jshell3 extends Object {                      
                                                           
    public static void main(String[] args){                
        String commandString = " ";                        
		        for (int i = 0; i < args.length ; i++){                    
            commandString += args[i]+ " ";                         
                                                                   
                                                                   
        }                                                          
                                                                   
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
                System.out.print("");                               
            else                                                    
                System.out.println("jshell failed");                
                                                                    
            AS400Message[] messagelist = command.getMessageList();  
                                                                    
            for (int i=0; i < messagelist.length; i++){             
        //      System.out.print(messagelist[i].getID());           
		                                                              
               System.out.print(messagelist[i].getText());    
                                                              
           }                                                  
                                                              
       }                                                      
       catch (Exception e)                                    
       {                                                      
           System.out.println("mjor failre");                 
       }                                                      
   }                                                          
                                                              
package terminal.cmds;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

//
//

public class CMDProcess extends Thread{
    private Process prc;
    private StringBuilder strBuilder;
    private final String pID;
    private final ProcessBuilder pBuilder;
 
//this class gets the pID of the user
    public CMDProcess(String pID, String... cmd) {
        this.pID = pID;
        pBuilder = new ProcessBuilder();
        
        //this was implemented but is showing errors on build and dont know how to 
        if (cmd.length < 3) {
            pBuilder.command("bash", "-c", cmd[0]);
        }
        else if (cmd.length < 4){
            pBuilder.command("bash", "-c", cmd[0], cmd[1]);
        }
        
        pBuilder.directory(new File(cmd[cmd.length-1]));
        //creates the output to the terminal
        System.out.println(Arrays.toString(cmd));
        strBuilder = new StringBuilder();

    }
    //get the result to push to the terminal
    public String resultGet() {
        return strBuilder.toString();
    }
    //checks if the terminal is running
    public boolean checkStatus(){
        if (prc != null){
            return prc.isAlive();
        }
        return false;
    }

    //returns the pID
    public String PIdGet() {
        return pID;
    }

    
    @Override
    public void run() {
        try {
            prc = pBuilder.start();
            BufferedReader read = new BufferedReader(new InputStreamReader(prc.getInputStream()));
            //create a line that is blank
            String line = null;
            while ((line = read.readLine()) != null){
                //after every line do a line break and print it out
                System.out.println(line);
                strBuilder.append(line).append("\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Stopped");
    }
}

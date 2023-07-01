package terminal.layout;



import java.beans.PropertyChangeListener;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.beans.PropertyChangeSupport;
import java.util.concurrent.atomic.AtomicInteger;
import java.net.UnknownHostException;

 //imports all of the packages form the project
import terminal.fileController.FileManager;
import terminal.cmds.CMDProcess;
import terminal.users.User;
import terminal.users.UserManager;

//
//

public class Model {

    
    private String usrName;
    private User user = null;
    private String pathCurrent; // stores current dir path
    private String cm = ""; //user input
    private UserManager usrManager;
    private AtomicInteger pIDs = new AtomicInteger(0);
    
    
    // manages the user files 
    private final FileManager fileManager; 
    private final PropertyChangeSupport propertyChngSupp = new PropertyChangeSupport(this);
    
    // stores the data that user sees on the line
    private final List<LineData> lnData = new ArrayList<>(); 
    private final List<CMDProcess> cmdProcesses = new ArrayList<>();


    public Model() {
        fileManager = FileManager.getInstance();
        //Stores the username and manager of the user
        try {
            usrName = InetAddress.getLocalHost().getHostName();
            usrManager = UserManager.getInstance();
        }
        
        catch (UnknownHostException e) {
            e.printStackTrace();
        }
        
        //sets the first command to login the user before they can use the terminal
        pathCurrent = "Login$";  
    }
    //This class wil be responcible for executing the commands that
    //are put into the terminal by the user
    public void executeCommand(String command) {
        //give the output to the user
        String output = "";
        //splits the command up and puts it into a array where each
        //part can be read seperatly
        String[] split = command.split(" ");
        //changes the cmd to be all lowercase
        String cmdLower = command.toLowerCase(Locale.ROOT);
        boolean Error = false;
        
        //This statement is used to add new users to the files
        if (cmdLower.contains("adduser")) {
            //this checks if there are enough statements in the command
            if (split.length < 5) {
                output = "Command should be :  super adduser <username> <password> <type std/super>.";
                Error = true;
            }
            
            //checks if the first input of the command is super.
            //and if not gives an error
            if (!split[0].equalsIgnoreCase("super")) {
                output = " Command should be :  super adduser <username> <password> <type std/super>.";
                Error = true;
            }
            if (Error) {
                //displays this to the user
                propertyChngSupp.firePropertyChange("command", output, command);
                return;
            }
        }
        
        //this cmd deletes a user.
        if (cmdLower.contains("deluser")) {
            //this checks if there are enough statements in the command
            if (split.length < 2) {
                output = "Command should be:  super deluser <username>.";
                Error = true;
            }
            //checks if the first input of the command is super.
            //and if not gives an error
            if (!split[0].equalsIgnoreCase("super")) {
                output = "Command should be: super deluser <username>.";
                Error = true;
            }
            //displays this to the user
            if (Error) {
                propertyChngSupp.firePropertyChange("command", output, command);
                return;
            }
        }
        
        //This cmd changes password
        if (cmdLower.contains("chpass")) {
            //this checks if there are enough statements in the command
            
            if (split.length < 3) {
                output = "Error: invalid command need super <username> <password>.";
                Error = true;
            }
            //checks if the first input of the command is super.
            //and if not gives an error
            if (!split[0].equalsIgnoreCase("super")) {
                output = "Error: invalid command need super <username> <password>.";
                Error = true;
            }
            //displays this to the user
            if (Error) {
                propertyChngSupp.firePropertyChange("command", output, command);
                return;
            }
        }
        
        
        if (cmdLower.contains("chusertype")) {
            //this checks if there are enough statements in the command
            if (split.length < 3) {
                output = "Error: invalid command need super <username> <type super/std>.";
                Error = true;
            }
            //checks if the first input of the command is super.
            //and if not gives an error
            if (!split[0].equalsIgnoreCase("super")) {
                output = "Error: invalid command need super <username> <type super/std>.";
                Error = true;
            }
            //displays this to the user
            if (Error) {
                propertyChngSupp.firePropertyChange("command", output, command);
                return;
            }
        }
        
        
        //this first if command handles the login when the terminal is launched
        if (split.length == 1) {
            //checks that there isnt a input
            if (user == null) {
                if (cm.isEmpty()) {
                    cm = command;
                    pathCurrent = "Password$";
                }
                else {
                    //this part checks if the user and pass match.
                    //and if not restart the login
                    User login = usrManager.login(cm, command);
                    if (login == null) {
                        output = "The Username does not match with the password";
                        pathCurrent = "Login$";
                        
                        cm = "";
                    }
                    else {
                        //gets the current dir
                        pathCurrent = fileManager.getCurrentFileName() + "$";
                        cm = "";
                        //puts the user into the login
                        user = login;
                        //outputs a welcome message
                        output = "hello " + user.getUserName();
                    }
                }
                
                
            } else {
                //checks if the first command is ls
                if (command.trim().equalsIgnoreCase("ls")) {
                    output = fileManager.listFiles();
                    //puts the variables into the the processbuilder
                    CMDProcess process = new CMDProcess(pIDs.addAndGet(1) + "", "dir", getClass().getClassLoader().getResource("").getPath() + pathCurrent.replace("$", ""));
                    //starts the processbuilder
                    process.start();
                    cmdProcesses.add(process);
                    
                    try {
                        process.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    
                    output = process.resultGet();
                    
                    //checks if the command is quit and uses system to exit
                } else if (command.trim().equalsIgnoreCase("quit")) {
                    System.exit(0);
                    
                    
                } else if (command.trim().equalsIgnoreCase("whoami")) {
                    //Changes the output to the uesrname 
                    output = user.getUserName();
                    //launches a processbuilder that has the user name
                    CMDProcess process = new CMDProcess(pIDs.addAndGet(1) + "", "whoami", getClass().getClassLoader().getResource("").getPath() + pathCurrent.replace("$", ""));
                    //starts
                    process.start();
                    cmdProcesses.add(process);
                    
                    try {
                        process.join();
                        
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    
                    //outputs the result of the processbuilder.
                    output = output + " " + process.resultGet();
                    
                }
                //this get all the users in the file and lists them
                else if (command.trim().equalsIgnoreCase("lsuser")) {
                    output = usrManager.listAllUser();

                }
                //If command invalid give error 
                else {
                    output = "The " + command + " command was not found.";
                }
            }
            
            //this part of the code checks all cmds with 2 statements
        } else if (split.length == 2) {
            //checks mkdir command
            if (split[0].trim().equalsIgnoreCase("mkdir")) {
                //takes the second statement and makes a directory
                fileManager.makeDirectory(split[1]);
                //starts processbuilder
                CMDProcess process = new CMDProcess(pIDs.addAndGet(1) + "", "mkdir", split[1], getClass().getClassLoader().getResource("").getPath() + pathCurrent.replace("$", ""));
                
                process.start();
                cmdProcesses.add(process);
            }

            //checks for the cd command
            else if (split[0].trim().equalsIgnoreCase("cd")) {
                //checks the second statemnt of the cd command
                output = fileManager.cd(split[1]);
                
                //if second statement == .. then go to home dir
                if (split[1].equals("..")) {
                    
                    String[] splits = pathCurrent.split("/");
                    pathCurrent = "";
                    
                    //if length of cm is 2 or under
                    if (splits.length <= 2) {
                        pathCurrent = fileManager.getCurrentFileName();
                    }
                    //goes through all the statements and adds a / between the words
                    for (int i = 1; i < splits.length -1 ; i++) {
                        
                        pathCurrent += "/" + splits[i];
                    }
                    //puts a $ at the end
                    pathCurrent += "$";
                    
                    
                }
                else {
                    //if there isnt a error change the current path
                    if (!output.isEmpty() & !output.contains("Error")) {
                        
                        pathCurrent = pathCurrent.replace("$", (pathCurrent.equals("Home$") ? "" : "Home") + output + "$");
                    }
                }
            } else {
                //invalid command
                output = "The " + command + " command was not found.";
            }
            
            //checks all the 3 statement commands
        } else if (split.length == 3) {
            
            if (split[1].trim().equalsIgnoreCase("delUser")) {
                //checks if user has permission of super
                if (user.isSuperUser()) {
                    //puts the output to del the user
                    output = usrManager.delUser(split[2]);
                } else {
                    //gives error
                    output = "Super privilages are required";
                }
            }

            
            
            //checks all the 4 statement commands
        } else if (split.length == 4) {
            
            if (split[1].trim().equalsIgnoreCase("chPass")) {
                //checks if user has permission of super
                if (user.isSuperUser()) {
                    //changes the pass
                    output = usrManager.passChange(split[2], split[3]);
                } else {
                    //gives error
                    output = "Super privilages are required";
                }
            }
            
            
            //checks all 5 statement commands
        } else if (split.length == 5) {
            
            if (split[1].trim().equalsIgnoreCase("addUser")) {
                //checks if user has permission of super
                if (user.isSuperUser()) {
                    //adds a new user to the file
                    output = usrManager.addUser(split[2], split[3], split[4].equalsIgnoreCase("super"));
                } else {
                    //gives error
                    output = "Super privilages are required";
                }
            }
        }

        propertyChngSupp.firePropertyChange("command", output, command);
    }

    public void addListener(PropertyChangeListener listener) {
        propertyChngSupp.addPropertyChangeListener(listener);
    }

    public void addLnData(LineData lineData) {
        this.lnData.add(lineData);
    }
    
    public int lineDataSize() {
        return lnData.size();
    }

    public LineData getLineData(int index) {
        return lnData.get(index);
    }

    public String getUsrName() {
        return usrName;
    }

    public String getPathCurrent() {
        return pathCurrent;
    }


}

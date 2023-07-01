package terminal.users;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

// This class is used to manage all of the commands that have to do with the user file
//

public class UserManager {
    //Sets variables that are used to handle all the user file commands
    private final List<User> users;
    private boolean fileNotFound = false;
    private static final String USR_FILE = "users.txt";
    private static UserManager usrManage = null;
  

    private UserManager() throws IOException, ClassNotFoundException {
        //loads all of the users
            users = loadUsr();
            //if cant find file make one
            if (fileNotFound){
                dataWrite();
            }
    }
//creates a new uesrmanager variable if one is not found
    public static UserManager getInstance() {
        if (usrManage == null){
            try {
                usrManage = new UserManager();
                
            }
            catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return usrManage;
    }
    
        //this command deals with deleting a user
        public String delUser(String usrname){
        User user = null;
        for (User u:users){
            //checks if the username stated by the command exists
            if (u.getUserName().equals(usrname)) {
                user = u;
                break;
            }
        }
        //gives error if user doesnt exist
        if (user == null){
            return "User : "+usrname+" is invalid/ doesnt exist.";
        }
        //if it finds a user it removes them
        users.remove(user);
        dataWrite();
        return "The user :  "+ usrname + " had been removed from the file.";
    }
    
        
     public String addUser(String usrname, String pass, boolean superUser){
        User user = null;
        for (User u:users){
            //cycles through all the users
            if (u.getUserName().equals(usrname)) {
                //checks if they match
                user = u;
                break;
            }
        }
        if (user != null){
            // is the user comes back to existing show error
            return "Error: User "+usrname+" already exists in the file.";
        }
        //carry this out if user doesnt exist
        user = new User(usrname,pass,superUser);
        users.add(user);
        //writes the userdata
        dataWrite();
        return usrname+" created.";
    }

     //Validates the login info and if it isnt correct returns nothing
    public User login(String username, String password){
        //cycles through the file
        for (User user:users){
            //validates given name to file
            if (user.getUserName().equals(username)){
                //validates given pass to file
                if (user.getPassword().equals(password)){
                    //returns the username and password
                    return user;
                }
            }
        }
        
        return null;
    }


//this command deals with changing the password
    public String passChange(String username, String password){
        User user = null;
        for (User u:users){
            //cycles through the users
            if (u.getUserName().equals(username)) {
                //sets the user to value of u
                user = u;
                break;
            }
        }
        //if no user then give error
        if (user == null){
            return "Error: User "+username+" does not exists.";
        }
        //sets the password of the user
        user.setPassword(password);
        //changes the value
        dataWrite();
        return "User"+ username + "'s password was changed.";
    }
    //creates a list that will create a file which has the base root login
    private List<User> loadUsr() throws ClassNotFoundException,IOException {
        List<User> users = new ArrayList<>();
        File file = new File(USR_FILE);
        
        //if the file doesnt exist add the admin root account
        if (!file.exists()){
            User root = new User("root","root",true);
            users.add(root);
            fileNotFound = true;
            return users;
        }
        //uses ois to add the user to the file
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
        Object read = null;
        //adds the root user to the existing file
        while ((read = ois.readObject()) != null){
            //while it can read the file
            User user = (User) read;
            //add to file
            users.add(user);
        }
        //closes the ois
        ois.close();
        return users;
    }

    private void dataWrite(){
        File file = new File(USR_FILE);
        try {
            //if the file doesnt exist make the file
            if (!file.exists()) {
                boolean newFile = file.createNewFile();
            }
            //encodes objects with OOS
            ObjectOutputStream OOS = new ObjectOutputStream(new FileOutputStream(file));
            for (User user : users) {
                //encode all the users
                OOS.writeObject(user);
            }
            //clear out the OOS
            OOS.writeObject(null);
            OOS.flush();
            //Close
            OOS.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    //lists all the users in the file
    public String listAllUser(){
        StringBuilder s = new StringBuilder();
        for (User user:users){
            //get all of the users in the users file and add them to the string builder
            //whilest also creating a line break between all
            s.append(user.getUserName()).append("\n");
        }
        return s.toString();
    }

}

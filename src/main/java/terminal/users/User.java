package terminal.users;

import java.io.Serializable;

//
//

public class User implements Serializable {
    private String userName;
    private String password;
    private boolean superUser;
// makes a variable which stores the user , pass , and type
    public User(String userName, String password, boolean superUser) {
        
        this.userName = userName;
        this.password = password;
        this.superUser = superUser;
    }
//gets the current username
    public String getUserName() {
        return userName;
    }
//sets the current username
    public void setUserName(String userName) {
        //assigns userName to userName
        this.userName = userName;
    }
//sets the current pass
    public void setPassword(String password) {
        this.password = password;
    }
//gets the current pass
    public String getPassword() {
        return password;
    }

//checks if super
    public boolean isSuperUser() {
        return superUser;
    }
//set a user to super type
    public void setSuperUser(boolean superUser) {
        this.superUser = superUser;
    }
}

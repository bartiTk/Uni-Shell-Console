package terminal.cntrls;

import terminal.layout.Model;

//Stores the main model class in here so that it can be
//controlled by the other classes

public abstract class MainControl {
    //create protected model
    protected Model model;

    public abstract void setModel(Model model);
}

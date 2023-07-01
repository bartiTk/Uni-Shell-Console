package terminal.cntrls;


import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import terminal.layout.LineData;
import terminal.layout.Model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

//This class is used to define how the terminal gets and shows the input
//That the user is providing. 

public class LineController extends MainControl implements PropertyChangeListener {
    public Label usrName;
    public Label dir;
    public Label cmd;
    public TextField txtInput;
    private int indx = 0;

    @Override
    public void setModel(Model mdl) {
        this.model = mdl;
        mdl.addListener(this);
        //gets the username from the model class
        usrName.setText(mdl.getUsrName());
        //gets the directory from the model class
        dir.setText(mdl.getPathCurrent());
        //gest the size of the input
        indx = mdl.lineDataSize();
    }
//this class checks when keys are pressed
    public void keyPressed(KeyEvent keyAction) {
        KeyCode code = keyAction.getCode();
        //checks if the user pressed the enter key
        if (code == KeyCode.ENTER){
            //if the input is empty ask for input again
            if (txtInput.getText().isEmpty()) return;
            model.executeCommand(txtInput.getText());
        }
        //this part of the code checks when the user presses the up button and gets the last input they put in
        if (code == KeyCode.UP){
            indx--;
            if (indx < 0){
                indx = 0;
                return;
            }
            //this part displays the last input
            LineData lndata = model.getLineData(indx);
            txtInput.setText(lndata.getControll().getCmd());

        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if (event.getPropertyName().equals("command")){
            cmd.setText(txtInput.getText());
            //sets the test field to be invisable
            txtInput.setVisible(false);
            //this sets the cmd label to be visible 
            cmd.setVisible(true);
        }
    }
//
    public String getCmd() {
        //gets the text from the command
        return cmd.getText();
    }
}

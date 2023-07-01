package terminal.cntrls;
//imports javafx
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
//loads the fxml file
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.geometry.Insets;
//gets the layout
import terminal.layout.LineData;
import terminal.layout.Model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.io.IOException;

import java.net.URISyntaxException;
import java.net.URL;

import java.util.Objects;
import java.util.ResourceBundle;

//This class controlls how the terminal functions for the user
//Also controlls how it is presented

public class TerminalControl extends MainControl implements Initializable,PropertyChangeListener {

//makes public variables to work with fxml files
    public ScrollPane terminal;
    public VBox vbox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //set the terminal box up with the terminal fxml
        vbox.minHeightProperty().bind(terminal.heightProperty());
        //sets the heightproperty so that is can be adjusted by the user
        vbox.heightProperty().addListener(observable -> terminal.setVvalue(1D));
    }

    @Override
    public void setModel(Model model) {
        this.model = model;
        model.addListener(this);
        //This part of the code loads the line fxml file which contains info on
        //how to display different parts of the code
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/line.fxml"));
        
        try {
            //loads the Hbox and linecontroller
            HBox ld = loader.load();
            LineController lnCntrl = loader.getController();
            //sets the controller to the model
            lnCntrl.setModel(model);
            vbox.getChildren().add(ld);
            //adds the linedata to the model
            LineData lndata = new LineData(ld, lnCntrl);
            model.addLnData(lndata);
            
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evnt) {
        String valueNew = (String) evnt.getNewValue();
        //if the newvalue is clear then clear the childern in the Vbox
        if (valueNew.equalsIgnoreCase("clear")){
            //clear childern
            vbox.getChildren().clear();
            
        }
        else {
            String lstFiles = (String) evnt.getOldValue();
            
            if (!lstFiles.isEmpty()) {
                //if the string isnt empty then create a new label
                Label lbl = new Label(lstFiles);
                
                try {
                    //gets the css file to use to display the terminal
                    String cssPath = Objects.requireNonNull(getClass().getClassLoader().getResource("css/styles.css")).toURI().toString();
                    //splits the css file up
                    String[] split = cssPath.split(":");
                    //changes the style of the label
                    lbl.setStyle(split[split.length-1]);
                    lbl.getStyleClass().add("results");
                } 
                catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                //sets how the text is padded
                lbl.setPadding(new Insets(0,0,0,5));
                //sets the font on the label
                lbl.setFont(new Font(14));
                
                vbox.getChildren().add(lbl);
            }
        }
        //Loads the line.fxml file which contains formatting for the terminal
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/line.fxml"));
        try {
            //create hbox and linecontroller 
            HBox load = loader.load();
            LineController lnCntrl = loader.getController();
            //sets the linecontroller to the model
            lnCntrl.setModel(model);
            vbox.getChildren().add(load);
            //creates linedata through linecontroller
            LineData lndata = new LineData(load, lnCntrl);
            model.addLnData(lndata);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

package terminal;

import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;

import javafx.stage.Stage;

import javafx.application.Application;

import javafx.fxml.FXMLLoader;


//import other packages
import terminal.cntrls.TerminalControl;
import terminal.layout.Model;

//This class controls the main function
//

public class Terminal extends Application {
    @Override
    //This code sets up the termainal that is going to be shown to the user using javafx
    public void start(Stage window) throws Exception {
        //loads the terminal fxml file
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/terminal.fxml"));
        ScrollPane ld = loader.load();
        //gets controller from file and sets a model
        TerminalControl Tcontroller = loader.getController();
        Tcontroller.setModel(new Model());
        //sets up and shows the terminal to the user
        // sets scene
        window.setScene(new Scene(ld));
        //sets name
        window.setTitle("Terminal"); 
        window.show();
    }

    public static void main(String[] args) {
        // launches the termainal
        launch(args); 
    }
}

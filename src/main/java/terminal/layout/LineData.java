package terminal.layout;

import javafx.scene.layout.HBox;
import terminal.cntrls.LineController;

//This class stores variables for the renderingof the terminal
//

public class LineData{
    //uses the Hbox to allign the linedata
    private final HBox view;
    private final LineController controller;
    
    public HBox viewGet() {
        //gets the view of the box
        return view;
    }
    public LineData(HBox view, LineController controller) {
        //uses the Hbox to allign the linedata
        this.view = view;
        this.controller = controller;
    }

 

    public LineController getControll() {
        //gets the controller for the line data
        return controller;
    }
}

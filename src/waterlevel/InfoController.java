package waterlevel;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

/**
 * Class: InfoController
 * Version: 0.1
 * Created on 09.06.2016 with the help of IntelliJ IDEA (thanks!)
 * Author: Evnica
 * Description:
 */
public class InfoController
{
    public Label infoLabel;
    public Button infoButton;
    public GridPane infoPane;
    private Main main;

    void setMainApp(Main main) {
        this.main = main;
    }

    @FXML
    public void initialize() throws Exception
    {
        infoButton.defaultButtonProperty().setValue( true );
        infoButton.setOnAction( event -> {
            infoButton.getScene().getWindow().hide();
        } );
    }

}

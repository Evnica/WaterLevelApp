package com.evnica.waterlevelwithgui;

import com.evnica.waterlevelwithgui.Main;
import com.evnica.waterlevelwithgui.logic.DatabaseOperator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

/**
 * Class: DialogController
 * Version: 0.1
 * Created on 08.06.2016 with the help of IntelliJ IDEA (thanks!)
 * Author: Evnica
 * Description:
 */
public class DialogController
{
    private Main main;
    public Button yesExit;
    public Button noExit;
    public GridPane dialogPane;

    @FXML
    public void initialize() throws Exception
    {
        yesExit.defaultButtonProperty().setValue( true );
        yesExit.setOnAction( event -> {
            try
            {
                DatabaseOperator.dropTable( "measurements");
            } catch ( Exception e )
            {
                e.printStackTrace();
            }
            DatabaseOperator.closeConnection();
            yesExit.getScene().getWindow().hide();
        } );
        noExit.setOnAction( event -> {
            main.show();
            noExit.getScene().getWindow().hide();
        } );
    }

    void setMainApp(Main main) {
        this.main = main;
    }

}

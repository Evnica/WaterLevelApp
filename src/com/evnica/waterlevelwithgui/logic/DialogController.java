package com.evnica.waterlevelwithgui.logic;

import com.evnica.waterlevelwithgui.Main;
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
            Main.stations.forEach( station -> DatabaseOperator.dropTable( station.place ) );
            DatabaseOperator.closeConnection();
            yesExit.getScene().getWindow().hide();
        } );
        noExit.setOnAction( event -> {
            main.show();
            noExit.getScene().getWindow().hide();
        } );
    }

    public void setMainApp(Main main) {
        this.main = main;
    }

}

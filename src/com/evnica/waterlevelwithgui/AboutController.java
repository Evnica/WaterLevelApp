package com.evnica.waterlevelwithgui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

/**
 * Class: AboutController
 * Version: 0.1
 * Created on 09.06.2016 with the help of IntelliJ IDEA (thanks!)
 * Author: Evnica
 * Description:
 */
public class AboutController
{
    public GridPane aboutPane;
    public Label aboutLabel;
    public Button aboutButton;

    @FXML
    public void initialize() throws Exception
    {
        aboutButton.defaultButtonProperty().setValue( true );
        aboutButton.setOnAction( event -> {
            aboutButton.getScene().getWindow().hide();
        } );
    }
}

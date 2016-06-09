package com.evnica.waterlevelwithgui;

import com.evnica.waterlevelwithgui.logic.Formatter;
import com.evnica.waterlevelwithgui.logic.ReportParameters;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class MainController
{
    private ReportParameters reportParameters = new ReportParameters();
    private Main main;
    @FXML
    private Button createButton;
    @FXML
    private Button exitButton;
    @FXML
    private Button aboutButton;
    @FXML
    private Button optionsButton;
    @FXML
    private GridPane gridPane;
    @FXML
    private ComboBox<String> stationCombo;
    @FXML
    private ComboBox<String> formatCombo;
    @FXML
    private DatePicker startPicker;
    @FXML
    private DatePicker endPicker;

    public void setMainApp(Main main) {
        this.main = main;
    }

    @FXML
    public void initialize() throws Exception
    {
        createButton.defaultButtonProperty().setValue( true );


        exitButton.addEventHandler( MouseEvent.MOUSE_CLICKED, e -> main.getPrimaryStage().hide() );
        exitButton.addEventHandler( KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode().equals( KeyCode.ENTER ))
            {
                main.getPrimaryStage().hide();
            }
        } );

        //GUI completion

        ObservableList<String> stationNames = FXCollections.observableArrayList();
        Main.stations.forEach( station -> stationNames.add( station.fullName()));

        //fill in station combo, save default value to report parameters and add listener
        stationCombo.getItems().addAll( stationNames );
        stationCombo.setValue( stationNames.get( 0 ));
        reportParameters.setStationName( Main.stations.get( 0 ).place );
        stationCombo.getSelectionModel().selectedItemProperty().addListener( (obs, old, newV) -> {
            int i = stationCombo.getSelectionModel().getSelectedIndex();
            reportParameters.setStationName( Main.stations.get( i ).place );
        } );

        //formal presence as there are no other parameters
        formatCombo.setValue( "BS2016SS" );

        //
        String now = LocalDate.now().format( Formatter.getDateFormatter() );
        int day = Integer.parseInt( now.substring( 0, 2 ) );
        int month = Integer.parseInt( now.substring( 3, 5 ) );
        int year = Integer.parseInt( now.substring( 6 ) );
        startPicker.setValue( LocalDate.of( year, month, day-7 ) );
        endPicker.setValue( LocalDate.now());



        String startDate, endDate;
        endDate = LocalDate.now().format( Formatter.getDateFormatter() );
        startDate = LocalDate.of( year, month, day-7 ).format( Formatter.getDateFormatter() );

        String startTime, endTime;
        startTime = LocalTime.now().format( Formatter.getTimeFormatter() );
        endTime = LocalTime.now().format( Formatter.getTimeFormatter() );




        TimeSpinner startSpinner = new TimeSpinner(  );
        startSpinner.setEditable( true );
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        startSpinner.valueProperty().addListener((obs, oldTime, newTime) ->
                System.out.println(formatter.format(newTime)));
        gridPane.add( startSpinner, 3, 3 );


        TimeSpinner endSpinner = new TimeSpinner(  );
        startSpinner.setEditable( true );
        endSpinner.valueProperty().addListener((obs, oldTime, newTime) ->
                System.out.println(formatter.format(newTime)));
        gridPane.add( endSpinner, 3, 4 );

    }

    public ReportParameters getReportParameters()
    {
        return reportParameters;
    }
}

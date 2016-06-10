package com.evnica.waterlevelwithgui;

import com.evnica.waterlevelwithgui.logic.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class MainController
{
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

    private LocalDate startDate, endDate;
    private LocalTime startTime, endTime;
    private ReportParameters reportParameters = new ReportParameters();
    private Main main;
    private int stationIndex;
    private List<DayMeasurement> measurements;

    private static final Logger LOGGER = LogManager.getLogger(MainController.class);

    void setMainApp(Main main) {
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
        DataStorage.getStations().forEach( station -> stationNames.add( station.fullName()));

        //fill in station combo
        stationCombo.getItems().addAll( stationNames );
        if ( stationNames.size() > 0 )
        {
            stationCombo.setValue( stationNames.get( 0 ) );

            // save default name and table values to report parameters
            reportParameters.setStationName( DataStorage.getStations().get( 0 ).fullName() );
            System.out.println( "Station name in report parameters: " + DataStorage.getStations().get( 0 ).fullName() );
            reportParameters.setTableName( DataStorage.getStations().get( 0 ).place );
            System.out.println( "Table name in report parameters: " + DataStorage.getStations().get( 0 ).place );
            // and add listener
        }
        stationCombo.getSelectionModel().selectedItemProperty().addListener( (obs, old, newV) -> {
            stationIndex = stationCombo.getSelectionModel().getSelectedIndex();
            System.out.println(stationIndex + " station index");
        } );

        //formal presence as there are no other parameters
        formatCombo.setValue( "BS2016SS" );

        // prepare strings for date settings (in pickers)
        LocalTime nowTime = LocalTime.now();

        //settings for starting the app with pickers with current dates; as the test data is for 2015, the default date
        // values in pickers are changed accordingly

        /*LocalDate nowDate = LocalDate.now();
        String now = nowDate.format( Formatter.getDATE_FORMATTER_ddMMyyyy() );
        int day = Integer.parseInt( now.substring( 0, 2 ) );
        int month = Integer.parseInt( now.substring( 3, 5 ) );
        int year = Integer.parseInt( now.substring( 6 ) );*/

        //set default report parameters for start and end dates
        String endDateParameter, startDateParameter, startTimeParameter, endTimeParameter;

        /*endDateParameter = nowDate.format( Formatter.getDATE_FORMATTER_ddMMyyyy() );
        startDateParameter = LocalDate.of( year, month, day-7 ).format( Formatter.getDATE_FORMATTER_ddMMyyyy() );

        startTimeParameter = nowTime.format( Formatter.getTIME_FORMATTER_HHmm() );
        endTimeParameter = nowTime.format( Formatter.getTIME_FORMATTER_HHmm() );*/

        //set default dates
        startDate = LocalDate.of( 2015, 1, 31 );
        endDate = LocalDate.of( 2015, 2, 6 );

        startDateParameter = Formatter.getDATE_FORMATTER_ddMMyyyy().format( startDate );
        endDateParameter = Formatter.getDATE_FORMATTER_ddMMyyyy().format( endDate );
        startTimeParameter = endTimeParameter = Formatter.getTIME_FORMATTER_HHmm().format( nowTime );

        reportParameters.setDateFrom( startDateParameter + " " + startTimeParameter );
        reportParameters.setDateTo( endDateParameter + " " + endTimeParameter );

        // set date pickers to default values

        startPicker.setValue( startDate );
        endPicker.setValue( endDate);

        startPicker.valueProperty().addListener( (obs, oldDate, newDate) -> {
            System.out.println(newDate + " start date");
            startDate = newDate;} );
        endPicker.valueProperty().addListener( (obs, oldDate, newDate) -> {
            System.out.println(newDate + " end date");
            endDate = newDate;} );

        startTime = nowTime;
        endTime = nowTime;
        TimeSpinner startSpinner = new TimeSpinner(  );
        startSpinner.setEditable( true );
        startSpinner.valueProperty().addListener( (obs, oldTime, newTime) -> {
        startTime = newTime;
            System.out.println(startTime + " start time");});
        startSpinner.addEventHandler( KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode().equals( KeyCode.ENTER ))
            {
                startTime = startSpinner.getValue();
                System.out.println(startTime + "start time");
            }
        } );

        gridPane.add( startSpinner, 3, 3 );

        TimeSpinner endSpinner = new TimeSpinner(  );
        startSpinner.setEditable( true );
        endSpinner.valueProperty().addListener((obs, oldTime, newTime) -> {
            endTime = newTime;
            System.out.println(endTime + " end time");});
        endSpinner.addEventHandler( KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode().equals( KeyCode.ENTER ))
            {
                endTime = startSpinner.getValue();
                System.out.println(startTime + "end time");
            }
        } );

        gridPane.add( endSpinner, 3, 4 );

        createButton.addEventHandler( MouseEvent.MOUSE_CLICKED, event -> {

            createReport();
            displayPdf();

        });

        aboutButton.addEventHandler( MouseEvent.MOUSE_CLICKED, event -> {
            try
            {
                createAboutDialog();
            } catch ( IOException e )
            {
                e.printStackTrace();
            }

        } );

    }

    private void createAboutDialog() throws IOException
    {
        FXMLLoader dialogLoader = new FXMLLoader(getClass().getResource( "about.fxml" ));
        Parent dialogRoot = dialogLoader.load();
        AboutController aboutController = dialogLoader.getController();
        aboutController.aboutLabel.setText(
                "This program is intended to create PDF reports based on \n " +
                        ".txt measurement data in BS2016SS format (other formats \n" +
                        "are not supported yet). Files from the /resources folder \n" +
                        "should contain one measurement station each. Duplicates \n" +
                        "are not processed correctly. App is not intended to be \n" +
                        "of production quality. GUI has to be further developed. \n" +
                        "It's not curious-user-proof. Created PDFs are opened \n" +
                        "with the default system program, if such exists." );
        Stage about = new Stage(  );
        about.setTitle( "Über das Programm" );
        about.getIcons().add( new javafx.scene.image.Image( Main.class.getResourceAsStream( "../../../assets/drop.png" ) ) );
        Scene aboutScene = new Scene( dialogRoot, 450, 300 );
        aboutScene.getStylesheets().add( this.getClass().getResource( "waterlevelstyle.css" ).toExternalForm() );
        about.setScene( aboutScene );
        about.setResizable( false );
        about.show();
    }

    private void createReport(){

        Station chosenStation = DataStorage.getStations().get( stationIndex );
        measurements = chosenStation.getMeasurementsWithinInterval( startDate, startTime, endDate, endTime );
        System.out.println(chosenStation.fullName() + " chosen for report creation");
        JasperAssistant.init( measurements );
        int numberOfEntries = JasperAssistant.countSizeOfData();

        if (numberOfEntries>0)
        {
            //measurements.forEach( System.out::println );
            reportParameters.setStationName( chosenStation.fullName() );
            reportParameters.setDateFrom( Formatter.getDATE_TIME_FORMATTER_ddMMyyyy_HHmm()
                    .format( LocalDateTime.of( startDate, startTime ) ) );
            System.out.println("Start of interval " + Formatter.getDATE_TIME_FORMATTER_ddMMyyyy_HHmm().
                    format( LocalDateTime.of( startDate, startTime ) ));
            reportParameters.setDateTo( Formatter.getDATE_TIME_FORMATTER_ddMMyyyy_HHmm()
                    .format( LocalDateTime.of( endDate, endTime ) ) );
            System.out.println("End of interval " + Formatter.getDATE_TIME_FORMATTER_ddMMyyyy_HHmm().
                    format( LocalDateTime.of( endDate, endTime ) ));
            reportParameters.setMeasurements( measurements ); //automatically fills the parameter map with availableTo and availableFrom parameters
            int everyNth = (numberOfEntries - 1) / 60 + 1; // 60 points are displayed relatively comfortably, more is too tight
            try
            {
                DatabaseOperator.insertDayMeasurements( measurements, everyNth );
                reportParameters.getParameters().put("REPORT_CONNECTION", DatabaseOperator.getConnection());
                String sourceFileName =
                        "../WaterLevelApp/assets/waterLevelApp.jasper";

                JasperPrint jasperPrint = JasperFillManager.fillReport( sourceFileName, reportParameters.getParameters(),
                        new JRTableModelDataSource( JasperAssistant.getTableModel() ));
                JasperExportManager.exportReportToPdfFile( jasperPrint, "report.pdf" );
                System.out.println("A file 'report.pdf' created ");
                DatabaseOperator.deleteFromTable( "measurements" );
            }
            catch ( Exception e )
            {
                try
                {
                    main.showInfoDialog(   "Die Datei report.pdf wurde \n" +
                                         "in WaterLevelApp Ordner erstellt" );
                } catch ( Exception e1 )
                {
                    LOGGER.error( "Can't inform user that report was created", e1 );
                }
            }


        }
        else
        {
            try
            {
                main.showInfoDialog( "Es gibt keine Messdaten für den angegebenen " +
                                     "\nZeitintervall. Bitte wählen Sie ein \n" +
                                     "anderes Anfangsdatum bzw. Enddatun" );
            } catch ( Exception e )
            {
                LOGGER.error( "Can't inform user that there's no data for the chosen time frame", e );
            }
        }
    }

    private void displayPdf()
    {
        if ( Desktop.isDesktopSupported()) {
            try {
                File myFile = new File("report.pdf");
                Desktop.getDesktop().open(myFile);
            } catch (IOException ex)
            {
                // no application registered for PDFs
            }
        }
    }

    public ReportParameters getReportParameters()
    {
        return reportParameters;
    }



}

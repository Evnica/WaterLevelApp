package com.evnica.waterlevelwithgui;

import com.evnica.waterlevelwithgui.logic.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    private Stage primaryStage;
    private Stage dialog;
    public static List<Station> stations = new ArrayList<>();
    private static final Logger LOGGER = LogManager.getLogger(DataReader.class);

    @Override
    public void start(Stage primaryStage) throws Exception{

        if ( !loadData() )
        {
           LOGGER.error( "Data not loaded. Ooooooops..." );
        }

        this.primaryStage = primaryStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource( "main.fxml" ));
        Parent root = loader.load();
        MainController controller =  loader.getController();
        controller.setMainApp( this );
        primaryStage.setTitle("Pegelberichte");
        primaryStage.getIcons().add( new Image( Main.class.getResourceAsStream( "../../../assets/drop.png" ) ) );
        Scene scene = new Scene(root, 560, 300);
        scene.getStylesheets().add( this.getClass().getResource( "waterlevelstyle.css" ).toExternalForm() );
        primaryStage.setScene(scene);
        dialog = new Stage( );

        primaryStage.setOnHiding( event -> {
        try
        {
            FXMLLoader dialogLoader = new FXMLLoader(getClass().getResource( "dialog.fxml" ));
            Parent dialogRoot = dialogLoader.load();
            DialogController dialogController = dialogLoader.getController();
            dialogController.setMainApp( this );
            dialog.setTitle( "Bestätigung" );
            dialog.getIcons().add( new Image( Main.class.getResourceAsStream( "../../../assets/drop.png" ) ) );
            Scene dialogScene = new Scene( dialogRoot, 320, 145 );
            dialogScene.getStylesheets().add( this.getClass().getResource( "waterlevelstyle.css" ).toExternalForm() );
            dialog.setScene( dialogScene );
            dialog.setResizable( false );
            dialog.show();
        }
        catch ( IOException e )
        {
                Label label = new Label( "Möchten Sie den Vorgang beenden?" );
                Button okButton = new Button( "Ja" );
                okButton.setOnAction( event1 -> dialog.hide() );
                Button cancelButton = new Button( "Nein" );
                cancelButton.setOnAction( event1 -> {
                    primaryStage.show();
                    dialog.hide();
                } );
                FlowPane pane = new FlowPane( 10, 10 );
                pane.setAlignment( Pos.CENTER );
                pane.getChildren().addAll( okButton, cancelButton );
                VBox vBox = new VBox( 10 );
                vBox.setAlignment( Pos.CENTER );
                vBox.getChildren().addAll( label, pane );
                Scene scene1 = new Scene( vBox );
                dialog.setScene( scene1 );
                dialog.show();
            }
        } );

        primaryStage.show();
    }

    public void show()
    {
        this.primaryStage.show();
    }

    public Stage getPrimaryStage()
    {
        return primaryStage;
    }

    private boolean loadData()
    {
        boolean success;

        try
        {
            List<File> resources = DataReader.listAllFilesInResources( "../WaterLevelApp/resources" );
            List<List<String>> data = new ArrayList<>( resources.size() );
            for (File source: resources)
            {
                data.add( DataReader.readData( source ) );
            }

            for ( List<String> fileContent: data )
            {
                stations.add( DataProcessor.convertTextIntoStation( fileContent ) );
            }
            if ( DatabaseOperator.connect() )
            {
                stations.forEach( DatabaseOperator::insert );
            }
            success = true;
        }
        catch ( IOException e )
        {
            success = false;
            LOGGER.error( "Can't load data", e );
        }
        return success;
    }

    public static void main( String[] args) {
        launch(args);
    }
}

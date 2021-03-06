package waterlevel;

import javafx.application.Application;
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

public class Main extends Application {

    private Stage primaryStage;
    private Stage dialog;
    private Stage info;
    private static final Logger LOGGER = LogManager.getLogger(DataReader.class);

    @Override
    public void start(Stage primaryStage) throws Exception{
        boolean noResources = false;
        try{

            DataStorage.fillTheStorage( "./resources" ); //"../WaterLevelApp/resources"
            LOGGER.error(new File( "./resources" ).getAbsolutePath());
        }
        catch ( IOException e )
        {
            LOGGER.error( "Can't read the data source(s) ", e );
            noResources = true;
        }
        finally
        {
            this.primaryStage = primaryStage;
            FXMLLoader loader = new FXMLLoader(getClass().getResource( "main.fxml" ));
            try
            {
                Parent root = loader.load();
                MainController controller =  loader.getController();
                controller.setMainApp( this );
                primaryStage.setTitle("Pegelberichte");
                try
                {
                    LOGGER.error(new File( "assets/drop.png" ).getAbsolutePath());
                    primaryStage.getIcons().add( new Image( "assets/drop.png" ) ); //"../../../assets/drop.png"
                } catch ( Exception e )
                {
                    LOGGER.error( "", e );
                }
                Scene scene = new Scene(root, 560, 300);
                scene.getStylesheets().add( this.getClass().getResource( "waterlevelstyle.css" ).toExternalForm() );
                primaryStage.setScene(scene);
            } catch ( IOException e )
            {
                LOGGER.error( "Can't load GUI", e );
            }

            dialog = new Stage( );

            primaryStage.setOnHiding( event -> {
            try
            {
                FXMLLoader dialogLoader = new FXMLLoader(getClass().getResource( "dialog.fxml" ));
                Parent dialogRoot = dialogLoader.load();
                DialogController dialogController = dialogLoader.getController();
                dialogController.setMainApp( this );
                dialog.setTitle( "Bestätigung" );
                try
                {
                    dialog.getIcons().add( new Image( "assets/drop.png" ) ); //"../../../assets/drop.png"
                } catch ( Exception e )
                {
                    LOGGER.error( "", e );
                }
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
            primaryStage.setResizable( false );
            primaryStage.show();

            if (noResources)
            {
                try
                {
                    showInfoDialog( "Es gibt keine Datenquellen. " +
                            "\nBitte kopieren Sie die Quelldateien " +
                            "\nin den /resources Ordner und starten " +
                            "\ndas Programm neu.");
                } catch ( Exception e )
                {
                    LOGGER.error( "Can't inform user that there are no data sources", e );
                }
            }

        }
    }

    void show()
    {
        this.primaryStage.show();
    }

    Stage getPrimaryStage()
    {
        return primaryStage;
    }

    public static void main( String[] args) {
        launch(args);
    }

    void showInfoDialog(String message) throws Exception
    {
        FXMLLoader infoLoader = new FXMLLoader(getClass().getResource( "infodialog.fxml" ));
        Parent infoRoot = infoLoader.load();
        InfoController infoController = infoLoader.getController();
        infoController.setMainApp( this );
        info = new Stage( );
        info.setTitle( "Info" );
        try
        {
            info.getIcons().add( new Image( "assets/drop.png" ) ); //"../../../assets/drop.png"
        } catch ( Exception e )
        {
            LOGGER.error( "", e );
        }
        infoController.infoLabel.setText( message );
        Scene infoScene = new Scene( infoRoot, 320, 165 );
        infoScene.getStylesheets().add( this.getClass().getResource( "waterlevelstyle.css" ).toExternalForm() );
        info.setScene( infoScene );
        info.setResizable( false );
        info.show();
    }



}



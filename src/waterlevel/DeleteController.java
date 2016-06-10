package waterlevel;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

/**
 * Class: DeleteController
 * Version: 0.1
 * Created on 10.06.2016 with the help of IntelliJ IDEA (thanks!)
 * Author: Evnica
 * Description:
 */
public class DeleteController
{
    public Button noDelete;
    public Button yesDelete;
    public Label exitConfirm;
    private Main main;
    public GridPane dialogPane;
    private static final Logger LOGGER = LogManager.getLogger(DeleteController.class);

    @FXML
    public void initialize() throws Exception
    {
        yesDelete.defaultButtonProperty().setValue( true );
        yesDelete.setOnAction( event -> {
            yesDelete.getScene().getWindow().hide();
            if ( deleteFiles() )
            {
                try
                {
                    main.showInfoDialog( "Alle Berichte wurden gelöscht" );
                } catch ( Exception e )
                {
                    LOGGER.error( "Can't inform that files are deleted", e );
                }
            }
        } );

        noDelete.setOnAction( event -> noDelete.getScene().getWindow().hide() );
    }

    void setMainApp(Main main) {
        this.main = main;
    }

    private boolean deleteFiles()
    {
        File reportDir = new File( "./reports" ); //"../WaterLevelApp/reports"
        boolean success = false;
        try
        {
            FileUtils.cleanDirectory(reportDir);
            success = true;
        } catch ( IOException e )
        {
            e.printStackTrace();
        }
        return success;
    }


}

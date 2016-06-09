package com.evnica.waterlevelwithgui.logic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class: DataReader
 * Version: 0.2
 * Created on 20.04.2016 with the help of IntelliJ IDEA (thanks!)
 * Author: Evnica
 * Description: DataReader reads measurement data stored in a text file and saves it as a two-dimensional list of Strings
 */
public class DataReader
{

    private static final Logger LOGGER = LogManager.getLogger(DataReader.class);



    public static List<String> readData( File file)
    {
        List<String> data = new ArrayList<>(  );
        String line;
        BufferedReader reader = null;
        try
        {
            reader = new BufferedReader( new InputStreamReader( new FileInputStream( file ), "UTF8" ) );
            while ( (line = reader.readLine()) != null )
            {
                data.add( line );
            }
        }
        catch ( FileNotFoundException e )
        {
            LOGGER.error( "File with measurement data was not found", e );
        }
        catch ( IOException e )
        {
            LOGGER.error( "Problem with reading a file after it was located", e );
        }
        finally
        {
            if (reader != null)
            {
                try
                {
                    reader.close();
                } catch ( IOException e )
                {
                    LOGGER.error( "Can't close the reader", e );
                }
            }
        }
        return data;
    }


    public static List<File> listAllFilesInResources(String pathToResources) throws IOException
    {
        return  Files.walk( Paths.get(pathToResources) ).
                filter( Files::isRegularFile ).
                map( Path::toFile).
                collect( Collectors.toList() );
    }

    public static List<File> listAllFilesInResourcesFromServlet(File folder) throws IOException
    {
        return  Files.walk( folder.toPath() ).
                filter( Files::isRegularFile ).
                map( Path::toFile).
                collect( Collectors.toList() );
    }

}

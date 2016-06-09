package com.evnica.waterlevelwithgui.logic;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class: DataStorage
 * Version: 0.2
 * Created on 20.04.2016 with the help of IntelliJ IDEA (thanks!)
 * Author: Evnica
 * Description: Stores Stations at runtime
 */
public class DataStorage
{
    private static List<Station> stations = new ArrayList<>();

    public static Station findStation ( String stationName)
    {
        Station result;
        String toFind = stationName.toLowerCase();
        List<Station> chosenStations = stations.stream()
                .filter( st -> st.name.toLowerCase().contains( toFind ) )
                .collect( Collectors.toList());
        if (chosenStations.size() > 0) { result = chosenStations.get( 0 ); }
        else { result = null; }
        return result;
    }

    public static void fillTheStorage(String path) throws IOException
    {
        List<File> resources = DataReader.listAllFilesInResources( path );
        List<List<String>> data = new ArrayList<>( resources.size() );
        resources.forEach( source -> data.add( DataReader.readData( source ) ) );
        stations = new ArrayList<>(  );
        data.forEach(st -> stations.add( DataProcessor.convertTextIntoStation( st ) ) );
    }

    public static List<Station> getStations()
    {
        return stations;
    }

    public static void loadInDb()
    {
        if ( DatabaseOperator.connect() )
        {
            stations.forEach(DatabaseOperator::insert);
        }
    }
}

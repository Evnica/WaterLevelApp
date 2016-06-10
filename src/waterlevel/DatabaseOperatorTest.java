package waterlevel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Class: DatabaseOperatorTest
 * Version: 0.1
 * Created on 08.06.2016 with the help of IntelliJ IDEA (thanks!)
 * Author: Evnica
 * Description:
 */
public class DatabaseOperatorTest
{
    @Before
    public void setUp() throws Exception
    {
        assertTrue( DatabaseOperator.connect() );
    }

    @After
    public void tearDown() throws Exception
    {
        assertTrue( DatabaseOperator.closeConnection());
    }

    @Test
    public void insert() throws Exception
    {

        List<File> resources = DataReader.listAllFilesInResources( "../WaterLevelApp/resources" );
        List<List<String>> data = new ArrayList<>( resources.size() );
        for (File source: resources)
        {
            data.add( DataReader.readData( source ) );
        }
        assertEquals( data.size(), 3 );

        Station station = DataProcessor.convertTextIntoStation( data.get( 0 ) );
        assertTrue( DatabaseOperator.insert( station ) );
        assertTrue( DatabaseOperator.insertDayMeasurements( station
                .getMeasurementsWithinInterval
                        ( LocalDate.of( 2015, 2, 20 ), LocalTime.of( 10,0 ),
                                LocalDate.of( 2015, 2, 22 ), LocalTime.of( 10,0 ) ), 1 ) );
        assertTrue( DatabaseOperator.deleteFromTable( station.place ) );
        DatabaseOperator.dropTable( station.place );
    }

}
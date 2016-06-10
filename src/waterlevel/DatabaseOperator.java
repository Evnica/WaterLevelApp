package waterlevel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Class: DatabaseOperator
 * Version: 0.2
 * Created on 16.05.2016 with the help of IntelliJ IDEA (thanks!)
 * Author: Evnica
 * Description: Creates an embedded db (H2) and executes basic operation:
 *              create table, insert data, delete from table, drop table
 */
public class DatabaseOperator
{
    private static Connection connection;
    private static final String DRIVER_CLASS = "org.h2.Driver";
    private static final Logger LOGGER = LogManager.getLogger( DatabaseOperator.class );
    private static List<String> stationsInDb = new ArrayList<>();

    public static boolean connect()
    {
        boolean connected;
        try
        {
            Class.forName( DRIVER_CLASS );
            connection = DriverManager.getConnection( "jdbc:h2:~/stations", "evnica", "getConnection" );
            connected = true;
        }
        catch ( ClassNotFoundException e )
        {
            LOGGER.error( "Can't load driver for the embedded DB, the report will contain a table only", e );
            connected = false;
        }
        catch ( SQLException ex )
        {
            LOGGER.error( "Can't connect to embedded DB, the report will contain a table only", ex );
            connected = false;
        }
        return connected;
    }

    public static boolean insert(Station station)
    {
        boolean success = false;
        Statement statement = null;
        boolean connected = true;
        String timestamp;
        if (connection == null)
        {
            connected = connect();
        }
        if (connected)
        {
            if (!stationsInDb.contains( station.place ))
            {
                provideEmptyTable( station.place );
            }
            if (stationsInDb.contains( station.place ))
            {
                try
                {
                    statement = connection.createStatement();
                        for ( DayMeasurement day : station.measurements )
                        {
                            for ( Measurement pair : day.hourlyMeasurementValues )
                            {
                                //The format is yyyy-MM-dd hh:mm:ss[.nnnnnnnnn] // http://www.h2database.com/html/datatypes.html#timestamp_type
                                timestamp = Formatter.getDATE_FORMATTER_yyyyMMdd().format( day.date ) + " "
                                        + Formatter.getTIME_FORMATTER_HHmmss().format( pair.timestamp );
                                statement.execute( "INSERT INTO " + station.place + " VALUES" +
                                        "('" + timestamp + "', " + pair.value + ")" );
                            }
                        }
                    success = true;
                }
                catch ( SQLException e )
                {
                        LOGGER.error( "Failed to insert data ", e );
                        success = false;
                }
                finally
                {
                    if ( statement != null )
                    {
                        try
                        {
                                statement.close();
                        }
                        catch ( SQLException e )
                        {
                                LOGGER.error( "Insert statement wasn't closed ", e );
                        }
                    }
                }
            }
        }
        return success;
    }

    public static boolean insertDayMeasurements(List<DayMeasurement> dayMeasurements, int step) throws SQLException
    {
        List<MeasurementForDb> pairsForDb = new ArrayList<>(  );

        if (step > 1)
        {
            int starting = 0, current, last;
            String tStamp;
            double value;

            for (DayMeasurement day: dayMeasurements)
            {
                current = starting;
                last = day.getHourlyMeasurementValues().size() - 1;
                while ( current <= last )
                {
                    tStamp = Formatter.getDATE_FORMATTER_yyyyMMdd().format( day.date ) + " "
                            + Formatter.getTIME_FORMATTER_HHmmss().format( day.getHourlyMeasurementValues().get( current ).timestamp );
                    value = day.getHourlyMeasurementValues().get( current ).getValue();
                    pairsForDb.add( new MeasurementForDb( value, tStamp ) );
                    current += step;
                }
                if (current > last)
                {
                    starting = current - last - 1;
                }
                else
                {
                    starting = 0;
                }
            }
        }


        boolean success = false;
        provideEmptyTable( "measurements" );
        Statement statement;
        boolean connected = true; //supposedly, but later we we'll make sure it's true before we execute insert
        String timestamp;

        if (connection == null)
        {
            connected = connect();
        }
        if (connected)
        {
            statement = connection.createStatement();

            if ( step > 1 )
            {
                for (MeasurementForDb measurementForDb: pairsForDb)
                {
                    statement.execute( "INSERT INTO measurements VALUES" +
                            "('" + measurementForDb.time + "', " + measurementForDb.value + ")"  );
                }
            }
            else
            {
                for ( DayMeasurement day : dayMeasurements )
                {
                    for ( Measurement pair : day.hourlyMeasurementValues )
                    {
                        timestamp = Formatter.getDATE_FORMATTER_yyyyMMdd().format( day.date ) + " "
                                + Formatter.getTIME_FORMATTER_HHmmss().format( pair.timestamp );
                        statement.execute( "INSERT INTO measurements VALUES" +
                                "('" + timestamp + "', " + pair.value + ")" );
                    }
                }
                success = true;
            }
            try
            {
                statement.close();
            } catch ( SQLException e )
            {
                LOGGER.error( "Couldn't close statement ", e );
            }
        }
        return success;
    }

    public static boolean deleteFromTable(String tableName) throws Exception
    {
        boolean success = false;
        boolean connected = true;
        if (connection == null)
        {
            connected = connect();
        }
        if (connected)
        {
            if ( stationsInDb.contains( tableName ) )
            {
                PreparedStatement statement = null;
                try
                {
                    statement = connection.prepareStatement("DELETE FROM " + tableName);
                    statement.executeUpdate();
                    success = true;
                }
                catch ( SQLException e )
                {
                    LOGGER.error( "Can't delete from the table " + tableName, e );
                    success = false;
                }
                finally
                {
                    if (statement != null)
                    {
                        try
                        {
                            statement.close();
                        }
                        catch ( SQLException e )
                        {
                            LOGGER.error( "Delete statement didn't get closed ", e );
                        }
                    }
                }
            }
        }
        return success;
    }

    //create or clear
    public static boolean provideEmptyTable( String tableName)
    {
        Statement statement = null;
        boolean success = false;
        boolean connected = true;
        if (connection == null)
        {
            connected = connect();
        }
        if (connected)
        {
            if ( !stationsInDb.contains( tableName ) )
            {
                try
                {
                    statement = connection.createStatement();
                    statement.executeUpdate( "CREATE TABLE " + tableName + " (" +
                            "timestamp TIMESTAMP, " +
                            "value DOUBLE" +
                            ")" );
                    stationsInDb.add( tableName );
                    success = true;
                }
                catch ( SQLException e )
                {
                    LOGGER.info( "Table " + tableName + " already exists" );
                    try
                    {
                        deleteFromTable( tableName );
                        LOGGER.info( "Values deleted from " + tableName + ". Table empty." );
                        stationsInDb.add( tableName );
                        success = true;
                    }
                    catch ( Exception e1)
                    {
                        LOGGER.error( "Values can't be deleted from  " + tableName, e );
                        success = false;
                    }
                }
                finally
                {
                    if (statement != null)
                    {
                        try
                        {
                            statement.close();
                        } catch ( SQLException e )
                        {
                            LOGGER.error( "Create statement wasn't closed ", e );
                        }
                    }
                }
            }
        }
        return success;
    }

    public static void dropTable(String tableName)
    {
        Statement statement = null;
        boolean connected = true;
        if (connection == null)
        {
            connected = connect();
        }
        if (connected)
        {
            try
            {
                statement = connection.createStatement();
                statement.execute( "DROP TABLE " + tableName );
                stationsInDb.remove( tableName );
            }
            catch ( SQLException e )
            {
                LOGGER.error( "Can't drop table " + tableName + " " + e.getMessage() );
            }
            finally
            {
                if (statement != null)
                {
                    try
                    {
                        statement.close();
                    }
                    catch ( SQLException e )
                    {
                        LOGGER.error( "Drop statement didn't get closed ", e );
                    }
                }
            }
        }
    }

    public static boolean closeConnection()
    {
        boolean success = false;
        if (connection != null)
        {
            try
            {
                connection.close();
                success = true;
            } catch ( SQLException e )
            {
                LOGGER.error( "Connection couldn't be closed", e );
            }
        }
        return success;
    }

    public static Connection getConnection()
    {
        return connection;
    }


}


package com.evnica.waterlevelwithgui.logic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.format.DateTimeFormatter;
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
                createTable( station.place );
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
                                timestamp = DateTimeFormatter.ofPattern( "yyyy-MM-dd" ).format( day.date ) + " "
                                        + DateTimeFormatter.ofPattern( "hh:mm:ss" ).format( pair.timestamp );
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

    public static boolean deleteFromTable(String station) throws Exception
    {
        boolean success = false;
        boolean connected = true;
        if (connection == null)
        {
            connected = connect();
        }
        if (connected)
        {
            if ( stationsInDb.contains( station ) )
            {
                PreparedStatement statement = null;
                try
                {
                    statement = connection.prepareStatement("DELETE FROM " + station);
                    statement.executeUpdate();
                    success = true;
                }
                catch ( SQLException e )
                {
                    LOGGER.error( "Can't delete from the table " + station, e );
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

    public static boolean createTable(String station)
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
            if ( !stationsInDb.contains( station ) )
            {
                try
                {
                    statement = connection.createStatement();
                    statement.executeUpdate( "CREATE TABLE " + station + " (" +
                            "timestamp TIMESTAMP," +
                            "value DOUBLE" +
                            ")" );
                    stationsInDb.add( station );
                    success = true;
                }
                catch ( SQLException e )
                {
                    LOGGER.info( "Table " + station + " already exists" );
                    try
                    {
                        deleteFromTable( station );
                        LOGGER.info( "Values deleted from " + station + ". Table empty." );
                        success = true;
                    }
                    catch ( Exception e1)
                    {
                        LOGGER.error( "Values can't be deleted from  " + station, e );
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

    public static boolean dropTable(String station)
    {
        Statement statement = null;
        boolean connected = true;
        boolean success = false;
        if (connection == null)
        {
            connected = connect();
        }
        if (connected)
        {
            if ( stationsInDb.contains( station ) )
            {
                try
                {
                    statement = connection.createStatement();
                    statement.execute( "DROP TABLE " + station );
                    stationsInDb.remove( station );
                    success = true;
                }
                catch ( SQLException e )
                {
                    LOGGER.error( "Can't drop table " + station, e );
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
                            LOGGER.error( "Drop statement didn't get closed ", e );
                        }
                    }
                }
            }
        }
        return success;
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


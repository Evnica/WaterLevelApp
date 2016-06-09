package com.evnica.waterlevelwithgui.logic;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class: ReportParameters
 * Version: 0.1
 * Created on 08.06.2016 with the help of IntelliJ IDEA (thanks!)
 * Author: Evnica
 * Description:
 */
public class ReportParameters
{
    private Map<String, Object> parameters = new HashMap<>(  );
    private String tableName, stationName, dateFrom, dateTo, availableTo, availableFrom;
    private List<DayMeasurement> measurements;

    public ReportParameters()
    {
    }

    public void setTableName( String tableName )
    {
        this.tableName = tableName;
    }

    public String getTableName()
    {
        return tableName;
    }

    public void setStationName( String stationName )
    {
        this.stationName = stationName;
        parameters.put( "ReportTitle", stationName );
    }

    public void setDateFrom( String dateFrom )
    {
        this.dateFrom = dateFrom;
        parameters.put( "DateFrom", dateFrom );
    }

    public void setDateTo( String dateTo )
    {
        this.dateTo = dateTo;
        parameters.put( "DateTo", dateTo );
    }

    public void setMeasurements( List<DayMeasurement> measurements )
    {
        this.measurements = measurements;
        availableFrom = Formatter.getDATE_TIME_FORMATTER_ddMMyyyy_HHmm().format(
                LocalDateTime.of(
                        measurements.get( 0 ).getDate(),
                        measurements.get( 0 ).getHourlyMeasurementValues().get( 0 ).getTimestamp() )
        );
        int numOfEntries = measurements.size();
        availableTo = Formatter.getDATE_TIME_FORMATTER_ddMMyyyy_HHmm().format(
                LocalDateTime.of(
                        measurements.get( numOfEntries - 1 ).getDate(),
                        measurements.get( numOfEntries - 1 ).getHourlyMeasurementValues()
                        .get( measurements.get( numOfEntries - 1 ).getHourlyMeasurementValues().size() - 1 ).getTimestamp()
                )
        );
        parameters.put( "AvailableTo", availableTo );
        parameters.put( "AvailableFrom", availableFrom );


    }


    public List<DayMeasurement> getMeasurements()
    {
        return measurements;
    }

    public Map<String, Object> getParameters()
    {
        return parameters;
    }



    public void putQueryParameter( int i )
    {
        String sqlQuery;
        if ( i != 1 )
        {
            sqlQuery = "SELECT table.timestamp, table.value FROM " +
                    "(SELECT timestamp, value, ROW_NUMBER() OVER (ORDER BY timestamp) AS row " +
                    "FROM "+ tableName +
                    ") as table " +
                    "WHERE table.row %" + i + "= 0 " +
                    "ORDER BY table.timestamp";
        }
        else
        {
            sqlQuery = "SELECT * FROM " + tableName;
        }
        parameters.put( "query", sqlQuery );


    }


}

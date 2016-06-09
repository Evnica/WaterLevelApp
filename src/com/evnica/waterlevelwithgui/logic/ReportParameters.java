package com.evnica.waterlevelwithgui.logic;

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
    Map<String, Object> parameters = new HashMap<>(  );
    String stationName, dateFrom, dateTo;
    List<DayMeasurement> measurements;

    public void setStationName( String stationName )
    {
        this.stationName = stationName;
        parameters.put( "stationName", stationName );
    }

    public void setDateFrom( String dateFrom )
    {
        this.dateFrom = dateFrom;
        parameters.put( "dateFrom", dateFrom );
    }

    public void setDateTo( String dateTo )
    {
        this.dateTo = dateTo;
        parameters.put( "dateTo", dateTo );
    }

    public void setMeasurements( List<DayMeasurement> measurements )
    {
        this.measurements = measurements;
    }


}

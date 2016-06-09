package com.evnica.waterlevelwithgui.logic;

import java.time.LocalTime;

/**
 * Class: Measurement
 * Version: 0.1
 * Created on 20.04.2016 with the help of IntelliJ IDEA (thanks!)
 * Author: Evnica
 * Description: Contains time-value pair of water level measurement. Unit is static.
 */
public class Measurement implements Comparable<Measurement>
{
    Double value;
    LocalTime timestamp;
    private static final String UNIT = "cm";

    public Measurement( LocalTime timestamp, Double value )
    {
        this.value = value;
        this.timestamp = timestamp;
    }

    public Double getValue()
    {
        return value;
    }

    public LocalTime getTimestamp()
    {
        return timestamp;
    }

    @Override
    public int compareTo( Measurement anotherMeasurement )
    {
        int result;

        if (this.timestamp.equals(anotherMeasurement.timestamp))
        {
            result = 0;
        }
        else if (this.timestamp.isBefore( anotherMeasurement.timestamp ))
        {
            result = -1;
        }
        else  // this is after anotherMeasurement
        {
            result = 1;
        }
        return result;
    }

    @Override
    public String toString()
    {
        String result;
        if (value != null)
        {
            result = Formatter.TIME_FORMATTER.format( timestamp ) + "#" + Formatter.NUMBER_FORMAT.format( value );
        }
        else
        {
            result = Formatter.TIME_FORMATTER.format( timestamp ) + "#" + value;
        }
        return result;
    }
}


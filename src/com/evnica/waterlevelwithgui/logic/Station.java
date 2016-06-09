package com.evnica.waterlevelwithgui.logic;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class: Station
 * Version: 0.2
 * Created on 20.04.2016 with the help of IntelliJ IDEA (thanks!)
 * Author: Evnica
 * Description: Contains station data: name, body of water, a list of daily measurements used to produce reports
 */
public class Station
{
    String name, bodyOfWater;
    public String place;
    public List<DayMeasurement> measurements = new ArrayList<>(  );

    public Station(){}

    public String getName()
    {
        return name;
    }

    public String getBodyOfWater()
    {
        return bodyOfWater;
    }

    public Station( String name, String place, String bodyOfWater )
    {
        this.name = name;
        this.bodyOfWater = bodyOfWater;
        this.place = place;
    }


    public List<DayMeasurement> getMeasurementsWithinInterval
            ( LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime)
    {
        List<DayMeasurement> chosenDays;

        if (startDate.isBefore( endDate ) || startDate.equals( endDate ))
        {

            chosenDays = measurements.stream()
                    .filter( day -> ((day.date.isAfter( startDate ) && day.date.isBefore( endDate )))
                            || day.date.equals( startDate ) || day.date.equals( endDate ))
                    .collect( Collectors.toList());

            if (chosenDays.size() > 0)
            {   // replace the measurements of the first day only with values after the start time
                chosenDays.get( 0 ).hourlyMeasurementValues = chosenDays.get( 0 ).hourlyMeasurementValues.stream()
                        .filter( valuePair -> (valuePair.getTimestamp().isAfter( startTime ) || valuePair.getTimestamp().equals( startTime )) )
                        .collect( Collectors.toList());
                chosenDays.get( chosenDays.size() - 1 ).hourlyMeasurementValues =
                        chosenDays.get( chosenDays.size() - 1 ).hourlyMeasurementValues.stream()
                                .filter( valuePair -> (valuePair.getTimestamp().isBefore( endTime ) ||
                                        valuePair.getTimestamp().equals( endTime ))  )
                                .collect( Collectors.toList());
            }
        }
        else
        {
            chosenDays = new ArrayList<>(  );
        }

        return chosenDays;
    }


    @Override
    public String toString()
    {
        String pairs = "";
        for (DayMeasurement measurement: measurements)
        {
            pairs = pairs + "\n\n" + measurement;
        }
        return "Messstation: " + name + " " + place + " " + " [" + bodyOfWater + "]:" + pairs;
    }

    public String fullName()
    {
       return name + " " + place + " " + bodyOfWater;
    }
}


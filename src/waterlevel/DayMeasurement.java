package waterlevel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Class: DayMeasurement
 * Version: 0.1
 * Created on 20.04.2016 with the help of IntelliJ IDEA (thanks!)
 * Author: Evnica
 * Description: Contains date and a list of time-value pairs of hourly water level measurements
 */

public class DayMeasurement implements Comparable<DayMeasurement>
{
    LocalDate date;
    List<Measurement> hourlyMeasurementValues = new ArrayList<>();


    DayMeasurement( LocalDate date, List<Measurement> hourlyMeasurementValues )
    {
        this.date = date;
        this.hourlyMeasurementValues = hourlyMeasurementValues;
    }

    List<Measurement> getHourlyMeasurementValues()
    {
        return hourlyMeasurementValues;
    }

    LocalDate getDate()
    {
        return date;
    }

    @Override
    public int compareTo( DayMeasurement anotherDayMeasurement )
    {
        int result;

        if ( this.date.isBefore( anotherDayMeasurement.date ) ) { result = -1; }
        else if ( this.date.isAfter( anotherDayMeasurement.date ) ) { result = 1; }
        else { result = 0; }

        return result;
    }

    @Override
    public String toString()
    {
        String hourlyValues = "";
        for ( Measurement m: hourlyMeasurementValues )
        {
            hourlyValues = hourlyValues + "\n" + m;
        }

        return Formatter.DATE_FORMATTER_ddMMyyyy.format( date ) + hourlyValues;
    }
}


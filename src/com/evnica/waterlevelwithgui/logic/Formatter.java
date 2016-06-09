package com.evnica.waterlevelwithgui.logic;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;

/**
 * Class: Formatter
 * Version: 0.1
 * Created on 20.04.2016 with the help of IntelliJ IDEA (thanks!)
 * Author: Evnica
 * Description: Enlists all formatters used by the app
 */
public class Formatter
{
    static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern( "dd.MM.yyyy" );
    static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern( "HH:mm" );
    static final DateTimeFormatter WEB_FORMATTER = DateTimeFormatter.ofPattern( "yyyy-MM-dd'T'HH:mm:ss'Z'" );
    static final NumberFormat NUMBER_FORMAT = new DecimalFormat( "#0.00" );

    public static DateTimeFormatter getWebDateFormatter()
    {
        return WEB_FORMATTER;
    }

    public static DateTimeFormatter getDateFormatter()
    {
        return DATE_FORMATTER;
    }

    public static DateTimeFormatter getTimeFormatter()
    {
        return TIME_FORMATTER;
    }

    public static NumberFormat getNumberFormat()
    {
        return NUMBER_FORMAT;
    }
}


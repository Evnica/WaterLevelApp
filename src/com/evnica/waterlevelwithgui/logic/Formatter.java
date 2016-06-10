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
    static final DateTimeFormatter DATE_FORMATTER_ddMMyyyy = DateTimeFormatter.ofPattern( "dd.MM.yyyy" );
    static final DateTimeFormatter TIME_FORMATTER_HHmm = DateTimeFormatter.ofPattern( "HH:mm" );
    private static final DateTimeFormatter DATE_FORMATTER_yyyyMMdd = DateTimeFormatter.ofPattern( "yyyy-MM-dd" );
    private static final DateTimeFormatter TIME_FORMATTER_HHmmss = DateTimeFormatter.ofPattern( "HH:mm:ss" );
    private static final DateTimeFormatter DATE_TIME_FORMATTER_ddMMyyyy_HHmm = DateTimeFormatter.ofPattern( "dd.MM.yyyy HH:mm" );
    private static final DateTimeFormatter DATE_TIME_FORMATTER_HHmmss_ddMMyyyy = DateTimeFormatter.ofPattern( "HHmmss-ddMMyyyy" );


    static final NumberFormat NUMBER_FORMAT = new DecimalFormat( "#0.00" );


    public static DateTimeFormatter getDATE_FORMATTER_ddMMyyyy()
    {
        return DATE_FORMATTER_ddMMyyyy;
    }

    public static DateTimeFormatter getTIME_FORMATTER_HHmm()
    {
        return TIME_FORMATTER_HHmm;
    }

    static DateTimeFormatter getDATE_FORMATTER_yyyyMMdd()
    {
        return DATE_FORMATTER_yyyyMMdd;
    }

    static DateTimeFormatter getTIME_FORMATTER_HHmmss()
    {
        return TIME_FORMATTER_HHmmss;
    }

    public static DateTimeFormatter getDATE_TIME_FORMATTER_ddMMyyyy_HHmm()
    {
        return DATE_TIME_FORMATTER_ddMMyyyy_HHmm;
    }

    public static DateTimeFormatter getDATE_TIME_FORMATTER_HHmmss_ddMMyyyy()
    {
        return DATE_TIME_FORMATTER_HHmmss_ddMMyyyy;
    }
}


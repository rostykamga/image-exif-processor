package org.rostykamga.imageexifprocessor.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Parser {

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");

    /**
     * Parses the given string as integer, and return it. If not possible to parse to int, then
     * return the default value.
     *
     * @param str the string to parse
     * @param def the default value to return if string is not processable
     *
     * @return either integer representation of string, or default value.
     */
    public static int parseToIntOrDefault(String str, int def){

        try{
            return Integer.parseInt(str);
        }
        catch (Exception ex){
            return def;
        }
    }

    /**
     * Parses the given string as long, and return it. If not possible to parse to long, then
     * return the default value.
     *
     * @param str the string to parse
     * @param def the default value to return if string is not processable
     *
     * @return either long representation of string, or default value.
     */
    public static long parseToLongOrDefault(String str, long def){

        try{
            return Long.parseLong(str);
        }
        catch (Exception ex){
            return def;
        }

    }

    /**
     * Parses the given string as double, and return it. If not possible to parse to double, then
     * return the default value.
     *
     * @param str the string to parse
     * @param def the default value to return if string is not processable
     *
     * @return either double representation of string, or default value.
     */
    public static double parseToDoubleOrDefault(String str, double def){

        try{
            return Double.parseDouble(str);
        }
        catch (Exception ex){
            return def;
        }
    }

    /**
     * Parses the given string as double, and return it. If not possible to parse to double, then
     * return the default value.
     *
     * @param str the string to parse
     * @param def the default value to return if string is not processable
     *
     * @return either double representation of string, or default value.
     */
    public static Date parseToDateOrDefault(String str, Date def){

        try{
            return DATE_FORMAT.parse(str);
        }
        catch (Exception ex){
            return def;
        }
    }
}

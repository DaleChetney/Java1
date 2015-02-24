package com.example.dchetney.helloworld;

import junit.framework.TestCase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dchetney on 2/24/2015.
 */
public class DateParserTest extends TestCase {
    public void testParse(){
        DateParser parser = new DateParser();
        try {
            parser.parse("word",DateParser.START_DATE);
        }
        catch (ParseException e){
            assertTrue(e.getMessage().contains("Invalid date format."));
        }
        try {
            parser.parse("30 15 15",DateParser.START_DATE);
        }
        catch (ParseException e){
            assertTrue(e.getMessage().contains("That's not a real month."));
        }
        try {
            parser.parse("2 30 15", DateParser.END_DATE);
        }
        catch (ParseException e){
            assertTrue(e.getMessage().contains("That's not a real day."));
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM dd yy");
            assertEquals(parser.parse("01.23.2015",DateParser.END_DATE),sdf.parse("01 23 15"));
            assertEquals(parser.parse("1/23/2015",DateParser.END_DATE), sdf.parse("01 23 15"));
            assertEquals(parser.parse("01-23-2015",DateParser.END_DATE), sdf.parse("01 23 15"));
            assertEquals(parser.parse("1-2015",DateParser.END_DATE), sdf.parse("01 01 15"));
            assertEquals(parser.parse("Jan 23 2015",DateParser.END_DATE),sdf.parse("01 23 15"));
            assertEquals(parser.parse("Jan 23, 2015",DateParser.END_DATE), sdf.parse("01 23 15"));
            assertEquals(parser.parse("January 23, 2015",DateParser.END_DATE), sdf.parse("01 23 15"));
            assertEquals(parser.parse("January 23 2015", DateParser.END_DATE), sdf.parse("01 23 15"));
            assertEquals(parser.parse("",DateParser.START_DATE), new Date());
            assertEquals(parser.parse("",DateParser.END_DATE), new Date(System.currentTimeMillis()+(1000*60*60*24*7)));
        }
        catch (ParseException e){
            fail();
        }
    }
    public void testFormat(){
        DateParser parser = new DateParser();
        SimpleDateFormat sdf = new SimpleDateFormat("MM dd yy");
        Date date = null;
        try {
            date = sdf.parse("01 23 15");
        }
        catch (ParseException e){
            fail();
        }
        assertEquals(parser.format(date),"Bidding opens on Jan 23, 2015");
        assertEquals(parser.formatTimeUntil(new Date(System.currentTimeMillis()+(1000*60*60*24*7))),"Bidding closes in 7 days, ");
    }
}

package com.example.dchetney.helloworld;

import junit.framework.TestCase;

import java.text.ParseException;

/**
 * Created by dchetney on 2/24/2015.
 */
public class MoneyParserTest extends TestCase {

    public void testParse() {
        MoneyParser parser = new MoneyParser();
        try {
            parser.parse("word");
        }
        catch (ParseException e){
            assertTrue(e.getMessage().contains("No numbers in your price."));
        }
        try {
            parser.parse("-1");
        }
        catch (ParseException e){
            assertTrue(e.getMessage().contains("No negatives allowed."));
        }
        try {
            assertEquals(parser.parse(""), 0.01f);
            assertEquals(parser.parse("1"), 1.0f);
            assertEquals(parser.parse("1.00"), 1.0f);
            assertEquals(parser.parse("$1.00"), 1.0f);
            assertEquals(parser.parse("$1"), 1.0f);
            assertEquals(parser.parse("$1,000"), 1.0f);
            assertEquals(parser.parse("$1,00"), 1.0f);
            assertEquals(parser.parse("1.0"), 1.0f);
        }
        catch (ParseException e){
            fail();
        }
    }

    public void testFormat() {
        MoneyParser parser = new MoneyParser();
        assertEquals(parser.format(1),"$1.00");
    }
}

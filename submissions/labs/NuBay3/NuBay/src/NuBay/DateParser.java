package NuBay;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DateParser {
    public static final int START_DATE = 0;
    public static final int END_DATE = 1;
    public static final Pattern numberPat = Pattern.compile("\\d\\d?[\\s\\.\\-/]\\d\\d?[\\s\\.\\-/]\\d{2}(\\d{2})?");
    public static final Pattern missingDay = Pattern.compile("\\d\\d?[\\s\\.\\-/]\\d{2}(\\d{2})?");
    public static final Pattern wordPat = Pattern.compile("(Jan(uary)?|Feb(ruary)?|Mar(ch)?|Apr(il)?|May|Jun(e)?|Jul(y)?|Aug(ust)?|Sept(ember)?|Oct(ober)?|(Nov|Dec)(ember)?)\\s\\d\\d?\\,?\\s\\d{4}");

    public Date parse(String toParse,int id)throws ParseException{
        if(toParse.isEmpty()){
            if(id == START_DATE){
                return new Date();
            }
            else{
                return new Date(System.currentTimeMillis()+(1000*60*60*24*7));
            }
        }
        Matcher numMatcher = numberPat.matcher(toParse);
        Matcher missedDay = missingDay.matcher(toParse);
        Matcher wordMatcher = wordPat.matcher(toParse);
        SimpleDateFormat sdf;
        Pattern num = Pattern.compile("\\d\\d?");
        Matcher na = num.matcher(toParse);
        if(numMatcher.find()){
            if(na.find()){
                String month = na.group();
                int monthnum = Integer.parseInt(month);
                if(monthnum <= 12 && monthnum >= 1){
                    if(na.find()) {
                        String day = na.group();
                        int daynum = Integer.parseInt(day);
                        if(monthnum == 2){
                            if(daynum < 1 || daynum > 28) throw new ParseException("That's not a real day.",0);
                        }
                        if(monthnum == 4||monthnum == 6||monthnum==9||monthnum==11){
                            if(daynum < 1 || daynum > 30) throw new ParseException("That's not a real day.",0);
                        }
                        else{
                            if(daynum < 1 || daynum > 31) throw new ParseException("That's not a real day.",0);
                        }
                    }
                }
                else throw new ParseException("That's not a real month.",0);
            }
            Pattern symbol = Pattern.compile("[/\\s\\.\\-]");
            Matcher ma = symbol.matcher(toParse);
            String first = null;
            String second = null;
            if(ma.find())
                first = ma.group();
            if(ma.find())
                second = ma.group();
            if(toParse.substring(toParse.length()-4).contains(second)) sdf = new SimpleDateFormat(String.format("M%sd%syy",first,second));
            else sdf = new SimpleDateFormat(String.format("M%sd%syyyy",first,second));

            Date result = sdf.parse(toParse);
            return result;
        }
        else if(wordMatcher.find()){
            if(na.find()){
                String month = toParse.substring(0,3);
                String day = na.group();
                int daynum = Integer.parseInt(day);
                if(month.equals("Feb")){
                    if(daynum < 1 || daynum > 28) throw new ParseException("That's not a real day.",0);
                }
                if(month.equals("Apr")||month.equals("Jun")||month.equals("Sep")||month.equals("Nov")){
                    if(daynum < 1 || daynum > 30) throw new ParseException("That's not a real day.",0);
                }
                else{
                    if(daynum < 1 || daynum > 31) throw new ParseException("That's not a real day.",0);
                }

            }
            if(toParse.substring(0,4).contains(" ")) {
                if (toParse.contains(",")) sdf = new SimpleDateFormat("MMM d, yyyy");
                else sdf = new SimpleDateFormat("MMM d yyyy");
            }
            else{
                if(toParse.contains(","))sdf = new SimpleDateFormat("MMMM d, yyyy");
                else sdf = new SimpleDateFormat("MMMM d yyyy");
            }
            Date result = sdf.parse(toParse);
            return result;
        }
        else if(missedDay.find()){
            if(na.find()){
                String month = na.group();
                int monthnum = Integer.parseInt(month);
                if(monthnum > 12 || monthnum < 1) throw new ParseException("That's not a real month.",0);
            }
            Pattern symbol = Pattern.compile("[/\\s\\.\\-]");
            Matcher ma = symbol.matcher(toParse);
            String first = null;
            if(ma.find())
                first = ma.group();
            if(toParse.substring(toParse.length()-4).contains(first)) sdf = new SimpleDateFormat(String.format("M%syy",first));
            else sdf = new SimpleDateFormat(String.format("M%syyyy",first));
            Date result = sdf.parse(toParse);
            return result;
        }
        else throw new ParseException("Invalid date format.",0);
    }

    public String format(Date toFormat){
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy");
        String expireString = sdf.format(toFormat);
        StringBuilder bob = new StringBuilder();
        bob.append("Bidding opens on ");
        bob.append(expireString);
        return bob.toString();
    }

    public String formatTimeUntil(Date until){
        long totaltime = until.getTime() - System.currentTimeMillis();
        long totalseconds = totaltime/1000;
        long totalMinutes = totalseconds/60;
        long totalHours = totalMinutes/60;
        long days = totalHours/24;
        int hours = (int)(totalHours-days*24);
        int minutes = (int)(totalMinutes - totalHours*60);
        int seconds = (int)(totalseconds - totalMinutes*60);
        StringBuilder bob = new StringBuilder();
        bob.append("Bidding closes in ");
        if(days>0) {
            bob.append(days);
            bob.append(" days, ");
        }
        if(hours>0) {
            bob.append(hours);
            bob.append(" hours, ");
        }
        if(minutes>0) {
            bob.append(minutes);
            bob.append(" minutes, ");
        }
        if(seconds>0) {
            bob.append(seconds);
            bob.append(" seconds.");
        }
        return bob.toString();
    }
    
    public String fileFormat(Date toFormat){
        SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy");
        return sdf.format(toFormat);
    }
}
package NuBay;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MoneyParser {
    public static final Pattern pattern = Pattern.compile("(\\d*\\.)?\\d+");

    public float parse(String toParse) throws ParseException{
        if(toParse.isEmpty()) return 0.01f;
        Matcher mitch = pattern.matcher(toParse);
        StringBuilder formatted = new StringBuilder();
        if(mitch.find()){
            formatted.append(mitch.group());
        }
        else throw  new ParseException("No numbers in your price.",0);
        BigDecimal deci = new BigDecimal(formatted.toString());
        float result = deci.floatValue();
        if(result < 0) throw new ParseException("No negatives allowed.",0);
        return result;
    }

    public String format(float toFormat){
        NumberFormat form = NumberFormat.getCurrencyInstance();
        return form.format(toFormat);
    }
    public String fileFormat(float toFormat){
        return String.valueOf(toFormat);
    }
}
package trung.motelmobileapp.MyTools;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateConverter {
    public static String toISO8601UTC(Date date) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(tz);
        return df.format(date);
    }

    public static Date fromISO8601UTC(String dateStr) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(tz);
        try {
            return df.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //format to displaying date from ISO mongodb date
    public static String formattedDate(String dateStr){
        SimpleDateFormat format = new SimpleDateFormat("EEE dd-MMM-yyyy HH:mm:ss");
        try {
            return format.format(fromISO8601UTC(dateStr));
        } catch (Exception e){
            e.printStackTrace();
            return e.toString();
        }
    }
}

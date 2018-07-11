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
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        try {
            return format.format(fromISO8601UTC(dateStr));
        } catch (Exception e){
            e.printStackTrace();
            return e.toString();
        }
    }

    public static String getPassedTime(String dateStr){
        try {
            Date date = fromISO8601UTC(dateStr);
            long timeUnit;
            long passedTime = (System.currentTimeMillis() - date.getTime()) / 1000;
            if (passedTime < 60){
                return "Cách đây không lâu";
            }
            if (passedTime < 3600){
                timeUnit = passedTime / 60;
                return "Khoảng " + timeUnit + " phút trước";
            }
            if (passedTime < 86400){
                timeUnit = passedTime / 3600;
                return "Khoảng " + timeUnit + " giờ trước";
            }
            if (passedTime < 604800){
                timeUnit = passedTime / 86400;
                return "Khoảng " + timeUnit + " ngày trước";
            }
            if (passedTime < 2419200){
                timeUnit = passedTime / 604800;
                return "Khoảng " + timeUnit + " tuần trước";
            }
            if (passedTime < 29030400){
                timeUnit = passedTime / 2419200;
                return "Khoảng " + timeUnit + " tháng trước";
            }
            return "Cách đây rất lâu";
        } catch (Exception e){
            e.printStackTrace();
            return "Không thể xác định";
        }
    }
}

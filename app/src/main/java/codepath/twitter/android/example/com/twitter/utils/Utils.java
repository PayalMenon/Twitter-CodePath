package codepath.twitter.android.example.com.twitter.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by payal.menon on 10/5/17.
 */

public class Utils {

    private static SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");

    public static String getDateString(String dateString) {

        if ("now".equals(dateString)) {
            return "now";
        }
        String displayDate = null;
        try {
            Date parsedDate = (Date) sdf.parse(dateString);
            Date today = new Date();

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
            SimpleDateFormat dayFormat = new SimpleDateFormat("ddMMM");
            SimpleDateFormat hourFormat = new SimpleDateFormat("HH");
            SimpleDateFormat minFormat = new SimpleDateFormat("mm");

            displayDate = dayFormat.format(parsedDate);

            int inputDate = Integer.parseInt(dateFormat.format(parsedDate));
            int currentDate = Integer.parseInt(dateFormat.format(today));

            if (inputDate == currentDate) {
                int inputHour = Integer.parseInt(hourFormat.format(parsedDate));
                int currentHour = Integer.parseInt(hourFormat.format(today));
                if (inputHour == currentHour) {
                    displayDate = minFormat.format(parsedDate) + "m";
                } else {
                    displayDate = hourFormat.format(parsedDate) + "h";
                }
            } else {
                displayDate = dayFormat.format(parsedDate);
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }

        return displayDate;
    }
}

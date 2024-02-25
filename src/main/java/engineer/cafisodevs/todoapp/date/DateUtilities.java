package engineer.cafisodevs.todoapp.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;

public class DateUtilities {

    public static final String DATE_TIME_PATTERN = "MM/dd/yyyy HH:mm:ss";

    public Date parseFromString(String date) {
        DateFormat df = new SimpleDateFormat(DateUtilities.DATE_TIME_PATTERN);
        try {
            return df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String todayAsString() {
        DateFormat df = new SimpleDateFormat(DateUtilities.DATE_TIME_PATTERN);
        return df.format(new Date());
    }

}

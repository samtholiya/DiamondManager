package diamond.com.comp.sam.diamondmanager.library;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by shubh on 24-02-2017.
 */

public class DateLibrary {
    public static Date getDate(int day, int month, int year){
        Date date;
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DATE, day);
        cal.set(Calendar.YEAR, year);
        return cal.getTime();
    }

    public static String getStringDate(int day,int month,int year){
        return day + "-" + (month+1) + "-" + year;
    }


}

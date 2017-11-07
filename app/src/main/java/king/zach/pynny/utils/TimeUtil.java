package king.zach.pynny.utils;

import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import king.zach.pynny.R;

/**
 * Created by zachking on 11/6/17.
 */

public class TimeUtil {

    private static TimeUtil instance;

    public static TimeUtil getInstance() {
        if (instance == null) {
            instance = new TimeUtil();
        }

        return instance;
    }

    public TimeUtil() {

    }

    public static String getCurrentTimeString() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date());
    }

    public static String getTimeStringFromDatePicker(DatePicker dp) {
        int createdMonth = dp.getMonth();
        int createdDay = dp.getDayOfMonth();
        int createdYear = dp.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(createdYear, createdMonth, createdDay);
        String createdAt = calendar.getTime().toString();

        return createdAt;
    }

}

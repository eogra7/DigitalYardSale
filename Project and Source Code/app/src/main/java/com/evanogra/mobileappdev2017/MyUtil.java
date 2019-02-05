package com.evanogra.mobileappdev2017;

import android.text.format.DateUtils;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by Evan on 2/28/2017.
 */

public class MyUtil {
    public static String getRelativeTime(long oldTime) throws ParseException {
        long now = System.currentTimeMillis();
        Date convertedDate = new Date(oldTime);

        return (String) DateUtils.getRelativeTimeSpanString(
                convertedDate.getTime(),
                now,
                DateUtils.SECOND_IN_MILLIS);
    }
}

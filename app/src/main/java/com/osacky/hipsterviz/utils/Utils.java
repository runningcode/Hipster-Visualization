package com.osacky.hipsterviz.utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.ReadableInstant;

public class Utils {

    public static DateTime roundDays(ReadableInstant dt) {
        long millisPerDay = DateTimeConstants.MILLIS_PER_DAY;

        long t = dt.getMillis() / millisPerDay * millisPerDay;
        // Keep TimeZone and round floor to a day
        return new DateTime(t, dt.getZone()).dayOfMonth().roundFloorCopy();
    }

}

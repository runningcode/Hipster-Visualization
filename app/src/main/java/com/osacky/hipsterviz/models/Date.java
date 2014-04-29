package com.osacky.hipsterviz.models;

import org.joda.time.DateTime;

public class Date {
    public static final long MILLIS_PER_SEC = 1000L;
    String uts;
    long time;

    public DateTime getDate() {
        if (time == 0L) {
            time = Long.parseLong(uts) * MILLIS_PER_SEC;
        }
        return new DateTime(Long.parseLong(uts) * MILLIS_PER_SEC);
    }
}

package com.osacky.hipsterviz.models;

import org.joda.time.DateTime;

public class Date {
    String uts;
    long time;

    public DateTime getDate() {
        if (time == 0L) {
            time = Long.parseLong(uts) * 1000L;
        }
        return new DateTime(Long.parseLong(uts) *1000L);
    }
}

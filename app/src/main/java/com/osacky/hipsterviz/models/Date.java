package com.osacky.hipsterviz.models;

import org.joda.time.DateTime;

public class Date {
    String uts;

    public DateTime getDate() {
        return new DateTime(Long.parseLong(uts) * 1000L);
    }

    public Long getLongUTS() {
        return Long.parseLong(uts);
    }
}

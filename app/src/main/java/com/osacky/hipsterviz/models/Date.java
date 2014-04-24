package com.osacky.hipsterviz.models;

import org.joda.time.DateTime;

public class Date {
    String uts;

    public DateTime getDate() {
        return new DateTime(uts);
    }

    public Long getLongUTS() {
        return Long.parseLong(uts);
    }
}

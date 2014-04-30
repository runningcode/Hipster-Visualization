package com.osacky.hipsterviz.utils;

import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.ReadableInstant;

public class Utils {

    public static SpringSystem springSystem = SpringSystem.create();

    public static DateTime roundDays(ReadableInstant dt) {
        long millisPerDay = DateTimeConstants.MILLIS_PER_DAY;

        long t = dt.getMillis() / millisPerDay * millisPerDay;
        // Keep TimeZone and round floor to a day
        return new DateTime(t, dt.getZone()).dayOfMonth().roundFloorCopy();
    }

    public static boolean isMbid(String artist) {
        return artist.matches("^([0-9a-f]*-[0-9a-f]*){4}$");
    }

    public static final SpringConfig ORIGAMI_SPRING_CONFIG = SpringConfig
            .fromOrigamiTensionAndFriction(40, 5);
    public static final SpringConfig BUTTON_SPRING_CONFIG = SpringConfig
            .fromOrigamiTensionAndFriction(140, 8);

}

package com.timhaasdyk.mathbattle.ui.util;

import android.os.Handler;

/**
 * @author Tim Haasdyk on 19-May-17.
 */
public class DelayUtil {

    private static Handler handler = new Handler();

    public static void delay(Runnable r, long delayMillis) {
        handler.postDelayed(r, delayMillis);
    }
}
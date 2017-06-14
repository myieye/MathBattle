package com.timhaasdyk.mathbattle.util;

import android.content.res.Resources;

/**
 * @author Tim Haasdyk on 12-May-17.
 */
public class ResourceUtil {

    private static Resources resources;

    public static void init(Resources resources) {
        ResourceUtil.resources = resources;
    }

    public static String str(int id) {
        return resources.getString(id);
    }

    public static String[] strArr(int id) {
        return resources.getStringArray(id);
    }

    public static float dim(int id) {
        return resources.getDimension(id);
    }

    public static int col(int id) {
        return resources.getColor(id);
    }

    public String[] arrs(int id) {
        return resources.getStringArray(id);
    }
}
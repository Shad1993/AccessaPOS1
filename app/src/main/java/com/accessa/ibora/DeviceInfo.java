package com.accessa.ibora;
import android.os.Build;

public class DeviceInfo {
    public static String getAndroidVersion() {
        int sdkInt = Build.VERSION.SDK_INT;
        String release = Build.VERSION.RELEASE;
        String codename = getCodename(sdkInt);

        return "Android " + release + " (API Level " + sdkInt + ")" + (codename.isEmpty() ? "" : " - " + codename);
    }

    private static String getCodename(int sdkInt) {
        switch (sdkInt) {
            case Build.VERSION_CODES.BASE: return "Base";
            case Build.VERSION_CODES.BASE_1_1: return "Base 1.1";
            case Build.VERSION_CODES.CUPCAKE: return "Cupcake";
            case Build.VERSION_CODES.DONUT: return "Donut";
            case Build.VERSION_CODES.ECLAIR: return "Eclair";
            case Build.VERSION_CODES.ECLAIR_0_1: return "Eclair 0.1";
            case Build.VERSION_CODES.ECLAIR_MR1: return "Eclair MR1";
            case Build.VERSION_CODES.FROYO: return "Froyo";
            case Build.VERSION_CODES.GINGERBREAD: return "Gingerbread";
            case Build.VERSION_CODES.GINGERBREAD_MR1: return "Gingerbread MR1";
            case Build.VERSION_CODES.HONEYCOMB: return "Honeycomb";
            case Build.VERSION_CODES.HONEYCOMB_MR1: return "Honeycomb MR1";
            case Build.VERSION_CODES.HONEYCOMB_MR2: return "Honeycomb MR2";
            case Build.VERSION_CODES.ICE_CREAM_SANDWICH: return "Ice Cream Sandwich";
            case Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1: return "Ice Cream Sandwich MR1";
            case Build.VERSION_CODES.JELLY_BEAN: return "Jelly Bean";
            case Build.VERSION_CODES.JELLY_BEAN_MR1: return "Jelly Bean MR1";
            case Build.VERSION_CODES.JELLY_BEAN_MR2: return "Jelly Bean MR2";
            case Build.VERSION_CODES.KITKAT: return "KitKat";
            case Build.VERSION_CODES.KITKAT_WATCH: return "KitKat Watch";
            case Build.VERSION_CODES.LOLLIPOP: return "Lollipop";
            case Build.VERSION_CODES.LOLLIPOP_MR1: return "Lollipop MR1";
            case Build.VERSION_CODES.M: return "Marshmallow";
            case Build.VERSION_CODES.N: return "Nougat";
            case Build.VERSION_CODES.N_MR1: return "Nougat MR1";
            case Build.VERSION_CODES.O: return "Oreo";
            case Build.VERSION_CODES.O_MR1: return "Oreo MR1";
            case Build.VERSION_CODES.P: return "Pie";
            case Build.VERSION_CODES.Q: return "Android 10";
            case Build.VERSION_CODES.R: return "Android 11";
            case Build.VERSION_CODES.S: return "Android 12";
            case Build.VERSION_CODES.S_V2: return "Android 12L";
            case Build.VERSION_CODES.TIRAMISU: return "Android 13";
            case 34: return "Android 14"; // Replace 34 with the actual constant if available in your build environment.
            default: return ""; // For future versions
        }
    }
}


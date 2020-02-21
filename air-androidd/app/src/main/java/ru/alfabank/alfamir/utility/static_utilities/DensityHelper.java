package ru.alfabank.alfamir.utility.static_utilities;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by U_M0WY5 on 21.11.2017.
 */

public class DensityHelper {

    private static int numberOfColumns;
    private static int remaining;

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

        dpWidth = dpWidth - 12;

        int noOfColumns = (int) (dpWidth / 76);
        numberOfColumns = noOfColumns;
        remaining = displayMetrics.widthPixels - (numberOfColumns * 64);
        return noOfColumns;
    }

    public static int calculateSpacing() {
//        System.out.println("\nNumber Of Columns\t"+ numberOfColumns+"\nRemaining Space\t"+remaining+"\nSpacing\t"+remaining/(2*numberOfColumns)+"\nWidth\t"+width+"\nHeight\t"+height+"\nDisplay DPI\t"+displayMetrics.densityDpi+"\nDisplay Metrics Width\t"+displayMetrics.widthPixels);
        return remaining / (2 * (numberOfColumns-1));
    }
}

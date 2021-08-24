package com.bizmiz.kvartirabor.ui.util;

import com.bizmiz.kvartirabor.R;

public class ColorTransparentUtils {
    public static final int defaultColorID = R.color.trans;
    public static final String defaultColor = "00ffffff";
    public static final String TAG = "ColorTransparentUtils";

    /**
     * This method convert numver into hexa number or we can say transparent code
     *
     * @param trans number of transparency you want
     * @return it return hex decimal number or transparency code
     */
    public static String convert(int trans) {
        String hexString = Integer.toHexString(Math.round(255 * trans / 100));
        return (hexString.length() < 2 ? "0" : "") + hexString;
    }

    public static String transparentColor(int colorCode, int trans) {
        return convertIntoColor(colorCode, trans);
    }


    /**
     * Convert color code into transparent color code
     *
     * @param colorCode color code
     * @param transCode transparent number
     * @return transparent color code
     */
    public static String convertIntoColor(int colorCode, int transCode) {
        // convert color code into hexa string and remove starting 2 digit

        String color = defaultColor;
        try{
            color = Integer.toHexString(colorCode).toUpperCase().substring(2);
        }catch (Exception ignored){}

        if (!color.isEmpty() && transCode < 100) {
            if (color.trim().length() == 6) {
                return "#" + convert(transCode) + color;
            } else {
                return convert(transCode) + color;
            }
        }
        // if color is empty or any other problem occur then we return deafult color;
        return "#" + Integer.toHexString(defaultColorID).toUpperCase().substring(2);
    }
}

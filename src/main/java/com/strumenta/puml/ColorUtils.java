package com.strumenta.puml;

import java.awt.*;

public class ColorUtils {
    public static String toHexColor(Color color) {
        return toHexColorComponent(color.getRed()) + toHexColorComponent(color.getGreen()) + toHexColorComponent(color.getBlue());
    }

    public static String toHexColorComponent(int component) {
        String value = Integer.toHexString(component);
        if (value.length() == 1) {
            return "0" + value;
        } else {
            return value;
        }
    }
}

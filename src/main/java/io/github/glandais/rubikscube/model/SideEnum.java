package io.github.glandais.rubikscube.model;

import javafx.scene.paint.Color;
import lombok.Getter;

@Getter
public enum SideEnum {
    F( "Green", Color.hsb(120, Constants.SATURATION, Constants.BRIGHTNESS), 2),
    B("Blue", Color.hsb(240, Constants.SATURATION, Constants.BRIGHTNESS), 4),
    U("White", Color.hsb(0, 0, Constants.BRIGHTNESS), 255),
    D("Yellow", Color.hsb(60, Constants.SATURATION, Constants.BRIGHTNESS), 226),
    R("Red", Color.hsb(0, Constants.SATURATION, Constants.BRIGHTNESS), 9),
    L("Orange", Color.hsb(30, Constants.SATURATION, Constants.BRIGHTNESS), 208);

    final String colorName;
    final Color color;
    final int ansiColor;

    SideEnum(String colorName, Color color, int ansiColor) {
        this.colorName = colorName;
        this.color = color;
        this.ansiColor = ansiColor;
    }

    private static class Constants {
        public static final double SATURATION = 1.0;
        public static final double BRIGHTNESS = 0.8;
    }
}

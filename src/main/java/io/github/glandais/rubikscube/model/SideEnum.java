package io.github.glandais.rubikscube.model;

import javafx.scene.paint.Color;
import lombok.Getter;

@Getter
public enum SideEnum {
    F( Color.hsb(120, Constants.SATURATION, Constants.BRIGHTNESS), 2),
    B(Color.hsb(240, Constants.SATURATION, Constants.BRIGHTNESS), 4),
    U(Color.hsb(0, 0, Constants.BRIGHTNESS), 255),
    D(Color.hsb(60, Constants.SATURATION, Constants.BRIGHTNESS), 226),
    R(Color.hsb(0, Constants.SATURATION, Constants.BRIGHTNESS), 9),
    L(Color.hsb(30, Constants.SATURATION, Constants.BRIGHTNESS), 208);

    final Color color;
    final int ansiColor;

    SideEnum(Color color, int ansiColor) {
        this.color = color;
        this.ansiColor = ansiColor;
    }

    private static class Constants {
        public static final double SATURATION = 1.0;
        public static final double BRIGHTNESS = 0.8;
    }
}

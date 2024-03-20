package io.github.glandais.rubikscube.model;

import javafx.scene.paint.Color;
import lombok.Getter;
import org.fusesource.jansi.Ansi;

@Getter
public enum SideEnum {
    F(Color.GREEN, Color.GREEN, 2),
    B(Color.BLUE, Color.BLUE, 4),
    U(Color.WHITE, Color.WHITE, 255),
    D(Color.YELLOW, Color.YELLOW, 226),
    R(Color.RED, Color.RED, 9),
    L(Color.ORANGE, Color.ORANGE, 208);

    final Color color;
    final Color darkColor;
    final int ansiColor;

    SideEnum(Color color, Color darkColor, int ansiColor) {
        this.color = color;
        this.darkColor = darkColor;
        this.ansiColor = ansiColor;
    }
}

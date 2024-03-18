package io.github.glandais.rubikscube.model;

import javafx.scene.paint.Color;
import lombok.Getter;
import org.fusesource.jansi.Ansi;

@Getter
public enum SideEnum {
    F(Color.BLUE, Color.BLUE, Ansi.Color.BLUE),
    B(Color.GREEN, Color.GREEN, Ansi.Color.GREEN),
    U(Color.YELLOW, Color.YELLOW, Ansi.Color.YELLOW),
    D(Color.WHITE, Color.WHITE, Ansi.Color.WHITE),
    R(Color.RED, Color.RED, Ansi.Color.RED),
    L(Color.ORANGE, Color.ORANGE, Ansi.Color.MAGENTA);

    final Color color;
    final Color darkColor;
    final Ansi.Color ansiColor;

    SideEnum(Color color, Color darkColor, Ansi.Color ansiColor) {
        this.color = color;
        this.darkColor = darkColor;
        this.ansiColor = ansiColor;
    }
}

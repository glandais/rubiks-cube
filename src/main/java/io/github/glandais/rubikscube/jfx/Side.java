package io.github.glandais.rubikscube.jfx;

import javafx.scene.paint.Color;
import lombok.Getter;

@Getter
public enum Side {
    F(Color.BLUE, Color.BLUE),
    B(Color.GREEN, Color.GREEN),
    U(Color.YELLOW, Color.YELLOW),
    D(Color.WHITE, Color.WHITE),
    R(Color.RED, Color.RED),
    L(Color.ORANGE, Color.ORANGE);

    final Color color;
    final Color darkColor;

    Side(Color color, Color darkColor) {
        this.color = color;
        this.darkColor = darkColor;
    }
}

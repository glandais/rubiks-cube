package io.github.glandais.rubikscube.jfx;

import javafx.scene.control.Tooltip;

public class MovesButton extends ActionButton {
    public MovesButton(String label, String moves) {
        super(label, () -> RubiksCubeApplication.rubiksCubeInteract.applyMoves(moves, true, true, 100));
        setTooltip(new Tooltip(moves));
    }
}

package io.github.glandais.rubikscube.jfx;

import javafx.beans.property.BooleanProperty;

public class MovesButton extends ActionButton {
    public MovesButton(BooleanProperty disable, String label, String movesDesc, String moves) {
        super(disable, label, moves, () -> RubiksCubeApplication.rubiksCubeInteract.applyMoves(movesDesc, moves, true));
    }
}

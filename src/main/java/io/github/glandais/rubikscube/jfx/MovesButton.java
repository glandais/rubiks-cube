package io.github.glandais.rubikscube.jfx;

public class MovesButton extends ActionButton {
    public MovesButton(String label, String moves) {
        super(label, moves, () -> RubiksCubeApplication.rubiksCubeInteract.applyMoves(moves, true, true, 100));
    }
}

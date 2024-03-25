package io.github.glandais.rubikscube.jfx;

import io.github.glandais.rubikscube.model.Action;
import io.github.glandais.rubikscube.model.rotation.RotationEnum;
import javafx.beans.property.BooleanProperty;

public class RotationButton extends MovesButton {
    public RotationButton(BooleanProperty disable, Action action) {
        super(disable, action.getNotation(), action.getNotation(), action.getNotation());
    }
}

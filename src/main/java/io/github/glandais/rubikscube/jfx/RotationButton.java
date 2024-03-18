package io.github.glandais.rubikscube.jfx;

import io.github.glandais.rubikscube.model.RotationEnum;
import javafx.scene.control.Button;

public class RotationButton extends Button {
    public RotationButton(RotationEnum rotationEnum) {
        super(rotationEnum.getNotation());
        setOnAction(e -> {
            RubiksCubeApplication.rubiksCubeInteract.rotate(rotationEnum, 100);
        });
    }
}

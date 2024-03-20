package io.github.glandais.rubikscube.jfx;

import io.github.glandais.rubikscube.model.rotation.RotationEnum;

public class RotationButton extends MovesButton {
    public RotationButton(RotationEnum rotationEnum) {
        super(rotationEnum.getNotation(), rotationEnum.getNotation());
    }
}

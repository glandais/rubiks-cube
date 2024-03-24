package io.github.glandais.rubikscube.jfx.scene;

import io.github.glandais.rubikscube.model.rotation.RotationEnum;

public class RotationDraggedModel extends RotationModel {

    private boolean dragging = true;

    public RotationDraggedModel(Cube3 cube3, RotationEnum rotation, int duration90) {
        super(cube3, rotation, duration90);
    }

    public void released(RotationEnum rotation, double targetAngle) {
        this.dragging = false;
        this.setRotation(rotation);
        this.setAngles(getCurrentAngle(), targetAngle);
    }

    @Override
    public boolean tick() {
        if (dragging) {
            return false;
        }
        return super.tick();
    }

}

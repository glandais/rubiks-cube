package io.github.glandais.rubikscube.jfx.scene;

import io.github.glandais.rubikscube.model.rotation.RotationEnum;

public class RotationPlayModel extends RotationModel {
    private final long duration;
    private long rotationStart;

    public RotationPlayModel(Cube3 cube3, RotationEnum rotation, long duration) {
        super(cube3, rotation);
        this.duration = duration;
        this.rotationStart = -1;
    }

    public boolean tick() {
        if (rotationStart == -1) {
            rotationStart = System.currentTimeMillis();
        }
        long rotationElapsed = System.currentTimeMillis() - rotationStart;
        double ratio;
        if (duration <= 0) {
            ratio = 1.0;
        } else {
            ratio = 1.0 * rotationElapsed / duration;
        }
        if (ratio >= 1.0) {
            applyRotation();
            return true;
        } else {
            rotate(ratio);
            return false;
        }
    }

}

package io.github.glandais.rubikscube.jfx;

import javafx.geometry.Point3D;
import javafx.scene.transform.Rotate;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class RotationModel {

    private final Cube3 cube3;
    private final Integer x;
    private final Integer y;
    private final Integer z;
    private long rotationStart;
    private Rotate rotate;
    private Double angle;
    private List<Cube> rotated;
    private long duration;

    public RotationModel(Cube3 cube3, double angle, Point3D axis, Integer x, Integer y, Integer z, long duration) {
        super();
        this.cube3 = cube3;
        this.rotationStart = -1;
        this.angle = angle;
        this.rotate = new Rotate(0, axis);
        this.x = x;
        this.y = y;
        this.z = z;
        this.duration = duration;
    }

    public boolean apply() {
        if (rotationStart == -1) {
            rotationStart = System.currentTimeMillis();
            this.rotated = cube3.getCubes(x, y, z);
            for (Cube cube : rotated) {
                cube.getTransforms().addFirst(rotate);
            }
        }
        long rotationEllapsed = System.currentTimeMillis() - rotationStart;
        double rotation;
        if (duration <= 0) {
            rotation = angle;
        } else {
            rotation = Math.signum(angle) * rotationEllapsed * (90.0 / duration);
        }
        if (Math.abs(rotation) >= Math.abs(angle)) {
            rotate.setAngle(angle);
            for (Cube cube : rotated) {
                cube.rotate(rotate);
            }
            return false;
        } else {
            rotate.setAngle(rotation);
            return true;
        }
    }
}

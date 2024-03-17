package io.github.glandais.rubikscube.jfx;

import javafx.geometry.Point3D;
import javafx.scene.transform.Rotate;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class Rotation {

    private long rotationStart;
    private Rotate rotate;
    private Double angle;
    private List<SmallCube> rotated;
    private long duration;

    public Rotation(double angle, Point3D axis, List<SmallCube> rotated, long duration) {
        super();
        this.rotationStart = -1;
        this.angle = angle;
        this.rotate = new Rotate(0, axis);
        this.rotated = rotated;
        this.duration = duration;
    }

    public boolean apply(long ellapsed) {
        if (rotationStart == -1) {
            rotationStart = System.currentTimeMillis();
            for (SmallCube smallCube : rotated) {
                smallCube.getTransforms().addFirst(rotate);
            }
        }
        long rotationEllapsed = System.currentTimeMillis() - rotationStart;
        double rotation = Math.signum(angle) * rotationEllapsed * (90.0 / duration);
        if (Math.abs(rotation) > Math.abs(angle)) {
            rotate.setAngle(angle);
            for (SmallCube smallCube : rotated) {
                smallCube.rotate(rotate);
            }
            return false;
        } else {
            rotate.setAngle(rotation);
            return true;
        }
    }
}

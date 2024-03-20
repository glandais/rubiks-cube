package io.github.glandais.rubikscube.jfx.scene;

import io.github.glandais.rubikscube.model.rotation.AxisEnum;
import io.github.glandais.rubikscube.model.rotation.RotationEnum;
import javafx.geometry.Point3D;
import javafx.scene.transform.Rotate;
import lombok.Getter;
import lombok.Synchronized;

import java.util.List;

import static javafx.scene.transform.Rotate.X_AXIS;
import static javafx.scene.transform.Rotate.Y_AXIS;
import static javafx.scene.transform.Rotate.Z_AXIS;

public abstract class RotationModel {

    private final Cube3 cube3;
    @Getter
    private final RotationEnum rotation;
    private final Rotate rotate;
    @Getter
    private final AxisEnum axis;
    private List<Cube> rotated;

    public RotationModel(Cube3 cube3, RotationEnum rotation) {
        super();
        this.cube3 = cube3;
        this.rotation = rotation;
        this.axis = rotation.getAxis();
        Point3D axis3D = switch (axis) {
            case X -> X_AXIS;
            case Y -> Y_AXIS;
            case Z -> Z_AXIS;
        };
        this.rotate = new Rotate(0, axis3D);
    }

    @Synchronized
    public List<Cube> getRotated() {
        if (rotated == null) {
            this.rotated = cube3.getCubes(rotation.getX(), rotation.getY(), rotation.getZ());
            for (Cube cube : rotated) {
                cube.getTransforms().addFirst(rotate);
            }
        }
        return rotated;
    }

    @Synchronized
    public void rotate(double ratio) {
        double angle = ratio * rotation.getAngle();
        getRotated();
        rotate.setAngle(angle);
    }

    @Synchronized
    public double getCurrentAngle() {
        return rotate.getAngle();
    }

    @Synchronized
    public void applyRotation() {
        getRotated();
        rotate.setAngle(rotation.getAngle());
        for (Cube cube : getRotated()) {
            cube.rotate(rotate);
        }
    }

    @Synchronized
    public void cancel() {
        for (Cube cube : getRotated()) {
            cube.getTransforms().removeFirst();
        }
    }
}

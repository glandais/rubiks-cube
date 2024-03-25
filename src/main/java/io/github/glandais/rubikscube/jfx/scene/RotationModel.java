package io.github.glandais.rubikscube.jfx.scene;

import io.github.glandais.rubikscube.model.rotation.AxisEnum;
import io.github.glandais.rubikscube.model.rotation.RotationEnum;
import javafx.geometry.Point3D;
import javafx.scene.transform.Rotate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;

import java.util.List;

import static javafx.scene.transform.Rotate.X_AXIS;
import static javafx.scene.transform.Rotate.Y_AXIS;
import static javafx.scene.transform.Rotate.Z_AXIS;

public abstract class RotationModel extends ActionModel {

    private final Cube3 cube3;
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private RotationEnum rotation;
    private final Rotate rotate;
    @Getter
    private final AxisEnum axis;
    private final int duration90;
    private List<Cube> rotated;
    private long duration;
    private long rotationStart;
    private double startAngle;
    private double endAngle;

    public RotationModel(Cube3 cube3, RotationEnum rotation, int duration90) {
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
        this.rotationStart = -1;
        this.duration90 = duration90;
        this.setAngles(0.0, rotation.getAngle());
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
        double angle = this.startAngle + ratio * (this.endAngle - this.startAngle);
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
        rotate.setAngle(this.endAngle);
        for (Cube cube : getRotated()) {
            cube.rotate(rotate);
        }
    }

    @Override
    @Synchronized
    public void cancel() {
        for (Cube cube : getRotated()) {
            cube.getTransforms().removeFirst();
        }
    }

    @Override
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

    protected void setAngles(double startAngle, double endAngle) {
        this.startAngle = startAngle;
        this.endAngle = endAngle;
        this.duration = Math.round(this.duration90 * Math.abs(startAngle - endAngle) / 90.0);
    }

}

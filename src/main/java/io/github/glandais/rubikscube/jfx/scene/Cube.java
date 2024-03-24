package io.github.glandais.rubikscube.jfx.scene;

import io.github.glandais.rubikscube.model.SideEnum;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import lombok.Getter;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
public class Cube extends Group {

    private final Point3D initialPosition;
    private final Translate translate;
    private final Rotate rotateX;
    private final Rotate rotateY;
    private final Rotate rotateZ;
    private Point3D currentPosition;

    private final List<Facelet> faces;
    private final List<Facelet> realFaces;

    public Cube(int x, int y, int z) {
        super();
        this.initialPosition = new Point3D(x, y, z);
        this.currentPosition = new Point3D(x, y, z);

        this.faces = new ArrayList<>();
        this.realFaces = new ArrayList<>();
        for (SideEnum sideEnum : SideEnum.values()) {
            Facelet facelet = new Facelet(this, sideEnum);
            faces.add(facelet);
            if (facelet.getInitialPosition() != null) {
                realFaces.add(facelet);
            }
            getChildren().add(facelet);
        }
        rotateX = new Rotate(0, Rotate.X_AXIS);
        rotateY = new Rotate(0, Rotate.Y_AXIS);
        rotateZ = new Rotate(0, Rotate.Z_AXIS);
        translate = new Translate(2.0 * x, 2.0 * y, 2.0 * z);
        getTransforms().setAll(
                translate,
                rotateX,
                rotateY,
                rotateZ
        );
    }

    public void rotate(Rotate rotate) {
        this.currentPosition = rotate.transform(this.currentPosition);
        for (Facelet face : faces) {
            face.rotate(rotate);
        }
    }

    public int getX() {
        return (int) (Math.round(this.currentPosition.getX()));
    }

    public int getY() {
        return (int) (Math.round(this.currentPosition.getY()));
    }

    public int getZ() {
        return (int) (Math.round(this.currentPosition.getZ()));
    }

    @Override
    public String toString() {
        return "SmallCube{" +
               "initialPosition=" + initialPosition +
               ", currentPosition=" + currentPosition +
               '}';
    }

    public void resetView() {
        rotateX.setAngle(0.0);
        rotateY.setAngle(0.0);
        rotateZ.setAngle(0.0);
        translate.setX(2 * this.initialPosition.getX());
        translate.setY(2 * this.initialPosition.getY());
        translate.setZ(2 * this.initialPosition.getZ());
    }

    public static final long duration = 5_000;

    private static final Random rand = new SecureRandom();
    private double d0;
    private double r;

    private int nt;

    private int rx;
    private int ry;
    private int rz;

    public void initExplode() {
        d0 = 3.5 + rand.nextDouble(1.0);
        r = d0 - 2.0;
        nt = 8;//3 + rand.nextInt(6);
        rx = 3 + rand.nextInt(6);
        ry = 3 + rand.nextInt(6);
        rz = 0;//3 + rand.nextInt(6);
    }

    public boolean explode(long elapsed) {
        elapsed = Math.min(elapsed, duration);
        double d = d0 + r * Math.sin(2 * Math.PI * (0.75 + (1.0 * elapsed * nt / duration)));
        translate.setX(d * this.initialPosition.getX());
        translate.setY(d * this.initialPosition.getY());
        translate.setZ(d * this.initialPosition.getZ());
        rotateX.setAngle(elapsed * 360.0 * rx / duration);
        rotateY.setAngle(elapsed * 360.0 * ry / duration);
        rotateZ.setAngle(elapsed * 360.0 * rz / duration);
        return elapsed == duration;
    }

}

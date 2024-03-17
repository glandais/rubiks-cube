package io.github.glandais.rubikscube.jfx;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.transform.Rotate;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SmallCube extends Group {

    private final Point3D initialPosition;
    private Point3D currentPosition;

    private final List<SmallCubeFace> faces;

    public SmallCube(int x, int y, int z) {
        super();
        this.initialPosition = new Point3D(x, y, z);
        this.currentPosition = new Point3D(x, y, z);

        this.faces = new ArrayList<>();
        for (Side side : Side.values()) {
            SmallCubeFace smallCubeFace = new SmallCubeFace(this, side);
            faces.add(smallCubeFace);
            getChildren().add(smallCubeFace);
        }
    }

    public void rotate(Rotate rotate) {
        this.currentPosition = rotate.transform(this.currentPosition);
        for (SmallCubeFace face : faces) {
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
}

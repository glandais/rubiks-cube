package io.github.glandais.rubikscube.jfx.scene;

import io.github.glandais.rubikscube.model.SideEnum;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.transform.Rotate;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Cube extends Group {

    private final Point3D initialPosition;
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
}

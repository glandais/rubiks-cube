package io.github.glandais.rubikscube.jfx;

import io.github.glandais.rubikscube.model.SideEnum;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import lombok.Getter;

@Getter
public class Facelet extends Group {
    private final SideEnum sideEnum;
    private final Cube cube;

    private Facelet3DEnum facelet3DEnum;
    private Point3D initialPosition;
    private Point3D currentPosition;

    public Facelet(Cube cube, SideEnum sideEnum) {
        super();
        this.cube = cube;
        this.sideEnum = sideEnum;
        switch (sideEnum) {
            case F -> addFaces(1, 1, 0, 1);
            case B -> addFaces(1, 1, 0, -1);
            case U -> addFaces(1, 0, 1, 1);
            case D -> addFaces(1, 0, 1, -1);
            case R -> addFaces(0, 1, 1, 1);
            case L -> addFaces(0, 1, 1, -1);
        }
        getTransforms().add(new Translate(2.0 * cube.getX(), 2.0 * cube.getY(), 2.0 * cube.getZ()));
    }

    private void addFaces(int w, int h, int d, int dc) {
        if (
                (sideEnum == SideEnum.F && cube.getZ() == 1) ||
                        (sideEnum == SideEnum.B && cube.getZ() == -1) ||
                        (sideEnum == SideEnum.U && cube.getY() == 1) ||
                        (sideEnum == SideEnum.D && cube.getY() == -1) ||
                        (sideEnum == SideEnum.R && cube.getX() == 1) ||
                        (sideEnum == SideEnum.L && cube.getX() == -1)
        ) {
            addFace(w * 0.95, h * 0.95, d * 0.95, dc * 0.5, true);
        }
        addFace(w, h, d, dc * 0.49, false);
    }

    private void addFace(double w, double h, double d, double dc, boolean trueColor) {
        Box face = new Box(2 * w, 2 * h, 2 * d);
        Translate translate;
        if (w == 0) {
            translate = new Translate(2 * dc, 0, 0);
        } else if (h == 0) {
            translate = new Translate(0, 2 * dc, 0);
        } else {
            translate = new Translate(0, 0, 2 * dc);
        }
        face.getTransforms().add(translate);
        if (trueColor) {
            PhongMaterial material = new PhongMaterial();
            material.setDiffuseColor(sideEnum.getDarkColor());
            material.setSpecularColor(sideEnum.getColor());
            face.setMaterial(material);

            this.initialPosition = new Point3D(
                    cube.getX() + translate.getX(),
                    cube.getY() + translate.getY(),
                    cube.getZ() + translate.getZ()
            );
            this.currentPosition = this.initialPosition;
            this.facelet3DEnum = Facelet3DEnum.of(this.initialPosition);
        } else {
            face.setMaterial(new PhongMaterial(Color.BLACK));
        }
        getChildren().add(face);
    }

    public void rotate(Rotate rotate) {
        if (this.currentPosition != null) {
            this.currentPosition = rotate.transform(this.currentPosition);
        }
    }

    @Override
    public String toString() {
        return "SmallCubeFace{" +
                "smallCube=" + cube.getInitialPosition() +
                ", side=" + sideEnum +
                ", facelet3DEnum=" + facelet3DEnum +
                ", initialPosition=" + initialPosition +
                ", currentPosition=" + currentPosition +
                '}';
    }
}

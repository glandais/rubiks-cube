package io.github.glandais.rubikscube.jfx;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import lombok.Getter;

@Getter
public class SmallCubeFace extends Group {
    private final Side side;
    private final SmallCube smallCube;

    private boolean realSide;
    private Point3D initialPosition;
    private Point3D currentPosition;

    public SmallCubeFace(SmallCube smallCube, Side side) {
        super();
        this.smallCube = smallCube;
        this.side = side;
        switch (side) {
            case F -> addFaces(1, 1, 0, 1);
            case B -> addFaces(1, 1, 0, -1);
            case U -> addFaces(1, 0, 1, 1);
            case D -> addFaces(1, 0, 1, -1);
            case R -> addFaces(0, 1, 1, 1);
            case L -> addFaces(0, 1, 1, -1);
        }
        getTransforms().add(new Translate(2.0 * smallCube.getX(), 2.0 * smallCube.getY(), 2.0 * smallCube.getZ()));
    }

    private void addFaces(int w, int h, int d, int dc) {
        if (
                (side == Side.F && smallCube.getZ() == 1) ||
                        (side == Side.B && smallCube.getZ() == -1) ||
                        (side == Side.U && smallCube.getY() == 1) ||
                        (side == Side.D && smallCube.getY() == -1) ||
                        (side == Side.R && smallCube.getX() == 1) ||
                        (side == Side.L && smallCube.getX() == -1)
        ) {
            addFace(w * 0.95, h * 0.95, d * 0.95, dc * 0.5, true);
            realSide = true;
        } else {
            realSide = false;
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
            material.setDiffuseColor(side.getDarkColor());
            material.setSpecularColor(side.getColor());
            face.setMaterial(material);

            this.initialPosition = new Point3D(
                    smallCube.getX() + translate.getX(),
                    smallCube.getY() + translate.getY(),
                    smallCube.getZ() + translate.getZ()
            );
            this.currentPosition = this.initialPosition;
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
                "smallCube=" + smallCube.getInitialPosition() +
                ", side=" + side +
                ", realSide=" + realSide +
                ", initialPosition=" + initialPosition +
                ", currentPosition=" + currentPosition +
                '}';
    }
}

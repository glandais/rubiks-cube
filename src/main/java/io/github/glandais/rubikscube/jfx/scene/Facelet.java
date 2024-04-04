package io.github.glandais.rubikscube.jfx.scene;

import io.github.glandais.rubikscube.model.FaceletEnum;
import io.github.glandais.rubikscube.model.SideEnum;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class Facelet extends Group {
    private static final Color BORDER = Color.hsb(0, 0, 0.05);
    private static final Color HIDDEN_FACE = Color.hsb(0, 0, 0.2);
    public static final Color SPECULAR_COLOR = Color.hsb(0, 0, 0.3);
    private final SideEnum sideEnum;
    private final Cube cube;

    private FaceletEnum initialPosition;
    private FaceletEnum currentPosition;
    private Point3D initialPosition3D;
    private Point3D currentPosition3D;

    private static Map<Point3D, Image> textures = new HashMap<>();

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
            addFace(w * 0.95, h * 0.95, d * 0.95, dc * 0.5, FaceletTypeEnum.PLAYABLE);
        } else {
            addFace(w * 0.95, h * 0.95, d * 0.95, dc * 0.5, FaceletTypeEnum.NOT_PLAYABLE);
        }
        addFace(w, h, d, dc * 0.49, FaceletTypeEnum.BORDER);
    }

    private void addFace(double w, double h, double d, double dc, FaceletTypeEnum faceletType) {
        Box face = new FaceletBox(this, faceletType, 2 * w, 2 * h, 2 * d);
        Translate translate;
        if (w == 0) {
            translate = new Translate(2 * dc, 0, 0);
        } else if (h == 0) {
            translate = new Translate(0, 2 * dc, 0);
        } else {
            translate = new Translate(0, 0, 2 * dc);
        }
        face.getTransforms().add(translate);

        this.initialPosition3D = new Point3D(
                cube.getX() + translate.getX(),
                cube.getY() + translate.getY(),
                cube.getZ() + translate.getZ()
        );
        this.currentPosition3D = this.initialPosition3D;
        Facelet3DEnum facelet3DEnum = Facelet3DEnum.of(this.currentPosition3D);
        if (facelet3DEnum != null) {
            this.initialPosition = facelet3DEnum.getFaceletEnum();
            this.currentPosition = this.initialPosition;
        }

        PhongMaterial material = new PhongMaterial();
        switch (faceletType) {
            case PLAYABLE -> getSetDiffuse(material);
            case NOT_PLAYABLE -> material.setDiffuseColor(HIDDEN_FACE);
            case BORDER -> material.setDiffuseColor(BORDER);
        }
        material.setSpecularColor(SPECULAR_COLOR);
        face.setMaterial(material);

        getChildren().add(face);
    }

    private void getSetDiffuse(PhongMaterial material) {
        Image image = textures.computeIfAbsent(this.initialPosition3D, this::getTexture);
        if (image != null) {
            material.setDiffuseMap(image);
        } else {
            material.setDiffuseColor(sideEnum.getColor());
        }
    }

    private Image getTexture(Point3D p) {
        Facelet3DEnum facelet3DEnum = Facelet3DEnum.of(p);
        if (facelet3DEnum == null) {
            return null;
        }
        return new Image("/textures/" + facelet3DEnum.name() + ".png", true);
    }

    public void rotate(Rotate rotate) {
        if (this.currentPosition3D != null) {
            this.currentPosition3D = rotate.transform(this.currentPosition3D);
            Facelet3DEnum facelet3DEnum = Facelet3DEnum.of(this.currentPosition3D);
            if (facelet3DEnum != null) {
                this.currentPosition = facelet3DEnum.getFaceletEnum();
            }
        }
    }

    @Override
    public String toString() {
        return "Facelet{" +
               "smallCube=" + cube.getInitialPosition() +
               ", initialPosition=" + initialPosition +
               ", currentPosition=" + currentPosition +
               '}';
    }

}

package io.github.glandais.rubikscube.jfx;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;

import java.util.List;

public class Axes extends Group {

    public Axes() {
        super();

        double dimModel = 10.0;
        double length = 1.4d * dimModel;
        double width = dimModel / 100d;
        double radius = 2d * dimModel / 100d;
        final PhongMaterial xMaterial = new PhongMaterial();
        xMaterial.setDiffuseColor(Color.DARKRED);
        xMaterial.setSpecularColor(Color.RED);
        final PhongMaterial yMaterial = new PhongMaterial();
        yMaterial.setDiffuseColor(Color.DARKGREEN);
        yMaterial.setSpecularColor(Color.GREEN);
        final PhongMaterial zMaterial = new PhongMaterial();
        zMaterial.setDiffuseColor(Color.DARKBLUE);
        zMaterial.setSpecularColor(Color.BLUE);

        Sphere xSphere = new Sphere(radius);
        Sphere ySphere = new Sphere(radius);
        Sphere zSphere = new Sphere(radius);
        xSphere.setMaterial(xMaterial);
        ySphere.setMaterial(yMaterial);
        zSphere.setMaterial(zMaterial);

        xSphere.setTranslateX(0.7 * dimModel);
        ySphere.setTranslateY(0.7 * dimModel);
        zSphere.setTranslateZ(0.7 * dimModel);

        Box xAxis = new Box(length, width, width);
        Box yAxis = new Box(width, length, width);
        Box zAxis = new Box(width, width, length);
        xAxis.setMaterial(xMaterial);
        yAxis.setMaterial(yMaterial);
        zAxis.setMaterial(zMaterial);

        getChildren().addAll(
                List.of(
                        xAxis, xSphere,
                        yAxis, ySphere,
                        zAxis, zSphere
                )
        );
    }
}

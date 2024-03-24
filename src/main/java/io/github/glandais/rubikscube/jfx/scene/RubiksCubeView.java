package io.github.glandais.rubikscube.jfx.scene;

import javafx.geometry.Point3D;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import lombok.Getter;

public class RubiksCubeView {
    private final PerspectiveCamera camera = new PerspectiveCamera(true);
    private double cameraTranslation;
    @Getter
    private final Cube3 cube3 = new Cube3();

    public RubiksCubeView() {
        super();
    }

    public SubScene buildSubScene() {
        AmbientLight ambientLight = new AmbientLight();
        ambientLight.setColor(new Color(0.5, 0.5, 0.5, 1));
        PointLight pointLight = new PointLight();
        pointLight.setColor(new Color(0.5, 0.5, 0.5, 1));
        pointLight.getTransforms().add(new Translate(0, 0, -20));
        Group group = new Group(cube3, ambientLight, pointLight);
        SubScene subScene = new SubScene(group, 800, 600, true, SceneAntialiasing.BALANCED);
        subScene.setCamera(camera);
        subScene.setFill(Color.CADETBLUE);
        return subScene;
    }

    public void resetView() {
        cameraTranslation = 23.0;
        updateCamera();
        cube3.resetView();
    }

    public void onZoom(double ratio) {
        cameraTranslation = cameraTranslation * ratio;
        cameraTranslation = Math.min(60.0, Math.max(6.0, cameraTranslation));
        updateCamera();
    }

    private void updateCamera() {
        setCameraTransforms(0, 0, -cameraTranslation);
    }

    private void setCameraTransforms(double x, double y, double z) {
        Point3D lookAtPos = new Point3D(0, 0, 0);
        Point3D cameraPosition = new Point3D(x, y, z);
        //Create direction vector
        Point3D camDirection = lookAtPos.subtract(cameraPosition.getX(), cameraPosition.getY(), cameraPosition.getZ());
        camDirection = camDirection.normalize();

        double xRotation = Math.toDegrees(Math.asin(-camDirection.getY()));
        double yRotation = Math.toDegrees(Math.atan2(camDirection.getX(), camDirection.getZ()));

        Rotate rx = new Rotate(xRotation, cameraPosition.getX(), cameraPosition.getY(), cameraPosition.getZ(), Rotate.X_AXIS);
        Rotate ry = new Rotate(yRotation, cameraPosition.getX(), cameraPosition.getY(), cameraPosition.getZ(), Rotate.Y_AXIS);

        camera.getTransforms().setAll(ry, rx,
                new Translate(
                        cameraPosition.getX(),
                        cameraPosition.getY(),
                        cameraPosition.getZ()));
    }

    public void reset() {
        cube3.reset();
    }

    public void rotate(double dx, double dy) {
        cube3.rotate(dx, dy);
    }

    public double getAngleX() {
        return cube3.getAngleX();
    }

    public double getAngleY() {
        return cube3.getAngleY();
    }

    public boolean explode(long elapsed) {
        return cube3.explode(elapsed);
    }

    public void initExplode() {
        cube3.initExplode();
    }
}

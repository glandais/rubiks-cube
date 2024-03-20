package io.github.glandais.rubikscube.jfx.scene;

import javafx.geometry.Point3D;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import lombok.Getter;

import static javafx.scene.transform.Rotate.X_AXIS;
import static javafx.scene.transform.Rotate.Y_AXIS;

public class RubiksCubeView {
    private final PerspectiveCamera camera = new PerspectiveCamera(true);
    private double cameraTranslation;
    @Getter
    private final Cube3 cube3 = new Cube3();
    private final Rotate rotateX = new Rotate(0, X_AXIS);
    private final Rotate rotateY = new Rotate(0, Y_AXIS);

    public RubiksCubeView() {
        super();
        cube3.getTransforms().setAll(rotateX, rotateY);
    }

    public SubScene buildSubScene() {
        SubScene subScene = new SubScene(cube3, 800, 600, true, SceneAntialiasing.BALANCED);
        subScene.setCamera(camera);
        subScene.setFill(Color.CADETBLUE);
        return subScene;
    }

    public void resetView() {
        cameraTranslation = 30.0;
        updateCamera();
        rotateX.setAngle(200);
        rotateY.setAngle(340);
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
        double angleX = mod360(rotateX.getAngle() + dx);
        double angleY;
        if (angleX >= 90 && angleX <= 270) {
            angleY = mod360(rotateY.getAngle() + dy);
        } else {
            angleY = mod360(rotateY.getAngle() - dy);
        }
        rotateX.setAngle(angleX);
        rotateY.setAngle(angleY);
    }

    public double getAngleX() {
        return rotateX.getAngle();
    }

    public double getAngleY() {
        return rotateY.getAngle();
    }

    private double mod360(double a) {
        while (a < 0) {
            a = a + 360.0;
        }
        while (a >= 360) {
            a = a - 360.0;
        }
        return a;
    }
}

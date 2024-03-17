package io.github.glandais.rubikscube.jfx;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Point3D;
import javafx.scene.PerspectiveCamera;
import javafx.scene.input.*;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.Duration;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static javafx.scene.transform.Rotate.*;

public class RubiksCubeInteract {
    @Setter
    private PerspectiveCamera camera;
    @Setter
    private Cube cube;

    private Translate translateCamera;

    private final List<Rotation> rotations = new ArrayList<>();

    private double anchorX;
    private double anchorY;
    private Rotate rotateX = new Rotate(-135, X_AXIS);
    private Double dRotateX = 0.0;
    private Rotate rotateY = new Rotate(315, Rotate.Y_AXIS);
    private Double dRotateY = 0.0;

    public void setOnKeyPressed(KeyEvent ke) {
        if (ke.getCode() == KeyCode.LEFT) {
            dRotateY = -1.5;
        }
        if (ke.getCode() == KeyCode.RIGHT) {
            dRotateY = 1.5;
        }
        if (ke.getCode() == KeyCode.UP) {
            dRotateX = -1.5;
        }
        if (ke.getCode() == KeyCode.DOWN) {
            dRotateX = 1.5;
        }
//        System.out.println("keyPressed " + ke);
    }

    public void setOnKeyTyped(KeyEvent ke) {
//        System.out.println("keyTyped " + ke);
    }

    public void onKeyReleased(KeyEvent ke) {
        if (ke.getCode() == KeyCode.LEFT) {
            dRotateY = 0.0;
        }
        if (ke.getCode() == KeyCode.RIGHT) {
            dRotateY = 0.0;
        }
        if (ke.getCode() == KeyCode.UP) {
            dRotateX = 0.0;
        }
        if (ke.getCode() == KeyCode.DOWN) {
            dRotateX = 0.0;
        }

        String modifier = "";
        if (ke.isShiftDown()) {
            modifier = "2";
        } else if (ke.isAltDown()) {
            modifier = "'";
        }

        if (ke.getCode() == KeyCode.F) {
            rotateUser("F" + modifier);
        }
        if (ke.getCode() == KeyCode.B) {
            rotateUser("B" + modifier);
        }
        if (ke.getCode() == KeyCode.U) {
            rotateUser("U" + modifier);
        }
        if (ke.getCode() == KeyCode.D) {
            rotateUser("D" + modifier);
        }
        if (ke.getCode() == KeyCode.R) {
            rotateUser("R" + modifier);
        }
        if (ke.getCode() == KeyCode.L) {
            rotateUser("L" + modifier);
        }
        if (ke.getCode() == KeyCode.ESCAPE) {
            reset();
        }
        if (ke.getCode() == KeyCode.F12) {
            debug();
        }
        if (ke.getCode() == KeyCode.F10) {
            getMoves();
        }
//        System.out.println("keyReleased " + ke);
    }

    public void onMousePressed(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
        }
    }

    public void onMouseDragged(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            double xAngle = rotateX.getAngle() - (anchorY - event.getSceneY());
            rotateX.setAngle(xAngle);
            double yAngle = rotateY.getAngle() - (anchorX - event.getSceneX());
            rotateY.setAngle(yAngle);

            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
        }
    }

    public void onMouseClicked(MouseEvent event) {
//            PickResult result = t.getPickResult();
//            Node intersectedNode = result.getIntersectedNode();
//
//            if (intersectedNode instanceof CubeFace cubeFace) {
//                System.out.println(cubeFace.x + " " + cubeFace.y + " " + cubeFace.z);
//            }
    }

    public void onScroll(ScrollEvent event) {
//        System.out.println("onScroll " + event);
        double v = 1000.0;
        double ratio = (v + event.getDeltaY()) / v;
        double z = translateCamera.getZ() * ratio;
        z = Math.max(-60.0, Math.min(-6.0, z));
        translateCamera.setZ(z);
    }

    private void reset() {
        translateCamera = new Translate(0, 0, -30);
        camera.getTransforms().setAll(translateCamera);
        rotateX = new Rotate(-135, X_AXIS);
        rotateY = new Rotate(315, Rotate.Y_AXIS);
        cube.getTransforms().setAll(rotateX, rotateY);
        cube.reset();
    }

    public void rotateUser(String rotation) {
        synchronized (this) {
            if (rotations.isEmpty()) {
                rotate(rotation, 500);
            }
        }
    }

    public void rotate(String rotation, long duration) {
        char move = rotation.charAt(0);

        double angle = 90.0;
        if (rotation.length() > 1) {
            char mod = rotation.charAt(1);
            if (mod == '2') {
                angle = 180;
            } else if (mod == '\'') {
                angle = -90;
            }
        }

        Point3D axis;
        Integer x = null;
        Integer y = null;
        Integer z = null;
        switch (move) {
            case 'F': {
                axis = Z_AXIS;
                z = 1;
                angle = -angle;
                break;
            }
            case 'B': {
                axis = Z_AXIS;
                z = -1;
                break;
            }
            case 'U': {
                axis = Y_AXIS;
                y = 1;
                angle = -angle;
                break;
            }
            case 'D': {
                axis = Y_AXIS;
                y = -1;
                break;
            }
            case 'R': {
                axis = X_AXIS;
                x = 1;
                angle = -angle;
                break;
            }
            case 'L': {
                axis = X_AXIS;
                x = -1;
                break;
            }
            default:
                throw new IllegalArgumentException("Invalid rotation " + rotation);
        }
        List<SmallCube> rotated = cube.getSmallCubes(x, y, z);
        rotations.add(new Rotation(angle, axis, rotated, duration));
    }

    public void start() {
        reset();
        // The main game loop
        Timeline gameLoop = new Timeline();
        gameLoop.setCycleCount(Animation.INDEFINITE);
        AtomicLong last = new AtomicLong(System.currentTimeMillis());
        KeyFrame keyFrame = new KeyFrame(
                Duration.seconds(1.0f / 60.0f), // 60 FPS
                ae -> {
                    long now = System.currentTimeMillis();
                    long ellapsed = now - last.getAndSet(now);
                    update(ellapsed);
                });
        gameLoop.getKeyFrames().add(keyFrame);
        gameLoop.play();
    }

    private void update(long ellapsed) {
        rotateX.setAngle(rotateX.getAngle() + dRotateX);
        rotateY.setAngle(rotateY.getAngle() + dRotateY);

        synchronized (this) {
            if (!rotations.isEmpty()) {
                Rotation rotation = rotations.getFirst();
                if (!rotation.apply(ellapsed)) {
                    rotations.removeFirst();
                }
            }
        }
    }

    private void debug() {
        System.out.println(rotateX);
        System.out.println(rotateY);
        for (SmallCube smallCube : cube.getSmallCubes()) {
            System.out.println(smallCube);
            for (SmallCubeFace face : smallCube.getFaces()) {
                if (face.isRealSide()) {
                    System.out.println(face);
                }
            }
        }
    }

    private void getMoves() {
    }

}

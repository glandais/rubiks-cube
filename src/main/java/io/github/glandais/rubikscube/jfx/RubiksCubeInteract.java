package io.github.glandais.rubikscube.jfx;

import cs.min2phase.Search;
import cs.min2phase.SearchWCA;
import cs.min2phase.Tools;
import io.github.glandais.rubikscube.model.RotationEnum;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Point3D;
import javafx.scene.PerspectiveCamera;
import javafx.scene.input.*;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.Duration;
import lombok.Getter;
import org.worldcubeassociation.tnoodle.puzzle.ThreeByThreeCubeFewestMovesPuzzle;
import org.worldcubeassociation.tnoodle.scrambles.Puzzle;
import org.worldcubeassociation.tnoodle.scrambles.PuzzleStateAndGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static io.github.glandais.rubikscube.model.RotationEnum.*;
import static javafx.scene.transform.Rotate.*;

public class RubiksCubeInteract {
    private final Search search;
    @Getter
    private PerspectiveCamera camera = new PerspectiveCamera(true);
    @Getter
    private Cube3 cube3 = new Cube3();

    private Translate translateCamera;

    private final List<RotationModel> rotationModels = new ArrayList<>();

    private double anchorX;
    private double anchorY;
    private Rotate rotateX = new Rotate(-135, X_AXIS);
    private Double dRotateX = 0.0;
    private Rotate rotateY = new Rotate(315, Y_AXIS);
    private Double dRotateY = 0.0;

    public RubiksCubeInteract() {
        Search.init();
        this.search = new Search();
    }

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

        RotationModifierEnum modifier = RotationModifierEnum.NORMAL;
        if (ke.isShiftDown()) {
            modifier = RotationModifierEnum.DOUBLE;
        } else if (ke.isAltDown()) {
            modifier = RotationModifierEnum.REVERSED;
        }

        if (ke.getCode() == KeyCode.F) {
            rotateUser(F, modifier);
        }
        if (ke.getCode() == KeyCode.B) {
            rotateUser(B, modifier);
        }
        if (ke.getCode() == KeyCode.U) {
            rotateUser(U, modifier);
        }
        if (ke.getCode() == KeyCode.D) {
            rotateUser(D, modifier);
        }
        if (ke.getCode() == KeyCode.R) {
            rotateUser(R, modifier);
        }
        if (ke.getCode() == KeyCode.L) {
            rotateUser(L, modifier);
        }
        if (ke.getCode() == KeyCode.ESCAPE) {
            reset();
        }
        if (ke.getCode() == KeyCode.F12) {
            debug();
        }
        if (ke.getCode() == KeyCode.F2) {
            scramble();
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

    public void resetView() {
        synchronized (this) {
            translateCamera = new Translate(0, 0, -30);
            camera.getTransforms().setAll(translateCamera);
            rotateX = new Rotate(-135, X_AXIS);
            rotateY = new Rotate(315, Y_AXIS);
            cube3.getTransforms().setAll(rotateX, rotateY);
        }
    }

    public void reset() {
        synchronized (this) {
            cube3.reset();
            rotationModels.clear();
        }
    }

    public void rotateUser(RotationEnum rotation, RotationModifierEnum modifierEnum) {
        synchronized (this) {
            if (rotationModels.isEmpty()) {
                RotationModel rotationModel = getRotationModel(rotation, modifierEnum, 100);
                rotationModels.add(rotationModel);
            }
        }
    }

    private void scramble() {
        synchronized (this) {
            reset();
            rotationModels.clear();
            ThreeByThreeCubeFewestMovesPuzzle puzzle = new ThreeByThreeCubeFewestMovesPuzzle();
            String scramble = puzzle.generateScramble();
            String facelets = Tools.fromScramble(scramble);
            String solution = search.solution(facelets, 21, 100000000, 20000, 0);
            System.out.println("Scramble : " + scramble);
            System.out.println("Solution : " + solution);
            String[] moves = scramble.split(" ");
            for (String move : moves) {
                if (!move.isEmpty()) {
                    RotationEnum rotationEnum = forNotation(move);
                    rotate(rotationEnum, 50);
                }
            }
            for (String move : solution.split(" ")) {
                if (!move.isEmpty()) {
                    RotationEnum rotationEnum = forNotation(move);
                    rotate(rotationEnum, 300);
                }
            }
        }
    }

    public void rotate(RotationEnum rotation, long duration) {
        synchronized (this) {
            RotationModel rotationModel = getRotationModel(rotation, RotationModifierEnum.NORMAL, duration);
            rotationModels.add(rotationModel);
        }
    }

    private RotationModel getRotationModel(RotationEnum rotation, RotationModifierEnum modifierEnum, long duration) {
        Point3D axis = switch (rotation.getAxis()) {
            case X -> X_AXIS;
            case Y -> Y_AXIS;
            case Z -> Z_AXIS;
        };
        Integer x = rotation.getX();
        Integer y = rotation.getY();
        Integer z = rotation.getZ();
        double angle = rotation.getAngle();
        switch (modifierEnum) {
            case DOUBLE -> angle = 2.0 * angle;
            case REVERSED -> angle = -angle;
        }
        return new RotationModel(cube3, angle, axis, x, y, z, duration);
    }

    public void start() {
        resetView();
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
            if (!rotationModels.isEmpty()) {
                RotationModel rotationModel = rotationModels.getFirst();
                if (!rotationModel.apply()) {
                    rotationModels.removeFirst();
                }
            }
        }
    }

    private void debug() {
        System.out.println(rotateX);
        System.out.println(rotateY);
        for (Cube cube : cube3.getCubes()) {
            System.out.println(cube);
            for (Facelet face : cube.getFaces()) {
                System.out.println(face);
            }
        }
        System.out.println("****");
        for (Facelet facelet : cube3.getFacelets().values()) {
            System.out.println(facelet);
        }
    }

    public byte[] getMoves(RotationEnum rotation) {
        reset();
        RotationModel rotationModel = getRotationModel(rotation, RotationModifierEnum.NORMAL, -1);
        rotationModel.apply();
        byte[] newPositions = new byte[48];
        for (Facelet facelet : cube3.getFacelets().values()) {
            Facelet3DEnum newPosition = Facelet3DEnum.of(facelet.getCurrentPosition());
            newPositions[facelet.getFacelet3DEnum().ordinal()] = (byte) newPosition.ordinal();
        }
        return newPositions;
    }

}

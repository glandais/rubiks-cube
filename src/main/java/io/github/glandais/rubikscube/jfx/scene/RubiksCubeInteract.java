package io.github.glandais.rubikscube.jfx.scene;

import io.github.glandais.rubikscube.model.FaceletDirectionEnum;
import io.github.glandais.rubikscube.model.FaceletRotationEnum;
import io.github.glandais.rubikscube.model.RotationEnum;
import io.github.glandais.rubikscube.model.SideEnum;
import io.github.glandais.rubikscube.model.Solver;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.SubScene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.input.ScrollEvent;
import javafx.util.Duration;
import lombok.Synchronized;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static io.github.glandais.rubikscube.model.RotationEnum.B;
import static io.github.glandais.rubikscube.model.RotationEnum.B_DOUBLE;
import static io.github.glandais.rubikscube.model.RotationEnum.B_REVERSE;
import static io.github.glandais.rubikscube.model.RotationEnum.D;
import static io.github.glandais.rubikscube.model.RotationEnum.D_DOUBLE;
import static io.github.glandais.rubikscube.model.RotationEnum.D_REVERSE;
import static io.github.glandais.rubikscube.model.RotationEnum.F;
import static io.github.glandais.rubikscube.model.RotationEnum.F_DOUBLE;
import static io.github.glandais.rubikscube.model.RotationEnum.F_REVERSE;
import static io.github.glandais.rubikscube.model.RotationEnum.L;
import static io.github.glandais.rubikscube.model.RotationEnum.L_DOUBLE;
import static io.github.glandais.rubikscube.model.RotationEnum.L_REVERSE;
import static io.github.glandais.rubikscube.model.RotationEnum.R;
import static io.github.glandais.rubikscube.model.RotationEnum.R_DOUBLE;
import static io.github.glandais.rubikscube.model.RotationEnum.R_REVERSE;
import static io.github.glandais.rubikscube.model.RotationEnum.U;
import static io.github.glandais.rubikscube.model.RotationEnum.U_DOUBLE;
import static io.github.glandais.rubikscube.model.RotationEnum.U_REVERSE;
import static io.github.glandais.rubikscube.model.RotationEnum.forNotation;
import static javafx.scene.transform.Rotate.X_AXIS;
import static javafx.scene.transform.Rotate.Y_AXIS;
import static javafx.scene.transform.Rotate.Z_AXIS;

public class RubiksCubeInteract {
    private final List<RotationPlayModel> rotationPlayModels = new ArrayList<>();
    private RubiksCubeView view;
    private double initDraggedX;
    private double initDraggedY;
    private double previousDraggedX;
    private double previousDraggedY;
    private FaceletClicked draggedInitial;
    private RotationDraggedModel rotationDraggedModel;
    private Point2D rotationDraggedVector;

    public void init() {
        view = new RubiksCubeView();
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

    public SubScene buildSubScene() {
        return view.buildSubScene();
    }

    public void setOnKeyPressed(KeyEvent ke) {
//        System.out.println("keyPressed " + ke);
    }

    public void setOnKeyTyped(KeyEvent ke) {
//        System.out.println("keyTyped " + ke);
    }

    public void onKeyReleased(KeyEvent ke) {
        if (ke.getCode() == KeyCode.F) {
            if (ke.isShiftDown()) {
                rotateUser(F_DOUBLE);
            } else if (ke.isAltDown()) {
                rotateUser(F_REVERSE);
            } else {
                rotateUser(F);
            }
        }
        if (ke.getCode() == KeyCode.B) {
            if (ke.isShiftDown()) {
                rotateUser(B_DOUBLE);
            } else if (ke.isAltDown()) {
                rotateUser(B_REVERSE);
            } else {
                rotateUser(B);
            }
        }
        if (ke.getCode() == KeyCode.U) {
            if (ke.isShiftDown()) {
                rotateUser(U_DOUBLE);
            } else if (ke.isAltDown()) {
                rotateUser(U_REVERSE);
            } else {
                rotateUser(U);
            }
        }
        if (ke.getCode() == KeyCode.D) {
            if (ke.isShiftDown()) {
                rotateUser(D_DOUBLE);
            } else if (ke.isAltDown()) {
                rotateUser(D_REVERSE);
            } else {
                rotateUser(D);
            }
        }
        if (ke.getCode() == KeyCode.R) {
            if (ke.isShiftDown()) {
                rotateUser(R_DOUBLE);
            } else if (ke.isAltDown()) {
                rotateUser(R_REVERSE);
            } else {
                rotateUser(R);
            }
        }
        if (ke.getCode() == KeyCode.L) {
            if (ke.isShiftDown()) {
                rotateUser(L_DOUBLE);
            } else if (ke.isAltDown()) {
                rotateUser(L_REVERSE);
            } else {
                rotateUser(L);
            }
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
        if (ke.getCode() == KeyCode.F3) {
            solve();
        }
//        System.out.println("keyReleased " + ke);
    }

    @Synchronized
    public void onMousePressed(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            draggedInitial = null;
            if (rotationPlayModels.isEmpty()) {
                PickResult pickResult = event.getPickResult();
                Node intersectedNode = pickResult.getIntersectedNode();
                if (intersectedNode instanceof FaceletBox faceletBox) {
                    Facelet facelet = faceletBox.getFacelet();
                    if (facelet.getCurrentPosition() != null) {
                        Point2D projected = getProjected(faceletBox, pickResult.getIntersectedPoint());
                        draggedInitial = new FaceletClicked(facelet, projected);
                    }
                }
            }
            initDraggedX = event.getSceneX();
            initDraggedY = event.getSceneY();
            previousDraggedX = event.getSceneX();
            previousDraggedY = event.getSceneY();
        }
    }

    private Point2D getProjected(FaceletBox faceletBox, Point3D intersectedPoint) {
        // Facelet
        intersectedPoint = faceletBox.localToParent(intersectedPoint);
        // Cube
        intersectedPoint = faceletBox.getFacelet().localToParent(intersectedPoint);
        // Cube3
        intersectedPoint = faceletBox.getFacelet().getCube().localToParent(intersectedPoint);
        SideEnum side = faceletBox.getFacelet().getCurrentPosition().getSide();
        Point3D xAxis = switch (side) {
            case F -> X_AXIS;
            case B -> X_AXIS.multiply(-1);
            case U -> X_AXIS;
            case D -> X_AXIS;
            case R -> Z_AXIS.multiply(-1);
            case L -> Z_AXIS;
        };
        double x = xAxis.dotProduct(intersectedPoint);
        Point3D yAxis =  switch (side) {
            case F -> Y_AXIS;
            case B -> Y_AXIS;
            case U -> Z_AXIS.multiply(-1);
            case D -> Z_AXIS;
            case R -> Y_AXIS;
            case L -> Y_AXIS;
        };
        double y = yAxis.dotProduct(intersectedPoint);
        return new Point2D(x, y);
    }

    public void onMouseDragged(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            double sceneX = event.getSceneX();
            double sceneY = event.getSceneY();

            double dx = sceneX - initDraggedX;
            double dy = sceneY - initDraggedY;

            double dxSinceLast = sceneX - previousDraggedX;
            double dySinceLast = sceneY - previousDraggedY;

            if (draggedInitial == null) {
                view.dRotate(dySinceLast, dxSinceLast);
            } else {
                if (rotationDraggedModel == null && Math.hypot(dx, dy) > 3.0) {
                    PickResult pickResult = event.getPickResult();
                    Node intersectedNode = pickResult.getIntersectedNode();
                    if (intersectedNode instanceof FaceletBox faceletBox) {
                        Facelet facelet = faceletBox.getFacelet();
                        if (facelet.getCurrentPosition() == draggedInitial.facelet().getCurrentPosition()) {
                            Point2D projected = getProjected(faceletBox, pickResult.getIntersectedPoint());
                            Point2D point2D = projected.subtract(draggedInitial.intersectedPoint());
                            RotationEnum rotation = getRotation(draggedInitial, point2D);
                            if (rotation != null) {
                                rotationDraggedModel = new RotationDraggedModel(view.getCube3(), rotation);
                                rotationDraggedVector = new Point2D(dxSinceLast, dySinceLast).normalize();
                            } else {
                                draggedInitial = null;
                            }
                        }
                    } else {
                        draggedInitial = null;
                    }
                }

                if (rotationDraggedModel != null) {
                    double ratio = rotationDraggedVector.dotProduct(
                            new Point2D(sceneX - initDraggedX, sceneY - initDraggedY)
                    ) / 50.0;
                    rotationDraggedModel.rotate(ratio);
                }
            }

            previousDraggedX = sceneX;
            previousDraggedY = sceneY;
        }
    }

    private RotationEnum getRotation(FaceletClicked draggedInitial, Point2D point2D) {
        FaceletDirectionEnum direction;
        if (Math.abs(point2D.getX()) > Math.abs(point2D.getY())) {
            if (point2D.getX() > 0) {
                direction = FaceletDirectionEnum.RIGHT;
            } else {
                direction = FaceletDirectionEnum.LEFT;
            }
        } else {
            if (point2D.getY() > 0) {
                direction = FaceletDirectionEnum.UP;
            } else {
                direction = FaceletDirectionEnum.DOWN;
            }
        }
//        System.out.println(point2D);
//        System.out.println(draggedInitial.facelet().getCurrentPosition());
//        System.out.println(direction);
        RotationEnum rotation = FaceletRotationEnum.getRotation(draggedInitial.facelet().getCurrentPosition(), direction);
//        System.out.println(rotation);
        return rotation;
    }

    public void onMouseReleased(MouseEvent mouseEvent) {
        if (rotationDraggedModel != null) {
            double currentAngle = rotationDraggedModel.getCurrentAngle();
            while (currentAngle < -180) {
                currentAngle = currentAngle + 360;
            }
            while (currentAngle >= 180) {
                currentAngle = currentAngle - 360;
            }
            if (currentAngle < -135) {
                currentAngle = -180;
            } else if (currentAngle < -45) {
                currentAngle = -90;
            } else if (currentAngle < 45) {
                currentAngle = 0;
            } else if (currentAngle < 135) {
                currentAngle = 90;
            } else {
                currentAngle = -180;
            }
            rotationDraggedModel.cancel();
            if (currentAngle != 0) {
                RotationEnum rotationEnum = null;
                for (RotationEnum value : RotationEnum.values()) {
                    if (
                            value.getAxis().equals(rotationDraggedModel.getAxis()) &&
                            value.getAngle() == currentAngle &&
                            Objects.equals(value.getX(), rotationDraggedModel.getRotation().getX()) &&
                            Objects.equals(value.getY(), rotationDraggedModel.getRotation().getY()) &&
                            Objects.equals(value.getZ(), rotationDraggedModel.getRotation().getZ())
                    ) {
                        rotationEnum = value;
                    }
                }
                if (rotationEnum != null) {
                    rotate(rotationEnum, 0);
                }
            }
        }
        draggedInitial = null;
        rotationDraggedModel = null;
        rotationDraggedVector = null;
    }

    public void onMouseClicked(MouseEvent event) {
//            PickResult result = t.getPickResult();
//            Node intersectedNode = result.getIntersectedNode();
//            if (intersectedNode instanceof CubeFace cubeFace) {
//                System.out.println(cubeFace.x + " " + cubeFace.y + " " + cubeFace.z);
//            }
    }

    public void onScroll(ScrollEvent event) {
//        System.out.println("onScroll " + event);
        double v = 1000.0;
        double ratio = (v + event.getDeltaY()) / v;
        view.onZoom(ratio);
    }

    @Synchronized
    public void resetView() {
        view.resetView();
    }

    @Synchronized
    public void reset() {
        view.reset();
        rotationPlayModels.clear();
    }

    @Synchronized
    public void rotateUser(RotationEnum rotation) {
        if (rotationPlayModels.isEmpty()) {
            rotate(rotation, 100);
        }
    }

    @Synchronized
    public void rotate(RotationEnum rotation, long duration) {
        duration = Math.round(duration * Math.abs(rotation.getAngle()) / 90);
        RotationPlayModel rotationModel = new RotationPlayModel(view.getCube3(), rotation, duration);
        rotationPlayModels.add(rotationModel);
    }

    @Synchronized
    public void scramble() {
        reset();
        doScramble();
    }

    @Synchronized
    public void solve() {
        if (!rotationPlayModels.isEmpty()) {
            rotationPlayModels.getFirst().applyRotation();
        }
        rotationPlayModels.clear();
        Cube3 cube3 = view.getCube3();
        String moves = cube3.getRotations()
                .stream()
                .map(RotationEnum::getNotation)
                .collect(Collectors.joining(" "));
        doSolve(moves);
    }

    @Synchronized
    public void scrambleAndSolve() {
        reset();
        String scramble = doScramble();
        doSolve(scramble);
    }

    private String doScramble() {
        String moves = Solver.scramble();
        applyMoves(moves, 50);
        return moves;
    }

    private void doSolve(String moves) {
        String solution = Solver.solve(moves);
        applyMoves(solution, 150);
    }

    private void applyMoves(String moves, int duration) {
        for (String move : moves.split(" ")) {
            if (!move.isEmpty()) {
                RotationEnum rotationEnum = forNotation(move);
                rotate(rotationEnum, duration);
            }
        }
    }

    @Synchronized
    private void update(long ellapsed) {
        if (!rotationPlayModels.isEmpty()) {
            RotationPlayModel rotationPlayModel = rotationPlayModels.getFirst();
            if (!rotationPlayModel.tick()) {
                rotationPlayModels.removeFirst();
            }
        }
    }

    private void debug() {
        /*
        System.out.println(rotateXNormal);
        System.out.println(rotateYNormal);
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
         */
    }

}

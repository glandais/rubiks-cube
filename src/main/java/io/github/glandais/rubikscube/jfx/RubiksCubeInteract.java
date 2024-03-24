package io.github.glandais.rubikscube.jfx;

import io.github.glandais.rubikscube.jfx.model.FaceletDirectionEnum;
import io.github.glandais.rubikscube.jfx.model.FaceletRotationEnum;
import io.github.glandais.rubikscube.jfx.scene.Facelet;
import io.github.glandais.rubikscube.jfx.scene.FaceletBox;
import io.github.glandais.rubikscube.jfx.scene.FaceletClicked;
import io.github.glandais.rubikscube.jfx.scene.RotationDraggedModel;
import io.github.glandais.rubikscube.jfx.scene.RotationModel;
import io.github.glandais.rubikscube.jfx.scene.RotationPlayModel;
import io.github.glandais.rubikscube.jfx.scene.RubiksCubeView;
import io.github.glandais.rubikscube.model.Cube3Model;
import io.github.glandais.rubikscube.model.SideEnum;
import io.github.glandais.rubikscube.model.rotation.RotationEnum;
import io.github.glandais.rubikscube.model.view.CubeVisibleOrientation;
import io.github.glandais.rubikscube.solver.DummySolver;
import io.github.glandais.rubikscube.solver.DummySolverInstance;
import io.github.glandais.rubikscube.solver.Scrambler;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.input.ScrollEvent;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Synchronized;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static io.github.glandais.rubikscube.model.rotation.RotationEnum.B;
import static io.github.glandais.rubikscube.model.rotation.RotationEnum.B_DOUBLE;
import static io.github.glandais.rubikscube.model.rotation.RotationEnum.B_REVERSE;
import static io.github.glandais.rubikscube.model.rotation.RotationEnum.D;
import static io.github.glandais.rubikscube.model.rotation.RotationEnum.D_DOUBLE;
import static io.github.glandais.rubikscube.model.rotation.RotationEnum.D_REVERSE;
import static io.github.glandais.rubikscube.model.rotation.RotationEnum.F;
import static io.github.glandais.rubikscube.model.rotation.RotationEnum.F_DOUBLE;
import static io.github.glandais.rubikscube.model.rotation.RotationEnum.F_REVERSE;
import static io.github.glandais.rubikscube.model.rotation.RotationEnum.L;
import static io.github.glandais.rubikscube.model.rotation.RotationEnum.L_DOUBLE;
import static io.github.glandais.rubikscube.model.rotation.RotationEnum.L_REVERSE;
import static io.github.glandais.rubikscube.model.rotation.RotationEnum.R;
import static io.github.glandais.rubikscube.model.rotation.RotationEnum.R_DOUBLE;
import static io.github.glandais.rubikscube.model.rotation.RotationEnum.R_REVERSE;
import static io.github.glandais.rubikscube.model.rotation.RotationEnum.U;
import static io.github.glandais.rubikscube.model.rotation.RotationEnum.U_DOUBLE;
import static io.github.glandais.rubikscube.model.rotation.RotationEnum.U_REVERSE;
import static javafx.scene.transform.Rotate.X_AXIS;
import static javafx.scene.transform.Rotate.Y_AXIS;
import static javafx.scene.transform.Rotate.Z_AXIS;

public class RubiksCubeInteract {
    private final Cube3Model cube3Model = new Cube3Model();
    private final DummySolver dummySolver = new DummySolver();

    private final List<RotationModel> rotationModels = new ArrayList<>();
    private RubiksCubeView view;
    @Getter
    private Label xRotationLabel;
    @Getter
    private Label yRotationLabel;
    private double initDraggedX;
    private double initDraggedY;
    private double previousDraggedX;
    private double previousDraggedY;
    private FaceletClicked draggedInitial;
    private RotationDraggedModel rotationDraggedModel;
    private Point2D rotationDraggedVector;
    private double rotationDraggedStartX;
    private double rotationDraggedStartY;

    private int historyIndex;
    private List<String> history;

    private boolean exploding = false;
    private long explodingStart;

    @Synchronized
    public void init() {
        view = new RubiksCubeView();
        xRotationLabel = new Label();
        yRotationLabel = new Label();
    }

    @Synchronized
    public void start() {
        resetView();
        doScramble(0);
        // The main game loop
        Timeline gameLoop = new Timeline();
        gameLoop.setCycleCount(Animation.INDEFINITE);
        KeyFrame keyFrame = new KeyFrame(
                Duration.seconds(1.0f / 60.0f), // 60 FPS
                ae -> update());
        gameLoop.getKeyFrames().add(keyFrame);
        gameLoop.play();
    }

    @Synchronized
    public SubScene buildSubScene() {
        return view.buildSubScene();
    }

    @Synchronized
    public void setOnKeyPressed(KeyEvent ke) {
//        System.out.println("keyPressed " + ke);
    }

    @Synchronized
    public void setOnKeyTyped(KeyEvent ke) {
//        System.out.println("keyTyped " + ke);
    }

    @Synchronized
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
        if (ke.getCode() == KeyCode.F2) {
            scramble();
        }
//        System.out.println("keyReleased " + ke);
    }

    @Synchronized
    public void onMousePressed(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            draggedInitial = null;
            if (rotationModels.isEmpty() && !exploding) {
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

    @Synchronized
    public void onMouseDragged(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            double sceneX = event.getSceneX();
            double sceneY = event.getSceneY();

            double dx = sceneX - initDraggedX;
            double dy = sceneY - initDraggedY;

            double dxSinceLast = sceneX - previousDraggedX;
            double dySinceLast = sceneY - previousDraggedY;

            if (draggedInitial == null) {
                rotateView(dySinceLast, dxSinceLast);
            } else {
                if (rotationDraggedModel == null && Math.hypot(dx, dy) > 5.0) {
                    PickResult pickResult = event.getPickResult();
                    Node intersectedNode = pickResult.getIntersectedNode();
                    if (intersectedNode instanceof FaceletBox faceletBox) {
                        Facelet facelet = faceletBox.getFacelet();
                        if (facelet.getCurrentPosition() == draggedInitial.facelet().getCurrentPosition()) {
                            Point2D projected = getProjected(faceletBox, pickResult.getIntersectedPoint());
                            Point2D point2D = projected.subtract(draggedInitial.intersectedPoint());
                            RotationEnum rotation = getRotation(draggedInitial, point2D);
                            if (rotation != null) {
                                rotationDraggedModel = new RotationDraggedModel(view.getCube3(), rotation, 300);
                                rotationDraggedVector = new Point2D(dx, dy).normalize();
                                rotationDraggedStartX = sceneX;
                                rotationDraggedStartY = sceneY;
                            }
                        }
                    }
                }

                if (rotationDraggedModel != null) {
                    double ratio = (rotationDraggedVector.dotProduct(
                            new Point2D(sceneX - rotationDraggedStartX, sceneY - rotationDraggedStartY)
                    )) / 50.0;
                    rotationDraggedModel.rotate(ratio);
                }
            }

            previousDraggedX = sceneX;
            previousDraggedY = sceneY;
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
        Point3D yAxis = switch (side) {
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

    private void rotateView(double dySinceLast, double dxSinceLast) {
        view.rotate(dySinceLast, dxSinceLast);
        xRotationLabel.setText("rotX : " + Math.round(view.getAngleX()));
        yRotationLabel.setText("rotY : " + Math.round(view.getAngleY()));
    }

    private CubeVisibleOrientation getCubeVisibleOrientation() {
        Map<SideEnum, Point3D> points = Map.of(
                SideEnum.F, new Point3D(0, 0, -1),
                SideEnum.B, new Point3D(0, 0, 1),
                SideEnum.U, new Point3D(0, -1, 0),
                SideEnum.D, new Point3D(0, 1, 0),
                SideEnum.L, new Point3D(1, 0, 0),
                SideEnum.R, new Point3D(-1, 0, 0)
        );
        SideEnum f = null;
        double fc = -2.0;
        SideEnum r = null;
        double rc = 2.0;
        SideEnum u = null;
        double uc = -2.0;
        for (Map.Entry<SideEnum, Point3D> entry : points.entrySet()) {
            Point3D projected = view.getCube3().localToScene(entry.getValue());
            if (projected.getZ() > fc) {
                f = entry.getKey();
                fc = projected.getZ();
            }
            if (projected.getX() < rc) {
                r = entry.getKey();
                rc = projected.getX();
            }
            if (projected.getY() > uc) {
                u = entry.getKey();
                uc = projected.getY();
            }
        }
        return new CubeVisibleOrientation(f, r, u);
    }

    private RotationEnum getRotation(FaceletClicked draggedInitial, Point2D point2D) {
        FaceletDirectionEnum direction = null;
        double angle = Math.atan2(point2D.getX(), point2D.getY()) * 180.0 / Math.PI;
        if (-30 < angle && angle < 30) {
            direction = FaceletDirectionEnum.UP;
        } else if (60 < angle && angle < 120) {
            direction = FaceletDirectionEnum.RIGHT;
        } else if (-120 < angle && angle < -60) {
            direction = FaceletDirectionEnum.LEFT;
        } else if (angle < -150 || angle > 150) {
            direction = FaceletDirectionEnum.DOWN;
        }
        return FaceletRotationEnum.getRotation(draggedInitial.facelet().getCurrentPosition(), direction);
    }

    @Synchronized
    public void onMouseReleased(MouseEvent event) {
//        System.out.println(event);
        if (event.getButton() == MouseButton.BACK) {
            undo();
        } else if (event.getButton() == MouseButton.FORWARD) {
            redo();
        } else if (event.getButton() == MouseButton.PRIMARY) {
            if (rotationDraggedModel != null) {
                double targetAngle = 90 * Math.round(rotationDraggedModel.getCurrentAngle() / 90.0);
                double currentAngle = targetAngle;
                while (currentAngle < -180) {
                    currentAngle = currentAngle + 360;
                }
                while (currentAngle >= 180) {
                    currentAngle = currentAngle - 360;
                }
                RotationEnum rotationEnum = null;
                if (currentAngle != 0) {
                    for (RotationEnum value : RotationEnum.values()) {
                        if (
                                value.getAxis().equals(rotationDraggedModel.getAxis()) &&
                                (
                                        value.getAngle() == currentAngle ||
                                        (
                                                Math.abs(value.getAngle()) == 180 &&
                                                Math.abs(value.getAngle()) == Math.abs(currentAngle)
                                        )
                                ) &&
                                Objects.equals(value.getX(), rotationDraggedModel.getRotation().getX()) &&
                                Objects.equals(value.getY(), rotationDraggedModel.getRotation().getY()) &&
                                Objects.equals(value.getZ(), rotationDraggedModel.getRotation().getZ())
                        ) {
                            rotationEnum = value;
                        }
                    }
                }

                rotationDraggedModel.released(rotationEnum, targetAngle);
                if (rotationEnum != null) {
                    historyIndex++;
                    history = history.subList(0, historyIndex);
                    history.add(rotationEnum.getNotation());
                }
                rotationModels.add(rotationDraggedModel);
            }
            draggedInitial = null;
            rotationDraggedModel = null;
        }
    }

    @Synchronized
    public void onMouseClicked(MouseEvent event) {
//            PickResult result = t.getPickResult();
//            Node intersectedNode = result.getIntersectedNode();
//            if (intersectedNode instanceof CubeFace cubeFace) {
//                System.out.println(cubeFace.x + " " + cubeFace.y + " " + cubeFace.z);
//            }
    }

    @Synchronized
    public void onScroll(ScrollEvent event) {
//        System.out.println("onScroll " + event);
        double v = 1000.0;
        double ratio = (v + event.getDeltaY()) / v;
        view.onZoom(ratio);
    }

    @Synchronized
    public void resetView() {
        if (!exploding) {
            view.resetView();
            rotateView(0, 0);
        }
    }

    @Synchronized
    public void reset() {
        if (exploding) {
            return;
        }
        clearRotationModels();
        view.reset();
        cube3Model.reset();
        historyIndex = -1;
        history = new ArrayList<>();
    }

    private void clearRotationModels() {
        if (!rotationModels.isEmpty()) {
            rotationModels.getFirst().cancel();
        }
        rotationModels.clear();
    }

    @Synchronized
    public void rotateUser(RotationEnum rotation) {
        if (exploding) {
            return;
        }
        applyMoves(rotation.getNotation(), true, true, 100);
    }

    @Synchronized
    public void scramble() {
        if (exploding) {
            return;
        }
        doScramble(50);
    }

    @Synchronized
    public void solveDummy() {
        if (exploding) {
            return;
        }
        doSolve(cube3Model.getNotation());
    }

    private String doScramble(int duration) {
        reset();
        String moves = Scrambler.scramble();
        System.out.println("Scramble : " + moves);
        applyMoves(moves, false, false, duration);
        return moves;
    }

    private void doSolve(String moves) {
        clearRotationModels();
        String solution = dummySolver.solve(moves);
        applyMoves(solution, false, true, 50);
    }

    @Synchronized
    public void applyMoves(String moves, boolean fromView, boolean addHistory, int duration90) {
        if (exploding) {
            return;
        }
        CubeVisibleOrientation cubeVisibleOrientation = null;
        if (fromView) {
            cubeVisibleOrientation = getCubeVisibleOrientation();
        }
        List<RotationEnum> rotationEnums = RotationEnum.parse(moves, cubeVisibleOrientation);
        if (rotationEnums.isEmpty()) {
            return;
        }
        for (RotationEnum rotation : rotationEnums) {
            RotationPlayModel rotationModel = new RotationPlayModel(view.getCube3(), rotation, duration90);
            rotationModels.add(rotationModel);
        }
        if (addHistory) {
            historyIndex++;
            history = history.subList(0, historyIndex);
            history.add(moves);
        }
    }

    @Synchronized
    private void update() {
        if (exploding) {
            if (view.explode(System.currentTimeMillis() - explodingStart)) {
                exploding = false;
            }
        } else if (!rotationModels.isEmpty()) {
            RotationModel rotationModel = rotationModels.getFirst();
            if (rotationModel.tick()) {
                RotationEnum rotation = rotationModel.getRotation();
                if (rotation != null) {
                    cube3Model.apply(rotation, false);
                }
                rotationModels.removeFirst();
//                if (rotationModels.isEmpty() && cube3Model.isSolved()) {
//                    doExplode();
//                }
                update();
            }
        }
    }

    @Synchronized
    public void undo() {
        if (rotationModels.isEmpty() && historyIndex >= 0 && !exploding) {
            String moves = history.get(historyIndex);
            historyIndex--;
            List<RotationEnum> rotationEnums = RotationEnum.parse(moves, null);
            String reversedMoves = rotationEnums.reversed()
                    .stream()
                    .map(RotationEnum::reverse)
                    .map(RotationEnum::getNotation)
                    .collect(Collectors.joining(" "));
            applyMoves(reversedMoves, false, false, 100);
        }
    }

    @Synchronized
    public void redo() {
        if (rotationModels.isEmpty() && historyIndex + 1 < history.size() && !exploding) {
            historyIndex++;
            String moves = history.get(historyIndex);
            applyMoves(moves, false, false, 100);
        }
    }

    @Synchronized
    public void solvePhase1() {
        solvePhase(1);
    }

    @Synchronized
    public void solvePhase2() {
        solvePhase(2);
    }

    @Synchronized
    public void solvePhase3() {
        solvePhase(3);
    }

    @Synchronized
    public void solvePhase4() {
        solvePhase(4);
    }

    @Synchronized
    public void solvePhase5() {
        solvePhase(5);
    }

    @Synchronized
    public void solvePhase6() {
        solvePhase(6);
    }

    @Synchronized
    public void solvePhase7() {
        solvePhase(7);
    }

    private void solvePhase(int toPhase) {
        if (exploding) {
            return;
        }
        clearRotationModels();
        String state = cube3Model.getNotation();
        DummySolverInstance dummySolverInstance = new DummySolverInstance(state);
        dummySolverInstance.solve(toPhase);
        applyMoves(dummySolverInstance.getMovesNotation(), false, true, 50);
    }

    @Synchronized
    public void explode() {
        if (!exploding) {
            doExplode();
        }
    }

    private void doExplode() {
        view.initExplode();
        explodingStart = System.currentTimeMillis();
        exploding = true;
    }
}

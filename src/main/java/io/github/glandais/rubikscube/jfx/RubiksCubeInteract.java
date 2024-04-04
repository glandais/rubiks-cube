package io.github.glandais.rubikscube.jfx;

import io.github.glandais.rubikscube.jfx.model.FaceletDirectionEnum;
import io.github.glandais.rubikscube.jfx.model.FaceletRotationEnum;
import io.github.glandais.rubikscube.jfx.model.Moves;
import io.github.glandais.rubikscube.jfx.model.TreeViewItem;
import io.github.glandais.rubikscube.jfx.model.TreeViewMove;
import io.github.glandais.rubikscube.jfx.model.TreeViewMoves;
import io.github.glandais.rubikscube.jfx.scene.ActionModel;
import io.github.glandais.rubikscube.jfx.scene.Facelet;
import io.github.glandais.rubikscube.jfx.scene.FaceletBox;
import io.github.glandais.rubikscube.jfx.scene.FaceletClicked;
import io.github.glandais.rubikscube.jfx.scene.RotationDraggedModel;
import io.github.glandais.rubikscube.jfx.scene.RotationPlayModel;
import io.github.glandais.rubikscube.jfx.scene.RubiksCubeView;
import io.github.glandais.rubikscube.jfx.scene.ViewModel;
import io.github.glandais.rubikscube.model.Action;
import io.github.glandais.rubikscube.model.SideEnum;
import io.github.glandais.rubikscube.model.rotation.RotationEnum;
import io.github.glandais.rubikscube.model.rotation.RotationModifierEnum;
import io.github.glandais.rubikscube.model.view.CubeVisibleOrientation;
import io.github.glandais.rubikscube.model.view.ViewEnum;
import io.github.glandais.rubikscube.solver.DummySolverInstance;
import io.github.glandais.rubikscube.solver.Scrambler;
import io.github.glandais.rubikscube.solver.SolveMoves;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
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

    private final List<ActionModel> actionModels = new ArrayList<>();
    private RubiksCubeView view;
    @Getter
    private Label xRotationLabel;
    @Getter
    private Label yRotationLabel;
    @Getter
    private Label groupLabel;
    @Getter
    private TreeView<TreeViewItem> treeView;
    private TreeItem<TreeViewItem> treeViewRoot;
    private double initDraggedX;
    private double initDraggedY;
    private double previousDraggedX;
    private double previousDraggedY;
    private FaceletClicked draggedInitial;
    private RotationDraggedModel rotationDraggedModel;
    private Point2D rotationDraggedVector;
    private double rotationDraggedStartX;
    private double rotationDraggedStartY;

    private boolean exploding = false;
    private long explodingStart;
    @Getter
    private BooleanProperty disable;

    @Synchronized
    public void init() {
        view = new RubiksCubeView();
        xRotationLabel = new Label();
        yRotationLabel = new Label();
        treeViewRoot = new TreeItem<>(null);
        treeViewRoot.setExpanded(true);
        groupLabel = new Label("");
        treeView = new TreeView<>(treeViewRoot);
        treeView.setShowRoot(false);
        treeView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        treeView.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> nodeSelected(oldValue, newValue));

        disable = new SimpleBooleanProperty();
        disable.bindBidirectional(treeView.disableProperty());
    }

    @Synchronized
    protected void nodeSelected(TreeItem<TreeViewItem> oldValue, TreeItem<TreeViewItem> newValue) {
        if (newValue != null) {
            if (newValue.getParent().equals(treeViewRoot)) {
                groupLabel.setText(newValue.getValue().toString());
            } else {
                groupLabel.setText(newValue.getParent().getValue().toString());
            }
        }
        List<Action> newActions = getActions(newValue);
        ViewEnum lastRotation = null;
        for (Action action : newActions) {
            if (action instanceof ViewEnum viewEnum) {
                lastRotation = viewEnum;
            }
        }
        List<Action> oldActions = getActions(oldValue);
        if (oldActions.size() > newActions.size()) {
            List<Action> subList = oldActions.subList(newActions.size(), oldActions.size());
            String moves = subList
                    .reversed()
                    .stream()
                    .filter(a -> !(a instanceof ViewEnum))
                    .map(Action::reverse)
                    .map(Action::getNotation)
                    .collect(Collectors.joining(" "));
            applyNodeSelectedMoves(lastRotation, moves);
        } else if (oldActions.size() < newActions.size()) {
            List<Action> subList = newActions.subList(oldActions.size(), newActions.size());
            String moves = subList
                    .stream()
                    .map(Action::getNotation)
                    .collect(Collectors.joining(" "));
            applyNodeSelectedMoves(lastRotation, moves);
        }
    }

    private void applyNodeSelectedMoves(ViewEnum lastRotation, String moves) {
        List<Action> actions = Action.parse(moves, null);
        if (lastRotation != null) {
            actions.addFirst(lastRotation);
        }
        if (actions.isEmpty()) {
            return;
        }
        int duration90 = Math.max(20, Math.min(100, 1000 / actions.size()));
        for (Action action : actions) {
            if (action instanceof RotationEnum rotationEnum) {
                RotationPlayModel rotationModel = new RotationPlayModel(view.getCube3(), rotationEnum, duration90);
                actionModels.add(rotationModel);
            } else if (action instanceof ViewEnum viewEnum) {
                actionModels.add(new ViewModel(view.getRotateX(), view.getRotateY(), viewEnum, duration90));
            }
        }
    }

    private List<Action> getActions(TreeItem<TreeViewItem> treeItem) {
        if (treeItem == null) {
            return List.of();
        }
        List<Action> actions = new ArrayList<>();
        for (TreeItem<TreeViewItem> child : treeViewRoot.getChildren()) {
            if (child.equals(treeItem)) {
                actions.addAll(child.getValue().getActions());
                return actions;
            }
            for (TreeItem<TreeViewItem> childChild : child.getChildren()) {
                actions.addAll(childChild.getValue().getActions());
                if (childChild.equals(treeItem)) {
                    return actions;
                }
            }
        }
        return actions;
    }

    @Synchronized
    public void start() {
        resetView();
        doScramble();
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
    public void onKeyReleased(KeyEvent ke) {
        if (ke.getCode() == KeyCode.F) {
            if (ke.isShiftDown()) {
                rotateKeyboard(F_DOUBLE);
            } else if (ke.isAltDown()) {
                rotateKeyboard(F_REVERSE);
            } else {
                rotateKeyboard(F);
            }
        }
        if (ke.getCode() == KeyCode.B) {
            if (ke.isShiftDown()) {
                rotateKeyboard(B_DOUBLE);
            } else if (ke.isAltDown()) {
                rotateKeyboard(B_REVERSE);
            } else {
                rotateKeyboard(B);
            }
        }
        if (ke.getCode() == KeyCode.U) {
            if (ke.isShiftDown()) {
                rotateKeyboard(U_DOUBLE);
            } else if (ke.isAltDown()) {
                rotateKeyboard(U_REVERSE);
            } else {
                rotateKeyboard(U);
            }
        }
        if (ke.getCode() == KeyCode.D) {
            if (ke.isShiftDown()) {
                rotateKeyboard(D_DOUBLE);
            } else if (ke.isAltDown()) {
                rotateKeyboard(D_REVERSE);
            } else {
                rotateKeyboard(D);
            }
        }
        if (ke.getCode() == KeyCode.R) {
            if (ke.isShiftDown()) {
                rotateKeyboard(R_DOUBLE);
            } else if (ke.isAltDown()) {
                rotateKeyboard(R_REVERSE);
            } else {
                rotateKeyboard(R);
            }
        }
        if (ke.getCode() == KeyCode.L) {
            if (ke.isShiftDown()) {
                rotateKeyboard(L_DOUBLE);
            } else if (ke.isAltDown()) {
                rotateKeyboard(L_REVERSE);
            } else {
                rotateKeyboard(L);
            }
        }
        if (ke.getCode() == KeyCode.ESCAPE) {
            reset();
        }
        if (ke.getCode() == KeyCode.F2) {
            scramble();
        }
    }

    @Synchronized
    protected void rotateKeyboard(RotationEnum rotation) {
        if (disable.get()) {
            return;
        }
        applyMoves("Keyboard", rotation.getNotation(), true);
    }

    @Synchronized
    public void onMousePressed(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            draggedInitial = null;
            if (!disable.get()) {
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
        if (event.getButton() == MouseButton.BACK) {
            if (!disable.get()) {
                goToGroupPrevious();
            }
        } else if (event.getButton() == MouseButton.FORWARD) {
            if (!disable.get()) {
                goToGroupNext();
            }
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
                actionModels.add(rotationDraggedModel);
            }
            draggedInitial = null;
            rotationDraggedModel = null;
        }
    }

    @Synchronized
    public void onScroll(ScrollEvent event) {
        double v = 1000.0;
        double ratio = (v + event.getDeltaY()) / v;
        view.onZoom(ratio);
    }

    @Synchronized
    public void resetView() {
        view.resetView();
        rotateView(0, 0);
    }

    @Synchronized
    public void reset() {
        clearRotationModels();
        disable.set(false);
        view.reset();
        treeViewRoot.getChildren().clear();
        exploding = false;
    }

    private void clearRotationModels() {
        if (!actionModels.isEmpty()) {
            actionModels.getFirst().cancel();
        }
        actionModels.clear();
    }

    @Synchronized
    public void scramble() {
        doScramble();
    }

    private void doScramble() {
        reset();
        String moves = "fru " + Scrambler.scramble();
        System.out.println("Scramble : " + moves);
        applyMoves("Scramble", moves, false);
    }

    @Synchronized
    public void applyMoves(String desc, String moves, boolean fromView) {
        CubeVisibleOrientation cubeVisibleOrientation = null;
        if (fromView) {
            cubeVisibleOrientation = getCubeVisibleOrientation();
        }
        List<Action> actions = Action.parse(moves, cubeVisibleOrientation);
        applyMoves(List.of(new Moves(desc, actions)), true);
    }

    @Synchronized
    public void applyMoves(List<Moves> movesList, boolean select) {
        if (movesList.isEmpty()) {
            return;
        }

        // remove actions after current selection
        TreeItem<TreeViewItem> selectedItem = treeView.getSelectionModel().getSelectedItem();
        boolean found = false;
        List<Runnable> removes = new ArrayList<>();
        for (TreeItem<TreeViewItem> child : treeViewRoot.getChildren()) {
            if (found) {
                removes.add(() -> treeViewRoot.getChildren().remove(child));
            } else if (child.equals(selectedItem)) {
                found = true;
            } else {
                for (TreeItem<TreeViewItem> childChild : child.getChildren()) {
                    if (found) {
                        removes.add(() -> child.getChildren().remove(childChild));
                    } else if (childChild.equals(selectedItem)) {
                        found = true;
                    }
                }
            }
        }
        for (Runnable remove : removes) {
            remove.run();
        }
        TreeItem<TreeViewItem> last = null;

        for (Moves moves : movesList) {
            List<Action> actions = moves.actions();
            actions = simplify(actions);
            boolean oneMove = false;
            for (Action action : actions) {
                if (action instanceof RotationEnum) {
                    oneMove = true;
                    break;
                }
            }
            if (oneMove) {
                TreeViewMoves treeViewMoves = new TreeViewMoves(moves.desc());
                TreeItem<TreeViewItem> moveGroup = new TreeItem<>(treeViewMoves);
                treeViewMoves.setTreeItem(moveGroup);
                moveGroup.setExpanded(true);
                for (Action action : actions) {
                    TreeItem<TreeViewItem> move = new TreeItem<>(new TreeViewMove(action));
                    moveGroup.getChildren().add(move);
                    last = move;
                }
                treeViewRoot.getChildren().add(moveGroup);
            }
        }

        if (select && last != null) {
            treeView.scrollTo(treeView.getRow(last));
            treeView.getSelectionModel().select(last);
        }
    }

    private List<Action> simplify(List<Action> actions) {
        boolean modified = true;
        while (modified) {
            List<Action> newActions = new ArrayList<>();
            for (int i = 0; i < actions.size(); i++) {
                Action action = actions.get(i);
                boolean add = true;
                if (i != actions.size() - 1) {
                    Action next = actions.get(i + 1);
                    if (action instanceof ViewEnum && next instanceof ViewEnum) {
                        add = false;
                    } else if (action instanceof RotationEnum r1 && next instanceof RotationEnum r2) {
                        if (r1.getSide() == r2.getSide()) {
                            add = false;
                            RotationModifierEnum modifier = switch (r1.getModifier()) {
                                case NORMAL -> switch (r2.getModifier()) {
                                    case NORMAL -> RotationModifierEnum.DOUBLE;
                                    case REVERSE -> null;
                                    case DOUBLE -> RotationModifierEnum.REVERSE;
                                };
                                case REVERSE -> switch (r2.getModifier()) {
                                    case NORMAL -> null;
                                    case REVERSE -> RotationModifierEnum.DOUBLE;
                                    case DOUBLE -> RotationModifierEnum.NORMAL;
                                };
                                case DOUBLE -> switch (r2.getModifier()) {
                                    case NORMAL -> RotationModifierEnum.REVERSE;
                                    case REVERSE -> RotationModifierEnum.NORMAL;
                                    case DOUBLE -> null;
                                };
                            };
                            if (modifier != null) {
                                RotationEnum rotationEnum = RotationEnum.getRotationEnum(r1.getSide(), modifier);
                                newActions.add(rotationEnum);
                            }
                            i++;
                        }
                    }
                }
                if (add) {
                    newActions.add(action);
                }
            }

            if (newActions.size() != actions.size()) {
                modified = true;
            } else {
                modified = false;
            }
            actions = newActions;
        }
        return actions;
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

    @Synchronized
    private void update() {
        disable.set(true);
        if (exploding) {
            if (view.explode(System.currentTimeMillis() - explodingStart)) {
                exploding = false;
            }
        } else if (!actionModels.isEmpty()) {
            ActionModel actionModel = actionModels.getFirst();
            if (actionModel.tick()) {
                actionModels.removeFirst();
                if (actionModel instanceof RotationDraggedModel rdm) {
                    if (rdm.getRotation() != null) {
                        applyMoves("Mouse", rdm.getRotation().getNotation(), false);
                        // already applied
                        if (!actionModels.isEmpty()) {
                            actionModels.removeFirst();
                        }
                    }
                }
                update();
            }
        } else {
            disable.set(false);
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
        if (disable.get()) {
            return;
        }
        clearRotationModels();
        DummySolverInstance dummySolverInstance = new DummySolverInstance(getCurrentMoves());
        dummySolverInstance.solve(toPhase);
        List<SolveMoves> solveMoves = dummySolverInstance.getMovesNotation();
        applySolution(solveMoves);
    }

    private void applySolution(List<SolveMoves> solveMoves) {
        List<Moves> moves = solveMoves.stream()
                .map(s -> new Moves(s.getPhase(), Action.parse(s.getMoves().toString(), null)))
                .toList();
        applyMoves(moves, false);
        int curGroupIndex = getCurGroupIndex();
        if (curGroupIndex < treeViewRoot.getChildren().size() - 1) {
            select(treeViewRoot.getChildren().get(curGroupIndex + 1));
        }
    }

    private String getCurrentMoves() {
        List<Action> actions = getActions(treeView.getSelectionModel().getSelectedItem());
        return actions.stream()
                .map(Action::getNotation)
                .collect(Collectors.joining(" "));
    }

    @Synchronized
    public void explode() {
        if (!disable.get()) {
            doExplode();
        }
    }

    private void doExplode() {
        view.initExplode();
        explodingStart = System.currentTimeMillis();
        exploding = true;
    }

    private void rotateView(double dySinceLast, double dxSinceLast) {
        view.rotate(dySinceLast, dxSinceLast);
        xRotationLabel.setText("rotX : " + Math.round(view.getAngleX()));
        yRotationLabel.setText("rotY : " + Math.round(view.getAngleY()));
    }

    public void goToStart() {
        if (treeViewRoot.getChildren().isEmpty()) {
            return;
        }
        select(treeViewRoot.getChildren().getFirst());
    }

    public void goToEnd() {
        if (treeViewRoot.getChildren().isEmpty()) {
            return;
        }
        selectLast(treeViewRoot.getChildren().getLast());
    }

    public void goToGroupPrevious() {
        if (treeViewRoot.getChildren().isEmpty()) {
            return;
        }
        int curIndex = getCurGroupIndex();
        if (curIndex > 0) {
            selectLast(treeViewRoot.getChildren().get(curIndex - 1));
        } else {
            select(treeViewRoot.getChildren().get(curIndex));
        }
    }

    public void goToGroupNext() {
        if (treeViewRoot.getChildren().isEmpty()) {
            return;
        }
        int curIndex = getCurGroupIndex();
        if (curIndex < treeViewRoot.getChildren().size() - 1) {
            selectLast(treeViewRoot.getChildren().get(curIndex + 1));
        } else {
            selectLast(treeViewRoot.getChildren().get(curIndex));
        }
    }

    private int getCurGroupIndex() {
        TreeItem<TreeViewItem> selectedItem = treeView.getSelectionModel().getSelectedItem();
        TreeItem<TreeViewItem> currentGroup;
        if (selectedItem.getParent().equals(treeViewRoot)) {
            currentGroup = selectedItem;
        } else {
            currentGroup = selectedItem.getParent();
        }
        return treeViewRoot.getChildren().indexOf(currentGroup);
    }

    public void goToActionPrevious() {
        if (treeViewRoot.getChildren().isEmpty()) {
            return;
        }
        TreeItem<TreeViewItem> selectedItem = getSelectedItemActionApplied();
        TreeItem<TreeViewItem> previous = treeViewRoot.getChildren().getFirst();
        for (TreeItem<TreeViewItem> child : treeViewRoot.getChildren()) {
            for (TreeItem<TreeViewItem> childChild : child.getChildren()) {
                if (childChild.equals(selectedItem)) {
                    select(previous);
                    return;
                }
                previous = childChild;
            }
        }
    }

    public void goToActionNext() {
        if (treeViewRoot.getChildren().isEmpty()) {
            return;
        }
        TreeItem<TreeViewItem> selectedItem = getSelectedItemActionApplied();
        boolean setNext = false;
        for (TreeItem<TreeViewItem> child : treeViewRoot.getChildren()) {
            if (child.equals(selectedItem)) {
                setNext = true;
            }
            for (TreeItem<TreeViewItem> childChild : child.getChildren()) {
                if (setNext) {
                    select(childChild);
                    return;
                } else if (childChild.equals(selectedItem)) {
                    setNext = true;
                }
            }
        }
    }

    private TreeItem<TreeViewItem> getSelectedItemActionApplied() {
        TreeItem<TreeViewItem> selectedItem = treeView.getSelectionModel().getSelectedItem();
        TreeItem<TreeViewItem> result = treeViewRoot.getChildren().getFirst();
        for (TreeItem<TreeViewItem> child : treeViewRoot.getChildren()) {
            if (selectedItem.equals(child)) {
                return result;
            }
            for (TreeItem<TreeViewItem> childChild : child.getChildren()) {
                result = childChild;
                if (selectedItem.equals(childChild)) {
                    return result;
                }
            }
        }
        return result;
    }

    private void selectLast(TreeItem<TreeViewItem> treeItem) {
        if (!treeItem.getChildren().isEmpty()) {
            select(treeItem.getChildren().getLast());
        } else {
            select(treeItem);
        }
    }

    private void select(TreeItem<TreeViewItem> treeItem) {
        if (!treeItem.getParent().equals(treeViewRoot)) {
            treeItem.getParent().setExpanded(true);
        }
        treeView.scrollTo(treeView.getRow(treeItem));
        treeView.getSelectionModel().select(treeItem);
    }

}

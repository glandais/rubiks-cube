package io.github.glandais.rubikscube.jfx;

import io.github.glandais.rubikscube.model.rotation.RotationEnum;
import io.github.glandais.rubikscube.model.view.ViewEnum;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.List;

public class RubiksCubeApplication extends Application {
    public static RubiksCubeInteract rubiksCubeInteract;

    public static void launchFromElseWhere(RubiksCubeInteract rubiksCubeInteract) {
        RubiksCubeApplication.rubiksCubeInteract = rubiksCubeInteract;
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        GridPane gridPane = new GridPane();

        int width = 1024;
        int height = 900;

        rubiksCubeInteract.init();
        BooleanProperty disable = rubiksCubeInteract.getDisable();

        ToolBar tbTop = new ToolBar();
        tbTop.setOrientation(Orientation.HORIZONTAL);
        tbTop.getItems().addAll(getTop(disable));
        gridPane.add(tbTop, 1, 0);

        ToolBar tbBottom = new ToolBar();
        tbBottom.setOrientation(Orientation.HORIZONTAL);
        tbBottom.getItems().addAll(getBottom());
        gridPane.add(tbBottom, 1, 2);

        ToolBar tbLeft = new ToolBar();
        tbLeft.setOrientation(Orientation.VERTICAL);
        tbLeft.getItems().addAll(getLeft(disable));
        gridPane.add(tbLeft, 0, 1);

        ToolBar tbRight = new ToolBar();
        tbRight.setOrientation(Orientation.VERTICAL);
        GridPane rightPane = new GridPane();
        VBox rightButtons = new VBox();
        rightButtons.getChildren().addAll(getRight(disable));

        HBox treeButtons = new HBox();
        treeButtons.getChildren().addAll(
                new ActionButton(disable, "<<<", "Start", rubiksCubeInteract::goToStart),
                new ActionButton(disable, "<<", "Previous group", rubiksCubeInteract::goToGroupPrevious),
                new ActionButton(disable, "<", "Previous", rubiksCubeInteract::goToActionPrevious),
                new ActionButton(disable, ">", "Next", rubiksCubeInteract::goToActionNext),
                new ActionButton(disable, ">>", "Next group", rubiksCubeInteract::goToGroupNext),
                new ActionButton(disable, ">>>", "End", rubiksCubeInteract::goToEnd)
        );

        rightPane.add(rubiksCubeInteract.getTreeView(), 0, 0);
        rightPane.add(treeButtons, 0, 1);

        rightPane.add(rightButtons, 1, 0);
        tbRight.getItems().addAll(rightPane);
        gridPane.add(tbRight, 2, 1);

        SubScene front = RubiksCubeApplication.rubiksCubeInteract.buildSubScene();
        front.setOnMousePressed(RubiksCubeApplication.rubiksCubeInteract::onMousePressed);
        front.setOnMouseDragged(RubiksCubeApplication.rubiksCubeInteract::onMouseDragged);
        front.setOnMouseReleased(RubiksCubeApplication.rubiksCubeInteract::onMouseReleased);
        front.setOnScroll(RubiksCubeApplication.rubiksCubeInteract::onScroll);
        gridPane.add(front, 1, 1);
        bindSize(front, gridPane, tbLeft, tbRight, tbTop, tbBottom);

        Scene scene = new Scene(gridPane, width, height, true);
        scene.setFill(Color.ALICEBLUE);
        scene.setOnKeyReleased(ke -> rubiksCubeInteract.onKeyReleased(ke));

        primaryStage.setScene(scene);
        primaryStage.setTitle("Rubik's Cube");
        primaryStage.setMaximized(true);
        primaryStage.show();

        rubiksCubeInteract.start();
    }

    private void bindSize(SubScene view, GridPane gridPane, ToolBar tbLeft, ToolBar tbRight, ToolBar tbTop, ToolBar tbBottom) {
        view.widthProperty().bind(
                gridPane.widthProperty()
                        .subtract(tbLeft.widthProperty())
                        .subtract(tbRight.widthProperty())
        );
        view.heightProperty().bind(
                gridPane.heightProperty()
                        .subtract(tbTop.heightProperty())
                        .subtract(tbBottom.heightProperty())
        );
    }

    private List<Node> getTop(BooleanProperty disable) {
        GridPane gridPane = new GridPane();
        gridPane.add(new RotationButton(disable, RotationEnum.L), 0, 0);
        gridPane.add(new RotationButton(disable, RotationEnum.L_DOUBLE), 0, 1);
        gridPane.add(new RotationButton(disable, RotationEnum.L_REVERSE), 0, 2);
        gridPane.add(new RotationButton(disable, RotationEnum.R), 4, 0);
        gridPane.add(new RotationButton(disable, RotationEnum.R_DOUBLE), 4, 1);
        gridPane.add(new RotationButton(disable, RotationEnum.R_REVERSE), 4, 2);
        gridPane.add(new RotationButton(disable, RotationEnum.U), 1, 0);
        gridPane.add(new RotationButton(disable, RotationEnum.U_DOUBLE), 2, 0);
        gridPane.add(new RotationButton(disable, RotationEnum.U_REVERSE), 3, 0);
        gridPane.add(new RotationButton(disable, RotationEnum.B), 1, 1);
        gridPane.add(new RotationButton(disable, RotationEnum.B_DOUBLE), 2, 1);
        gridPane.add(new RotationButton(disable, RotationEnum.B_REVERSE), 3, 1);
        gridPane.add(new RotationButton(disable, RotationEnum.F), 1, 2);
        gridPane.add(new RotationButton(disable, RotationEnum.F_DOUBLE), 2, 2);
        gridPane.add(new RotationButton(disable, RotationEnum.F_REVERSE), 3, 2);
        gridPane.add(new RotationButton(disable, RotationEnum.D), 1, 3);
        gridPane.add(new RotationButton(disable, RotationEnum.D_DOUBLE), 2, 3);
        gridPane.add(new RotationButton(disable, RotationEnum.D_REVERSE), 3, 3);

        Button undo = new ActionButton(disable, "Undo", "Undo last action group", RubiksCubeApplication.rubiksCubeInteract::goToGroupPrevious);
        gridPane.add(undo, 5, 0);
        Button redo = new ActionButton(disable,"Redo", "Redo last action group", RubiksCubeApplication.rubiksCubeInteract::goToGroupNext);
        gridPane.add(redo, 5, 1);

        Button reset = new ActionButton(null, "Reset", "Reset to a solved cube", RubiksCubeApplication.rubiksCubeInteract::reset);
        gridPane.add(reset, 6, 0);
        Button scramble = new ActionButton(null, "Scramble", "Randomize cube", RubiksCubeApplication.rubiksCubeInteract::scramble);
        gridPane.add(scramble, 6, 1);
        Button solveDummy = new ActionButton(disable, "Solve", "Solve cube", RubiksCubeApplication.rubiksCubeInteract::solveDummy);
        gridPane.add(solveDummy, 6, 2);
        Button explode = new ActionButton(disable, "Explode", "Explode cube", RubiksCubeApplication.rubiksCubeInteract::explode);
        gridPane.add(explode, 7, 0);

        gridPane.add(new RotationButton(disable, ViewEnum.fru), 8, 0);
        gridPane.add(new RotationButton(disable, ViewEnum.rbu), 8, 1);
        gridPane.add(new RotationButton(disable, ViewEnum.blu), 8, 2);
        gridPane.add(new RotationButton(disable, ViewEnum.lfu), 8, 3);
        gridPane.add(new RotationButton(disable, ViewEnum.fld), 9, 0);
        gridPane.add(new RotationButton(disable, ViewEnum.rfd), 9, 1);
        gridPane.add(new RotationButton(disable, ViewEnum.brd), 9, 2);
        gridPane.add(new RotationButton(disable, ViewEnum.lbd), 9, 3);

        return List.of(gridPane);
    }

    private List<Node> getBottom() {
        return List.of(
                new ActionButton(null, "Reset view", "Reset to initial view", rubiksCubeInteract::resetView),
                new Separator(),
                rubiksCubeInteract.getXRotationLabel(),
                rubiksCubeInteract.getYRotationLabel()
        );
    }

    private List<Node> getLeft(BooleanProperty disable) {
        return List.of(
                new Label("Step by step solving"),
                new Separator(),
                new ActionButton(disable, "White edges", "Phase 1", rubiksCubeInteract::solvePhase1),
                new ActionButton(disable, "First layer", "Phase 2", rubiksCubeInteract::solvePhase2),
                new ActionButton(disable, "Second layer", "Phase 3", rubiksCubeInteract::solvePhase3),
                new ActionButton(disable, "Yellow cross", "Phase 4", rubiksCubeInteract::solvePhase4),
                new ActionButton(disable, "Yellow edges", "Phase 5", rubiksCubeInteract::solvePhase5),
                new ActionButton(disable, "Yellow corners", "Phase 6", rubiksCubeInteract::solvePhase6),
                new ActionButton(disable, "Orient yellow corners", "Phase 7", rubiksCubeInteract::solvePhase7)
        );
    }

    private List<Node> getRight(BooleanProperty disable) {
        return List.of(
                new Label("Standard moves"),
                new Separator(),
                new LabelTooltip("White edges", "Phase 1"),
                new MovesButton(disable, "From top", "White edges - From top", "F U' R U"),
                new MovesButton(disable, "From bottom", "White edges - From bottom", "F' U' R U"),
                new MovesButton(disable, "From right", "White edges - From right", "U' R U"),
                new MovesButton(disable, "From left", "White edges - From left", "U L' U'"),
                new Separator(),
                new LabelTooltip("First layer", "Phase 2"),
                new MovesButton(disable, "From right", "First layer - From right", "R' D' R"),
                new MovesButton(disable, "From front", "First layer - From front", "F D F'"),
                new MovesButton(disable, "From down", "First layer - From down", "R2 D' R2 D R2"),
                new MovesButton(disable, "Left to down", "First layer - Left to down", "L D L'"),
                new Separator(),
                new LabelTooltip("Second layer", "Phase 3"),
                new MovesButton(disable, "Right", "Second layer - Right", "U R U' R' U' F' U F"),
                new MovesButton(disable, "Left", "Second layer - Left", "U' L' U L U F U' F'"),
                new MovesButton(disable, "Wrong orient", "Second layer - Wrong orient", "U R U' R' U' F' U F U2 U R U' R' U' F' U F"),
                new Separator(),
                new LabelTooltip("Yellow cross", "Phase 4"),
                new MovesButton(disable, "Fix", "Yellow cross - Fix", "F R U R' U' F'"),
                new MovesButton(disable, "Reversed", "Yellow cross - Reversed", "F U R U' R' F'"),
                new Separator(),
                new LabelTooltip("Yellow edges", "Phase 5"),
                new MovesButton(disable, "Switch", "Yellow edges - Switch", "R U R' U R U2 R' U"),
                new MovesButton(disable, "Opposite", "Yellow edges - Opposite", "U R U R' U R U2 R' U R U R' U R U2 R' U"),
                new Separator(),
                new LabelTooltip("Yellow corners", "Phase 6"),
                new MovesButton(disable, "Swap", "Yellow corners - Swap", "U R U' L' U R' U' L"),
                new Separator(),
                new LabelTooltip("Orient yellow corners", "Phase 7"),
                new MovesButton(disable, "Move", "Orient yellow corners - Move", "R' D' R D R' D' R D"),
                new Separator()
        );
    }

}

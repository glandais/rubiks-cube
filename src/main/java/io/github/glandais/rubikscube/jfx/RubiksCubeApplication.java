package io.github.glandais.rubikscube.jfx;

import io.github.glandais.rubikscube.jfx.scene.RubiksCubeInteract;
import io.github.glandais.rubikscube.model.RotationEnum;
import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.GridPane;
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

        int width = 800;
        int height = 600;

        rubiksCubeInteract.init();

        ToolBar tbTop = new ToolBar();
        tbTop.setOrientation(Orientation.HORIZONTAL);
        tbTop.getItems().addAll(getTop());
        gridPane.add(tbTop, 1, 0);

        ToolBar tbBottom = new ToolBar();
        tbBottom.setOrientation(Orientation.HORIZONTAL);
        tbBottom.getItems().addAll(getBottom());
        gridPane.add(tbBottom, 1, 2);

        ToolBar tbLeft = new ToolBar();
        tbLeft.setOrientation(Orientation.VERTICAL);
        tbLeft.getItems().addAll(getLeft());
        gridPane.add(tbLeft, 0, 1);

        ToolBar tbRight = new ToolBar();
        tbRight.setOrientation(Orientation.VERTICAL);
        tbRight.getItems().addAll(getRight());
        gridPane.add(tbRight, 2, 1);

        /*
        GridPane scenes = new GridPane();
        scenes.setOnMousePressed(RubiksCubeApplication.rubiksCubeInteract::onMousePressed);
        scenes.setOnMouseDragged(RubiksCubeApplication.rubiksCubeInteract::onMouseDragged);
        scenes.setOnMouseClicked(RubiksCubeApplication.rubiksCubeInteract::onMouseClicked);
        scenes.setOnScroll(RubiksCubeApplication.rubiksCubeInteract::onScroll);
        List<SubScene> subScenes = new ArrayList<>();
        SubScene front = RubiksCubeApplication.rubiksCubeInteract.buildSubScene(RubiksCubeViewEnum.F);
        scenes.add(front, 0, 0);
        subScenes.add(front);
        SubScene back = RubiksCubeApplication.rubiksCubeInteract.buildSubScene(RubiksCubeViewEnum.B);
        scenes.add(back, 1, 0);
        subScenes.add(back);
         */
        /*
        for (RubiksCubeViewEnum rubiksCubeViewEnum : RubiksCubeViewEnum.values()) {
            SubScene subScene = RubiksCubeApplication.rubiksCubeInteract.buildSubScene(rubiksCubeViewEnum);
            switch (rubiksCubeViewEnum) {
                case F -> scenes.add(subScene, 1, 1);
                case B -> scenes.add(subScene, 3, 1);
                case U -> scenes.add(subScene, 1, 0);
                case D -> scenes.add(subScene, 1, 2);
                case R -> scenes.add(subScene, 2, 1);
                case L -> scenes.add(subScene, 0, 1);
            }
            subScenes.add(subScene);
        }
        for (SubScene subScene : subScenes) {
            bindSize(subScene, gridPane, tbLeft, tbRight, tbTop, tbBottom);
        }
         */
        SubScene front = RubiksCubeApplication.rubiksCubeInteract.buildSubScene();
        front.setOnMousePressed(RubiksCubeApplication.rubiksCubeInteract::onMousePressed);
        front.setOnMouseDragged(RubiksCubeApplication.rubiksCubeInteract::onMouseDragged);
        front.setOnMouseReleased(RubiksCubeApplication.rubiksCubeInteract::onMouseReleased);
        front.setOnMouseClicked(RubiksCubeApplication.rubiksCubeInteract::onMouseClicked);
        front.setOnScroll(RubiksCubeApplication.rubiksCubeInteract::onScroll);
        gridPane.add(front, 1, 1);
        bindSize(front, gridPane, tbLeft, tbRight, tbTop, tbBottom);

        Scene scene = new Scene(gridPane, width, height, true);
        scene.setFill(Color.ALICEBLUE);
        scene.setOnKeyPressed(ke -> rubiksCubeInteract.setOnKeyPressed(ke));
        scene.setOnKeyTyped(ke -> rubiksCubeInteract.setOnKeyTyped(ke));
        scene.setOnKeyReleased(ke -> rubiksCubeInteract.onKeyReleased(ke));

        primaryStage.setScene(scene);
        primaryStage.setTitle("Rubik's Cube");
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

    private List<Node> getTop() {
        GridPane gridPane = new GridPane();
        gridPane.add(new RotationButton(RotationEnum.L), 0, 0);
        gridPane.add(new RotationButton(RotationEnum.L_DOUBLE), 0, 1);
        gridPane.add(new RotationButton(RotationEnum.L_REVERSE), 0, 2);
        gridPane.add(new RotationButton(RotationEnum.R), 4, 0);
        gridPane.add(new RotationButton(RotationEnum.R_DOUBLE), 4, 1);
        gridPane.add(new RotationButton(RotationEnum.R_REVERSE), 4, 2);
        gridPane.add(new RotationButton(RotationEnum.U), 1, 0);
        gridPane.add(new RotationButton(RotationEnum.U_DOUBLE), 2, 0);
        gridPane.add(new RotationButton(RotationEnum.U_REVERSE), 3, 0);
        gridPane.add(new RotationButton(RotationEnum.B), 1, 1);
        gridPane.add(new RotationButton(RotationEnum.B_DOUBLE), 2, 1);
        gridPane.add(new RotationButton(RotationEnum.B_REVERSE), 3, 1);
        gridPane.add(new RotationButton(RotationEnum.F), 1, 2);
        gridPane.add(new RotationButton(RotationEnum.F_DOUBLE), 2, 2);
        gridPane.add(new RotationButton(RotationEnum.F_REVERSE), 3, 2);
        gridPane.add(new RotationButton(RotationEnum.D), 1, 3);
        gridPane.add(new RotationButton(RotationEnum.D_DOUBLE), 2, 3);
        gridPane.add(new RotationButton(RotationEnum.D_REVERSE), 3, 3);

        Button reset = new Button("Reset");
        reset.setOnAction(e -> RubiksCubeApplication.rubiksCubeInteract.reset());
        gridPane.add(reset, 5, 0);
        Button scramble = new Button("Scramble");
        scramble.setOnAction(e -> RubiksCubeApplication.rubiksCubeInteract.scramble());
        gridPane.add(scramble, 5, 1);
        Button solve = new Button("Solve");
        solve.setOnAction(e -> RubiksCubeApplication.rubiksCubeInteract.solve());
        gridPane.add(solve, 5, 2);
        Button scrambleSolve = new Button("Scramble & Solve");
        scrambleSolve.setOnAction(e -> RubiksCubeApplication.rubiksCubeInteract.scrambleAndSolve());
        gridPane.add(scrambleSolve, 5, 3);

        return List.of(gridPane);
    }

    private List<Node> getBottom() {
        return List.of();
    }

    private List<Node> getLeft() {
        return List.of();
    }

    private List<Node> getRight() {
        return List.of();
    }

}

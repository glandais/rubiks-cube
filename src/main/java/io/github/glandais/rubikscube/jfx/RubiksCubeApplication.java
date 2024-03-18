package io.github.glandais.rubikscube.jfx;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class RubiksCubeApplication extends Application {
    private static RubiksCubeInteract rubiksCubeInteract;

    public static void launchFromElseWhere(RubiksCubeInteract rubiksCubeInteract) {
        RubiksCubeApplication.rubiksCubeInteract = rubiksCubeInteract;
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        root.getChildren().add(RubiksCubeApplication.rubiksCubeInteract.getCamera());
        root.getChildren().add(RubiksCubeApplication.rubiksCubeInteract.getCube3());

        Scene scene = getScene(root, RubiksCubeApplication.rubiksCubeInteract.getCamera());

        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();

        rubiksCubeInteract.start();
    }

    private Scene getScene(Group root, PerspectiveCamera camera) {
        Scene scene = new Scene(root, 800, 600, true, SceneAntialiasing.BALANCED);
        scene.setFill(Color.DARKGREEN);
        scene.setCamera(camera);

        scene.setOnMousePressed(event -> rubiksCubeInteract.onMousePressed(event));
        scene.setOnMouseDragged(event -> rubiksCubeInteract.onMouseDragged(event));
        scene.setOnMouseClicked(event -> rubiksCubeInteract.onMouseClicked(event));
        scene.setOnScroll(event -> rubiksCubeInteract.onScroll(event));
        scene.setOnKeyPressed(ke -> rubiksCubeInteract.setOnKeyPressed(ke));
        scene.setOnKeyTyped(ke -> rubiksCubeInteract.setOnKeyTyped(ke));
        scene.setOnKeyReleased(ke -> rubiksCubeInteract.onKeyReleased(ke));
        return scene;
    }

}

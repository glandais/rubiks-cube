package io.github.glandais.rubikscube;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Shape3D;
import javafx.scene.shape.Sphere;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Random;

/**
 * example for flat labels
 */
public class StarFieldExample extends Application {

    public static void launchFromElseWhere() {
        launch();
    }

    final double sceneWidth = 600;
    final double sceneHeight = 600;

    private double mousePosX;
    private double mousePosY;
    private double mouseOldX;
    private double mouseOldY;
    private double mouseDeltaX;
    private double mouseDeltaY;

    // We'll use custom Rotate transforms to manage the coordinate conversions
    private final Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
    private final Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);
    private final Rotate rotateZ = new Rotate(0, Rotate.Z_AXIS);

    //All shapes and labels linked via hash for easy update during camera movement
    private final HashMap<Shape3D, Label> shape3DToLabel = new HashMap<>();

    private SubScene subScene;

    private final Random random = new Random();

    private final static double RADIUS_MAX = 7;
    private final static double X_MAX = 300;
    private final static double Y_MAX = 300;
    private final static double Z_MAX = 300;

    public Pane createStarField() {
        generateRandomStars(20);

        Group spheres = new Group();
        spheres.getChildren().addAll(shape3DToLabel.keySet());

        //attach our custom rotation transforms so we can update the labels dynamically
        spheres.getTransforms().addAll(rotateX, rotateY, rotateZ);

        subScene = new SubScene(spheres, sceneWidth, sceneHeight, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.BLACK);

        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(10000.0);
        camera.setTranslateZ(-1000);

        subScene.setCamera(camera);
        Group sceneRoot = new Group(subScene);

        Group labels = new Group();
        labels.getChildren().addAll(shape3DToLabel.values());
        sceneRoot.getChildren().add(labels);

        subScene.setOnMousePressed((MouseEvent me) -> {
                    mousePosX = me.getSceneX();
                    mousePosY = me.getSceneY();
                    mouseOldX = me.getSceneX();
                    mouseOldY = me.getSceneY();
                }
        );

        subScene.setOnMouseDragged((MouseEvent me) -> {
                    mouseOldX = mousePosX;
                    mouseOldY = mousePosY;
                    mousePosX = me.getSceneX();
                    mousePosY = me.getSceneY();
                    mouseDeltaX = (mousePosX - mouseOldX);
                    mouseDeltaY = (mousePosY - mouseOldY);
                    double modifier = 5.0;
                    double modifierFactor = 0.1;

                    if (me.isPrimaryButtonDown()) {
                        if (me.isAltDown()) { //roll
                            rotateZ.setAngle(((rotateZ.getAngle() + mouseDeltaX * modifierFactor * modifier * 2.0) % 360 + 540) % 360 - 180); // +
                        } else {
                            rotateY.setAngle(((rotateY.getAngle() + mouseDeltaX * modifierFactor * modifier * 2.0) % 360 + 540) % 360 - 180); // +
                            rotateX.setAngle(((rotateX.getAngle() - mouseDeltaY * modifierFactor * modifier * 2.0) % 360 + 540) % 360 - 180); // -
                        }
                    }
                    updateLabels();
                }
        );

        // add to the 2D portion of this component
        Pane pane = new Pane();
        pane.setPrefSize(sceneWidth, sceneHeight);
        pane.setMaxSize(Pane.USE_COMPUTED_SIZE, Pane.USE_COMPUTED_SIZE);
        pane.setMinSize(Pane.USE_COMPUTED_SIZE, Pane.USE_COMPUTED_SIZE);
        pane.setBackground(Background.EMPTY);
        pane.getChildren().add(sceneRoot);
        pane.setPickOnBounds(false);

        subScene.widthProperty().bind(pane.widthProperty());
        subScene.heightProperty().bind(pane.heightProperty());
        Platform.runLater(this::updateLabels);
        return (pane);
    }

    public void generateRandomStars(int numberStars) {
        Font font = new Font("arial", 10);
        for (int i = 0; i < numberStars; i++) {
            double radius = random.nextDouble() * RADIUS_MAX;
            Color color = randomColor();
            double x = random.nextDouble() * X_MAX * 2 / 3 * (random.nextBoolean() ? 1 : -1);
            double y = random.nextDouble() * Y_MAX * 2 / 3 * (random.nextBoolean() ? 1 : -1);
            double z = random.nextDouble() * Z_MAX * 2 / 3 * (random.nextBoolean() ? 1 : -1);

            String labelText = "Star " + i;
            createSphereLabel(radius, x, y, z, color, font, labelText);
        }
    }

    private Color randomColor() {
        int r = random.nextInt(255);
        int g = random.nextInt(255);
        int b = random.nextInt(255);
        return Color.rgb(r, g, b);
    }

    private void createSphereLabel(double radius, double x, double y, double z, Color color, Font font, String labelText) {
        Sphere sphere = new Sphere(radius);
        sphere.setTranslateX(x);
        sphere.setTranslateY(y);
        sphere.setTranslateZ(z);
        sphere.setMaterial(new PhongMaterial(color));

        Label label = new Label(labelText);
        label.setTextFill(color);
        label.setFont(font);

        //Add to hashmap so updateLabels() can manage the label position
        shape3DToLabel.put(sphere, label);
    }

    private void updateLabels() {
        shape3DToLabel.forEach((node, label) -> {
            Point3D coordinates = node.localToScene(Point3D.ZERO, true);

            //Clipping Logic
            //if coordinates are outside of the scene it could
            //stretch the screen so don't transform them
            double x = coordinates.getX();
            double y = coordinates.getY();

            // is it left of the view?
            if (x < 0) {
                x = 0;
            }

            // is it right of the view?
            if ((x + label.getWidth() + 5) > subScene.getWidth()) {
                x = subScene.getWidth() - (label.getWidth() + 5);
            }

            // is it above the view?
            if (y < 0) {
                y = 0;
            }

            // is it below the view
            if ((y + label.getHeight()) > subScene.getHeight()) {
                y = subScene.getHeight() - (label.getHeight() + 5);
            }

            //update the local transform of the label.
            label.getTransforms().setAll(new Translate(x, y));
        });
    }

    //////////////////////////////////

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        Pane pane = createStarField();
        root.getChildren().add(pane);
        Scene scene = new Scene(root, sceneWidth, sceneHeight);
        primaryStage.setTitle("2D Labels over 3D SubScene");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
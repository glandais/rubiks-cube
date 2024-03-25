package io.github.glandais.rubikscube.jfx.scene;

import io.github.glandais.rubikscube.model.FaceletEnum;
import javafx.scene.Group;
import javafx.scene.transform.Rotate;
import lombok.Getter;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static javafx.scene.transform.Rotate.X_AXIS;
import static javafx.scene.transform.Rotate.Y_AXIS;

@Getter
public class Cube3 extends Group {

    private static final boolean SHOW_AXES = false;

    private List<Cube> cubes;
    private Map<FaceletEnum, Facelet> facelets;
    @Getter
    private final Rotate rotateX = new Rotate(0, X_AXIS);
    @Getter
    private final Rotate rotateY = new Rotate(0, Y_AXIS);

    public Cube3() {
        super();
        reset();
    }

    public void reset() {
        cubes = new ArrayList<>();
        this.facelets = new EnumMap<>(FaceletEnum.class);
        getChildren().clear();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                for (int k = -1; k <= 1; k++) {
                    Cube cube = new Cube(i, j, k);
                    cubes.add(cube);
                    for (Facelet realFace : cube.getRealFaces()) {
                        facelets.put(realFace.getInitialPosition(), realFace);
                    }
                }
            }
        }
        getChildren().addAll(cubes);
        Axes axes = new Axes();
        axes.setVisible(SHOW_AXES);
        getChildren().add(axes);
        getTransforms().setAll(rotateX, rotateY);
    }

    public List<Cube> getCubes(Integer x, Integer y, Integer z) {
        return cubes.stream()
                .filter(c -> {
                    if (x != null && x.equals(c.getX())) {
                        return true;
                    }
                    if (y != null && y.equals(c.getY())) {
                        return true;
                    }
                    return z != null && z.equals(c.getZ());
                })
                .toList();
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

    private double mod360(double a) {
        while (a < 0) {
            a = a + 360.0;
        }
        while (a >= 360) {
            a = a - 360.0;
        }
        return a;
    }

    public void resetView() {
        rotateX.setAngle(200);
        rotateY.setAngle(340);
        for (Cube cube : cubes) {
            cube.resetView();
        }
    }

    public double getAngleX() {
        return rotateX.getAngle();
    }

    public double getAngleY() {
        return rotateY.getAngle();
    }

    public void initExplode() {
        for (Cube cube : cubes) {
            cube.initExplode();
        }
    }
    public boolean explode(long elapsed) {
        for (Cube cube : cubes) {
            cube.explode(elapsed);
        }
        return elapsed > Cube.EXPLOSION_DURATION;
    }

}

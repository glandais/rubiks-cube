package io.github.glandais.rubikscube.jfx;

import javafx.scene.Group;
import lombok.Getter;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Getter
public class Cube3 extends Group {

    private static final boolean SHOW_AXES = false;

    private List<Cube> cubes;
    private Map<Facelet3DEnum, Facelet> facelets;

    public Cube3() {
        super();
        reset();
    }

    public void reset() {
        cubes = new ArrayList<>();
        this.facelets = new EnumMap<>(Facelet3DEnum.class);
        getChildren().clear();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                for (int k = -1; k <= 1; k++) {
                    Cube cube = new Cube(i, j, k);
                    cubes.add(cube);
                    for (Facelet realFace : cube.getRealFaces()) {
                        facelets.put(realFace.getFacelet3DEnum(), realFace);
                    }
                }
            }
        }
        getChildren().addAll(cubes);

        if (SHOW_AXES) {
            Axes axes = new Axes();
            getChildren().add(axes);
        }
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
}

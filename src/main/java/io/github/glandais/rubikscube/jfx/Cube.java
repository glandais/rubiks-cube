package io.github.glandais.rubikscube.jfx;

import javafx.scene.Group;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Cube extends Group {

    private static final boolean SHOW_AXES = true;

    private List<SmallCube> smallCubes;

    public Cube() {
        super();
        reset();
    }

    public void reset() {
        smallCubes = new ArrayList<>();
        getChildren().clear();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                for (int k = -1; k <= 1; k++) {
                    SmallCube smallCube = new SmallCube(i, j, k);
                    smallCubes.add(smallCube);
                }
            }
        }
        getChildren().addAll(smallCubes);

        if (SHOW_AXES) {
            Axes axes = new Axes();
            getChildren().add(axes);
        }
    }

    public List<SmallCube> getSmallCubes(Integer x, Integer y, Integer z) {
        return smallCubes.stream()
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

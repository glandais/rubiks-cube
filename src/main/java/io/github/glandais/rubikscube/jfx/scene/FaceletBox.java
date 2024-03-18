package io.github.glandais.rubikscube.jfx.scene;

import javafx.scene.shape.Box;
import lombok.Getter;

@Getter
public class FaceletBox extends Box {

    private final Facelet facelet;

    public FaceletBox(Facelet facelet, double width, double height, double depth) {
        super(width, height, depth);
        this.facelet = facelet;
    }
}

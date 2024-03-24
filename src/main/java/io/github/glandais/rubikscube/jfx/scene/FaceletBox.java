package io.github.glandais.rubikscube.jfx.scene;

import javafx.scene.shape.Box;
import lombok.Getter;

@Getter
public class FaceletBox extends Box {

    private final Facelet facelet;
    private final FaceletTypeEnum faceletType;

    public FaceletBox(Facelet facelet, FaceletTypeEnum faceletType, double width, double height, double depth) {
        super(width, height, depth);
        this.facelet = facelet;
        this.faceletType = faceletType;
    }
}

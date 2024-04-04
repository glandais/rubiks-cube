package io.github.glandais.rubikscube.model.view;

import io.github.glandais.rubikscube.model.Action;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ViewEnum implements Action {
    fru(CubeVisibleOrientation.DEFAULT, 200, 340),
    rbu(CubeVisibleOrientation.UP_RIGHT, 200, 250),
    blu(CubeVisibleOrientation.UP_BACK, 200, 160),
    lfu(CubeVisibleOrientation.UP_LEFT, 200, 70),
    fld(CubeVisibleOrientation.DOWN_FRONT, 20, 200),
    rfd(CubeVisibleOrientation.DOWN_RIGHT, 20, 110),
    brd(CubeVisibleOrientation.DOWN_BACK, 20, 20),
    lbd(CubeVisibleOrientation.DOWN_LEFT, 20, 290),
    ;

    private final CubeVisibleOrientation cubeVisibleOrientation;
    private final int rotateX;
    private final int rotateY;

    public static Action forNotation(String notation) {
        for (ViewEnum viewEnum : values()) {
            if (viewEnum.getCubeVisibleOrientation().notation().equals(notation)) {
                return viewEnum;
            }
        }
        throw new IllegalArgumentException("invalid view " + notation);
    }

    @Override
    public String getNotation() {
        return name();
    }

    @Override
    public Action reverse() {
        return this;
    }

    @Override
    public String toString() {
        return getNotation();
    }
}

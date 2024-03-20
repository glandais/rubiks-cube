package io.github.glandais.rubikscube.model.view;

import io.github.glandais.rubikscube.model.SideEnum;

import static io.github.glandais.rubikscube.model.SideEnum.*;
import static io.github.glandais.rubikscube.model.SideEnum.R;
import static io.github.glandais.rubikscube.model.SideEnum.U;

public record CubeVisibleOrientation(SideEnum f, SideEnum r, SideEnum u) {
    public static final CubeVisibleOrientation DEFAULT = new CubeVisibleOrientation(F, R, U);
    public static final CubeVisibleOrientation UP_RIGHT = new CubeVisibleOrientation(R, B, U);
    public static final CubeVisibleOrientation UP_BACK = new CubeVisibleOrientation(B, L, U);
    public static final CubeVisibleOrientation UP_LEFT = new CubeVisibleOrientation(L, F, U);

}

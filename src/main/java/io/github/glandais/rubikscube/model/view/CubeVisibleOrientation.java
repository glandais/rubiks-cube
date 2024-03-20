package io.github.glandais.rubikscube.model.view;

import io.github.glandais.rubikscube.model.SideEnum;

import static io.github.glandais.rubikscube.model.SideEnum.B;
import static io.github.glandais.rubikscube.model.SideEnum.D;
import static io.github.glandais.rubikscube.model.SideEnum.F;
import static io.github.glandais.rubikscube.model.SideEnum.L;
import static io.github.glandais.rubikscube.model.SideEnum.R;
import static io.github.glandais.rubikscube.model.SideEnum.U;

public record CubeVisibleOrientation(SideEnum f, SideEnum r, SideEnum u) {
    public static final CubeVisibleOrientation DEFAULT = new CubeVisibleOrientation(F, R, U);
    public static final CubeVisibleOrientation UP_RIGHT = new CubeVisibleOrientation(R, B, U);
    public static final CubeVisibleOrientation UP_BACK = new CubeVisibleOrientation(B, L, U);
    public static final CubeVisibleOrientation UP_LEFT = new CubeVisibleOrientation(L, F, U);
    public static final CubeVisibleOrientation DOWN_FRONT = new CubeVisibleOrientation(F, L, D);
    public static final CubeVisibleOrientation DOWN_RIGHT = new CubeVisibleOrientation(R, F, D);
    public static final CubeVisibleOrientation DOWN_BACK = new CubeVisibleOrientation(B, R, D);
    public static final CubeVisibleOrientation DOWN_LEFT = new CubeVisibleOrientation(L, B, D);

}

package io.github.glandais.rubikscube.model.view;

import io.github.glandais.rubikscube.model.FaceletEnum;
import io.github.glandais.rubikscube.model.SideEnum;
import io.github.glandais.rubikscube.model.SideOppositeEnum;

import java.util.HashMap;
import java.util.Map;

import static io.github.glandais.rubikscube.model.SideEnum.B;
import static io.github.glandais.rubikscube.model.SideEnum.D;
import static io.github.glandais.rubikscube.model.SideEnum.F;
import static io.github.glandais.rubikscube.model.SideEnum.L;
import static io.github.glandais.rubikscube.model.SideEnum.R;
import static io.github.glandais.rubikscube.model.SideEnum.U;

public class RotatedCubes {

    private static final Map<CubeVisibleOrientation, byte[]> realPositions = new HashMap<>();

    private static final int[][] faceRotations = new int[3][9];

    static {
        buildFaceRotations();

        addRealPositions(CubeVisibleOrientation.UP_RIGHT);
        addRealPositions(CubeVisibleOrientation.UP_BACK);
        addRealPositions(CubeVisibleOrientation.UP_LEFT);

        addRealPositions(CubeVisibleOrientation.DOWN_FRONT);
        addRealPositions(CubeVisibleOrientation.DOWN_RIGHT);
        addRealPositions(CubeVisibleOrientation.DOWN_BACK);
        addRealPositions(CubeVisibleOrientation.DOWN_LEFT);
    }

    private static void buildFaceRotations() {
        // +90
        int k = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 2; j >= 0; j--) {
                int s = i + 3 * j;
                if (k != 4) {
                    faceRotations[0][s] = k;
                }
                k++;
            }
        }
        // +180
        k = 0;
        for (int j = 2; j >= 0; j--) {
            for (int i = 2; i >= 0; i--) {
                int s = i + 3 * j;
                if (k != 4) {
                    faceRotations[1][s] = k;
                }
                k++;
            }
        }
        // -90
        k = 0;
        for (int i = 2; i >= 0; i--) {
            for (int j = 0; j < 3; j++) {
                int s = i + 3 * j;
                if (k != 4) {
                    faceRotations[2][s] = k;
                }
                k++;
            }
        }
    }

    public static byte getRealPosition(CubeVisibleOrientation view, int i) {
        if (view.equals(CubeVisibleOrientation.DEFAULT)) {
            return (byte) i;
        }
        byte[] realPositionsForView = realPositions.get(view);
        if (realPositionsForView == null) {
            throw new IllegalArgumentException("Unsupported view " + view);
        }
        return realPositionsForView[i];
    }

    private static void addRealPositions(CubeVisibleOrientation view) {
        if (view.u() == U) {
            addRealPositionsUp(view);
        }
        if (view.u() == D) {
            addRealPositionsDown(view);
        }
    }

    private static void addRealPositionsUp(CubeVisibleOrientation view) {
        byte[] positions = new byte[48];
        int angle = 0;
        if (view.f() == R) {
            angle = 90;
        } else if (view.f() == B) {
            angle = 180;
        } else if (view.f() == L) {
            angle = -90;
        }
        addFace(positions, view.f(), F, 0);
        addFace(positions, SideOppositeEnum.getOpposite(view.f()), B, 0);
        addFace(positions, view.u(), U, angle);
        addFace(positions, SideOppositeEnum.getOpposite(view.u()), D, -angle);
        addFace(positions, view.r(), R, 0);
        addFace(positions, SideOppositeEnum.getOpposite(view.r()), L, 0);
        realPositions.put(view, positions);
    }

    private static void addRealPositionsDown(CubeVisibleOrientation view) {
        byte[] positions = new byte[48];
        int angle = 180;
        if (view.f() == R) {
            angle = 90;
        } else if (view.f() == B) {
            angle = 0;
        } else if (view.f() == L) {
            angle = -90;
        }
        addFace(positions, view.f(), F, 180);
        addFace(positions, SideOppositeEnum.getOpposite(view.f()), B, 180);
        addFace(positions, view.u(), U, angle);
        addFace(positions, SideOppositeEnum.getOpposite(view.u()), D, -angle);
        addFace(positions, view.r(), R, 180);
        addFace(positions, SideOppositeEnum.getOpposite(view.r()), L, 180);
        realPositions.put(view, positions);
    }

    private static void addFace(byte[] positions, SideEnum source, SideEnum target, int angle) {
        for (int i = 0; i < 9; i++) {
            if (i != 4) {
                FaceletEnum targetFacelet = FaceletEnum.of(target, rotate(i, angle));
                FaceletEnum sourceFacelet = FaceletEnum.of(source, i);
                positions[targetFacelet.ordinal()] = (byte) sourceFacelet.ordinal();
            }
        }
    }

    private static int rotate(int i, int angle) {
        if (angle == 0) {
            return i;
        } else if (angle == 90) {
            return faceRotations[0][i];
        } else if (angle == -90) {
            return faceRotations[2][i];
        } else {
            return faceRotations[1][i];
        }
    }

}

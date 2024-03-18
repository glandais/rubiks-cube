package io.github.glandais.rubikscube.model;

import lombok.experimental.UtilityClass;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static io.github.glandais.rubikscube.model.FaceletDirectionEnum.DOWN;
import static io.github.glandais.rubikscube.model.FaceletDirectionEnum.LEFT;
import static io.github.glandais.rubikscube.model.FaceletDirectionEnum.RIGHT;
import static io.github.glandais.rubikscube.model.FaceletDirectionEnum.UP;
import static io.github.glandais.rubikscube.model.FaceletEnum.B0;
import static io.github.glandais.rubikscube.model.FaceletEnum.B1;
import static io.github.glandais.rubikscube.model.FaceletEnum.B2;
import static io.github.glandais.rubikscube.model.FaceletEnum.B3;
import static io.github.glandais.rubikscube.model.FaceletEnum.B5;
import static io.github.glandais.rubikscube.model.FaceletEnum.B6;
import static io.github.glandais.rubikscube.model.FaceletEnum.B7;
import static io.github.glandais.rubikscube.model.FaceletEnum.B8;
import static io.github.glandais.rubikscube.model.FaceletEnum.D0;
import static io.github.glandais.rubikscube.model.FaceletEnum.D1;
import static io.github.glandais.rubikscube.model.FaceletEnum.D2;
import static io.github.glandais.rubikscube.model.FaceletEnum.D3;
import static io.github.glandais.rubikscube.model.FaceletEnum.D5;
import static io.github.glandais.rubikscube.model.FaceletEnum.D6;
import static io.github.glandais.rubikscube.model.FaceletEnum.D7;
import static io.github.glandais.rubikscube.model.FaceletEnum.D8;
import static io.github.glandais.rubikscube.model.FaceletEnum.F0;
import static io.github.glandais.rubikscube.model.FaceletEnum.F1;
import static io.github.glandais.rubikscube.model.FaceletEnum.F2;
import static io.github.glandais.rubikscube.model.FaceletEnum.F3;
import static io.github.glandais.rubikscube.model.FaceletEnum.F5;
import static io.github.glandais.rubikscube.model.FaceletEnum.F6;
import static io.github.glandais.rubikscube.model.FaceletEnum.F7;
import static io.github.glandais.rubikscube.model.FaceletEnum.F8;
import static io.github.glandais.rubikscube.model.FaceletEnum.L0;
import static io.github.glandais.rubikscube.model.FaceletEnum.L1;
import static io.github.glandais.rubikscube.model.FaceletEnum.L2;
import static io.github.glandais.rubikscube.model.FaceletEnum.L3;
import static io.github.glandais.rubikscube.model.FaceletEnum.L5;
import static io.github.glandais.rubikscube.model.FaceletEnum.L6;
import static io.github.glandais.rubikscube.model.FaceletEnum.L7;
import static io.github.glandais.rubikscube.model.FaceletEnum.L8;
import static io.github.glandais.rubikscube.model.FaceletEnum.R0;
import static io.github.glandais.rubikscube.model.FaceletEnum.R1;
import static io.github.glandais.rubikscube.model.FaceletEnum.R2;
import static io.github.glandais.rubikscube.model.FaceletEnum.R3;
import static io.github.glandais.rubikscube.model.FaceletEnum.R5;
import static io.github.glandais.rubikscube.model.FaceletEnum.R6;
import static io.github.glandais.rubikscube.model.FaceletEnum.R7;
import static io.github.glandais.rubikscube.model.FaceletEnum.R8;
import static io.github.glandais.rubikscube.model.FaceletEnum.U0;
import static io.github.glandais.rubikscube.model.FaceletEnum.U1;
import static io.github.glandais.rubikscube.model.FaceletEnum.U2;
import static io.github.glandais.rubikscube.model.FaceletEnum.U3;
import static io.github.glandais.rubikscube.model.FaceletEnum.U5;
import static io.github.glandais.rubikscube.model.FaceletEnum.U6;
import static io.github.glandais.rubikscube.model.FaceletEnum.U7;
import static io.github.glandais.rubikscube.model.FaceletEnum.U8;
import static io.github.glandais.rubikscube.model.RotationEnum.B;
import static io.github.glandais.rubikscube.model.RotationEnum.B_DOUBLE;
import static io.github.glandais.rubikscube.model.RotationEnum.B_REVERSE;
import static io.github.glandais.rubikscube.model.RotationEnum.D;
import static io.github.glandais.rubikscube.model.RotationEnum.D_DOUBLE;
import static io.github.glandais.rubikscube.model.RotationEnum.D_REVERSE;
import static io.github.glandais.rubikscube.model.RotationEnum.F;
import static io.github.glandais.rubikscube.model.RotationEnum.F_DOUBLE;
import static io.github.glandais.rubikscube.model.RotationEnum.F_REVERSE;
import static io.github.glandais.rubikscube.model.RotationEnum.L;
import static io.github.glandais.rubikscube.model.RotationEnum.L_DOUBLE;
import static io.github.glandais.rubikscube.model.RotationEnum.L_REVERSE;
import static io.github.glandais.rubikscube.model.RotationEnum.R;
import static io.github.glandais.rubikscube.model.RotationEnum.R_DOUBLE;
import static io.github.glandais.rubikscube.model.RotationEnum.R_REVERSE;
import static io.github.glandais.rubikscube.model.RotationEnum.U;
import static io.github.glandais.rubikscube.model.RotationEnum.U_DOUBLE;
import static io.github.glandais.rubikscube.model.RotationEnum.U_REVERSE;

@UtilityClass
public class FaceletRotationEnum {

    static Map<FaceletEnum, Map<FaceletDirectionEnum, RotationEnum>> rotations;

    static {
        rotations = new EnumMap<>(FaceletEnum.class);
        for (FaceletEnum faceletEnum : FaceletEnum.values()) {
            rotations.put(faceletEnum, new EnumMap<>(FaceletDirectionEnum.class));
        }

        add(F,
                List.of(L2, L5, L8),
                List.of(R0, R3, R6),
                List.of(D0, D1, D2),
                List.of(U6, U7, U8)
        );
        add(B,
                List.of(R2, R5, R8),
                List.of(L0, L3, L6),
                List.of(U0, U1, U2),
                List.of(D6, D7, D8)
        );
        add(L,
                List.of(
                        B2, B5, B8
                ),
                List.of(
                        F0, F3, F6,
                        U0, U3, U5,
                        D0, D3, D5
                ),
                List.of(),
                List.of()
        );
        add(R,
                List.of(
                        F2, F5, F8,
                        U2, U5, U8,
                        D2, D5, D8
                ),
                List.of(
                        B0, B3, B6
                ),
                List.of(),
                List.of()
        );
        add(U,
                List.of(),
                List.of(),
                List.of(
                        L0, L1, L2,
                        F0, F1, F2,
                        R0, R1, R2,
                        B0, B1, B2
                ),
                List.of()
        );
        add(D,
                List.of(),
                List.of(),
                List.of(),
                List.of(
                        L6, L7, L8,
                        F6, F7, F8,
                        R6, R7, R8,
                        B6, B7, B8
                )
        );
    }

    public static RotationEnum getRotation(FaceletEnum facelet, FaceletDirectionEnum direction) {
        if (facelet == null) {
            return null;
        }
        Map<FaceletDirectionEnum, RotationEnum> map = rotations.get(facelet);
        if (map == null) {
            return null;
        }
        return map.get(direction);
    }

    private static void add(
            RotationEnum rotationEnum,
            List<FaceletEnum> faceletsUp,
            List<FaceletEnum> faceletsDown,
            List<FaceletEnum> faceletsLeft,
            List<FaceletEnum> faceletsRight
    ) {
        for (FaceletEnum facelet : faceletsUp) {
            rotations.get(facelet).put(UP, rotationEnum);
            rotations.get(facelet).put(DOWN, inverse(rotationEnum));
        }
        for (FaceletEnum facelet : faceletsDown) {
            rotations.get(facelet).put(DOWN, rotationEnum);
            rotations.get(facelet).put(UP, inverse(rotationEnum));
        }
        for (FaceletEnum facelet : faceletsLeft) {
            rotations.get(facelet).put(LEFT, rotationEnum);
            rotations.get(facelet).put(RIGHT, inverse(rotationEnum));
        }
        for (FaceletEnum facelet : faceletsRight) {
            rotations.get(facelet).put(RIGHT, rotationEnum);
            rotations.get(facelet).put(LEFT, inverse(rotationEnum));
        }
    }

    private static FaceletDirectionEnum inverse(FaceletDirectionEnum faceletDirectionEnum) {
        return switch (faceletDirectionEnum) {
            case UP -> DOWN;
            case DOWN -> UP;
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
        };
    }

    private static RotationEnum inverse(RotationEnum rotationEnum) {
        return switch (rotationEnum) {
            case F -> F_REVERSE;
            case F_DOUBLE -> F_DOUBLE;
            case F_REVERSE -> F;
            case B -> B_REVERSE;
            case B_DOUBLE -> B_DOUBLE;
            case B_REVERSE -> B;
            case U -> U_REVERSE;
            case U_DOUBLE -> U_DOUBLE;
            case U_REVERSE -> U;
            case D -> D_REVERSE;
            case D_DOUBLE -> D_DOUBLE;
            case D_REVERSE -> D;
            case R -> R_REVERSE;
            case R_DOUBLE -> R_DOUBLE;
            case R_REVERSE -> R;
            case L -> L_REVERSE;
            case L_DOUBLE -> L_DOUBLE;
            case L_REVERSE -> L;
        };
    }

}

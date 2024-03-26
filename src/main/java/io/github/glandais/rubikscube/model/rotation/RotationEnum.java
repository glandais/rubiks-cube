package io.github.glandais.rubikscube.model.rotation;

import io.github.glandais.rubikscube.model.Action;
import io.github.glandais.rubikscube.model.SideEnum;
import io.github.glandais.rubikscube.model.SideOppositeEnum;
import io.github.glandais.rubikscube.model.view.CubeVisibleOrientation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Getter
public enum RotationEnum implements Action {
    F("F", SideEnum.F, RotationModifierEnum.NORMAL, AxisEnum.Z, -90.0, null, null, 1),
    F_DOUBLE("F2", SideEnum.F, RotationModifierEnum.DOUBLE, AxisEnum.Z, -180.0, null, null, 1),
    F_REVERSE("F'", SideEnum.F, RotationModifierEnum.REVERSE, AxisEnum.Z, 90.0, null, null, 1),
    B("B", SideEnum.B, RotationModifierEnum.NORMAL, AxisEnum.Z, 90.0, null, null, -1),
    B_DOUBLE("B2", SideEnum.B, RotationModifierEnum.DOUBLE, AxisEnum.Z, 180.0, null, null, -1),
    B_REVERSE("B'", SideEnum.B, RotationModifierEnum.REVERSE, AxisEnum.Z, -90.0, null, null, -1),
    U("U", SideEnum.U, RotationModifierEnum.NORMAL, AxisEnum.Y, -90.0, null, 1, null),
    U_DOUBLE("U2", SideEnum.U, RotationModifierEnum.DOUBLE, AxisEnum.Y, -180.0, null, 1, null),
    U_REVERSE("U'", SideEnum.U, RotationModifierEnum.REVERSE, AxisEnum.Y, 90.0, null, 1, null),
    D("D", SideEnum.D, RotationModifierEnum.NORMAL, AxisEnum.Y, 90.0, null, -1, null),
    D_DOUBLE("D2", SideEnum.D, RotationModifierEnum.DOUBLE, AxisEnum.Y, 180.0, null, -1, null),
    D_REVERSE("D'", SideEnum.D, RotationModifierEnum.REVERSE, AxisEnum.Y, -90.0, null, -1, null),
    R("R", SideEnum.R, RotationModifierEnum.NORMAL, AxisEnum.X, -90.0, 1, null, null),
    R_DOUBLE("R2", SideEnum.R, RotationModifierEnum.DOUBLE, AxisEnum.X, -180.0, 1, null, null),
    R_REVERSE("R'", SideEnum.R, RotationModifierEnum.REVERSE, AxisEnum.X, 90.0, 1, null, null),
    L("L", SideEnum.L, RotationModifierEnum.NORMAL, AxisEnum.X, 90.0, -1, null, null),
    L_DOUBLE("L2", SideEnum.L, RotationModifierEnum.DOUBLE, AxisEnum.X, 180.0, -1, null, null),
    L_REVERSE("L'", SideEnum.L, RotationModifierEnum.REVERSE, AxisEnum.X, -90.0, -1, null, null);

    final String notation;
    final SideEnum side;
    final RotationModifierEnum modifier;
    final AxisEnum axis;
    final double angle;
    final Integer x;
    final Integer y;
    final Integer z;

    public static RotationEnum forNotation(String notation) {
        for (RotationEnum rotationEnum : values()) {
            if (rotationEnum.getNotation().equals(notation)) {
                return rotationEnum;
            }
        }
        throw new IllegalArgumentException("invalid rotation " + notation);
    }

    public static RotationEnum fromOtherSide(RotationEnum rotation, CubeVisibleOrientation cubeVisibleOrientation) {
        SideEnum mappedSide = switch (rotation.side) {
            case F -> cubeVisibleOrientation.f();
            case B -> SideOppositeEnum.getOpposite(cubeVisibleOrientation.f());
            case U -> cubeVisibleOrientation.u();
            case D -> SideOppositeEnum.getOpposite(cubeVisibleOrientation.u());
            case R -> cubeVisibleOrientation.r();
            case L -> SideOppositeEnum.getOpposite(cubeVisibleOrientation.r());
        };
        return getRotationEnum(mappedSide, rotation.getModifier());
    }

    public static RotationEnum getRotationEnum(SideEnum side, RotationModifierEnum modifier) {
        for (RotationEnum rotationEnum : values()) {
            if (rotationEnum.side == side &&
                rotationEnum.modifier == modifier) {
                return rotationEnum;
            }
        }
        throw new IllegalArgumentException("no RotationEnum for " + side + " " + modifier);
    }

    public RotationEnum reverse() {
        if (modifier == RotationModifierEnum.DOUBLE) {
            return this;
        }
        RotationModifierEnum reversed;
        if (modifier == RotationModifierEnum.NORMAL) {
            reversed = RotationModifierEnum.REVERSE;
        } else {
            reversed = RotationModifierEnum.NORMAL;
        }
        for (RotationEnum other : values()) {
            if (other.side == side && other.modifier == reversed) {
                return other;
            }
        }
        throw new IllegalStateException("No inverse for " + this);
    }

}

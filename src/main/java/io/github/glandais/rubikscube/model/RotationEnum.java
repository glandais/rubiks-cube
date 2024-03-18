package io.github.glandais.rubikscube.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum RotationEnum {
    F("F", AxisEnum.Z, -90.0, null, null, 1),
    F_DOUBLE("F2", AxisEnum.Z, -180.0, null, null, 1),
    F_REVERSE("F'", AxisEnum.Z, 90.0, null, null, 1),
    B("B", AxisEnum.Z, 90.0, null, null, -1),
    B_DOUBLE("B2", AxisEnum.Z, 180.0, null, null, -1),
    B_REVERSE("B'", AxisEnum.Z, -90.0, null, null, -1),
    U("U", AxisEnum.Y, -90.0, null, 1, null),
    U_DOUBLE("U2", AxisEnum.Y, -180.0, null, 1, null),
    U_REVERSE("U'", AxisEnum.Y, 90.0, null, 1, null),
    D("D", AxisEnum.Y, 90.0, null, -1, null),
    D_DOUBLE("D2", AxisEnum.Y, 180.0, null, -1, null),
    D_REVERSE("D'", AxisEnum.Y, -90.0, null, -1, null),
    R("R", AxisEnum.X, -90.0, 1, null, null),
    R_DOUBLE("R2", AxisEnum.X, -180.0, 1, null, null),
    R_REVERSE("R'", AxisEnum.X, 90.0, 1, null, null),
    L("L", AxisEnum.X, 90.0, -1, null, null),
    L_DOUBLE("L2", AxisEnum.X, 180.0, -1, null, null),
    L_REVERSE("L'", AxisEnum.X, -90.0, -1, null, null);

    final String notation;
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
}

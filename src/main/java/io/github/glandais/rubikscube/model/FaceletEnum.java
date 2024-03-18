package io.github.glandais.rubikscube.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum FaceletEnum {
    F0(SideEnum.F, 0),
    F1(SideEnum.F, 1),
    F2(SideEnum.F, 2),
    F3(SideEnum.F, 3),
    F5(SideEnum.F, 5),
    F6(SideEnum.F, 6),
    F7(SideEnum.F, 7),
    F8(SideEnum.F, 8),
    B0(SideEnum.B, 0),
    B1(SideEnum.B, 1),
    B2(SideEnum.B, 2),
    B3(SideEnum.B, 3),
    B5(SideEnum.B, 5),
    B6(SideEnum.B, 6),
    B7(SideEnum.B, 7),
    B8(SideEnum.B, 8),
    U0(SideEnum.U, 0),
    U1(SideEnum.U, 1),
    U2(SideEnum.U, 2),
    U3(SideEnum.U, 3),
    U5(SideEnum.U, 5),
    U6(SideEnum.U, 6),
    U7(SideEnum.U, 7),
    U8(SideEnum.U, 8),
    D0(SideEnum.D, 0),
    D1(SideEnum.D, 1),
    D2(SideEnum.D, 2),
    D3(SideEnum.D, 3),
    D5(SideEnum.D, 5),
    D6(SideEnum.D, 6),
    D7(SideEnum.D, 7),
    D8(SideEnum.D, 8),
    R0(SideEnum.R, 0),
    R1(SideEnum.R, 1),
    R2(SideEnum.R, 2),
    R3(SideEnum.R, 3),
    R5(SideEnum.R, 5),
    R6(SideEnum.R, 6),
    R7(SideEnum.R, 7),
    R8(SideEnum.R, 8),
    L0(SideEnum.L, 0),
    L1(SideEnum.L, 1),
    L2(SideEnum.L, 2),
    L3(SideEnum.L, 3),
    L5(SideEnum.L, 5),
    L6(SideEnum.L, 6),
    L7(SideEnum.L, 7),
    L8(SideEnum.L, 8);

    final SideEnum side;
    final int i;

    public static FaceletEnum of(SideEnum side, int i) {
        for (FaceletEnum faceletEnum : values()) {
            if (faceletEnum.side == side && faceletEnum.i == i) {
                return faceletEnum;
            }
        }
        return null;
    }

}

package io.github.glandais.rubikscube.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SideOppositeEnum {
    F(SideEnum.F, SideEnum.B),
    B(SideEnum.B, SideEnum.F),
    U(SideEnum.U, SideEnum.D),
    D(SideEnum.D, SideEnum.U),
    R(SideEnum.R, SideEnum.L),
    L(SideEnum.L, SideEnum.R);

    final SideEnum side;
    final SideEnum oppositeSide;

    public static SideEnum getOpposite(SideEnum side) {
        for (SideOppositeEnum sideOppositeEnum : values()) {
            if (sideOppositeEnum.side == side) {
                return sideOppositeEnum.oppositeSide;
            }
        }
        throw new IllegalArgumentException("invalid side " + side);
    }
}

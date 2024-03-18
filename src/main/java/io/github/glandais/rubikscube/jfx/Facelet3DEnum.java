package io.github.glandais.rubikscube.jfx;

import io.github.glandais.rubikscube.model.FaceletEnum;
import javafx.geometry.Point3D;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Facelet3DEnum {
    F0(FaceletEnum.F0,-1,1,2),
    F1(FaceletEnum.F1,0,1,2),
    F2(FaceletEnum.F2,1,1,2),
    F3(FaceletEnum.F3,-1,0,2),
    F5(FaceletEnum.F5,1,0,2),
    F6(FaceletEnum.F6,-1,-1,2),
    F7(FaceletEnum.F7,0,-1,2),
    F8(FaceletEnum.F8,1,-1,2),
    B0(FaceletEnum.B0,1,1,-2),
    B1(FaceletEnum.B1,0,1,-2),
    B2(FaceletEnum.B2,-1,1,-2),
    B3(FaceletEnum.B3,1,0,-2),
    B5(FaceletEnum.B5,-1,0,-2),
    B6(FaceletEnum.B6,1,-1,-2),
    B7(FaceletEnum.B7,0,-1,-2),
    B8(FaceletEnum.B8,-1,-1,-2),
    U0(FaceletEnum.U0,-1,2,-1),
    U1(FaceletEnum.U1,0,2,-1),
    U2(FaceletEnum.U2,1,2,-1),
    U3(FaceletEnum.U3,-1,2,0),
    U5(FaceletEnum.U5,1,2,0),
    U6(FaceletEnum.U6,-1,2,1),
    U7(FaceletEnum.U7,0,2,1),
    U8(FaceletEnum.U8,1,2,1),
    D0(FaceletEnum.D0,-1,-2,1),
    D1(FaceletEnum.D1,0,-2,1),
    D2(FaceletEnum.D2,1,-2,1),
    D3(FaceletEnum.D3,-1,-2,0),
    D5(FaceletEnum.D5,1,-2,0),
    D6(FaceletEnum.D6,-1,-2,-1),
    D7(FaceletEnum.D7,0,-2,-1),
    D8(FaceletEnum.D8,1,-2,-1),
    R0(FaceletEnum.R0,2,1,1),
    R1(FaceletEnum.R1,2,1,0),
    R2(FaceletEnum.R2,2,1,-1),
    R3(FaceletEnum.R3,2,0,1),
    R5(FaceletEnum.R5,2,0,-1),
    R6(FaceletEnum.R6,2,-1,1),
    R7(FaceletEnum.R7,2,-1,0),
    R8(FaceletEnum.R8,2,-1,-1),
    L0(FaceletEnum.L0,-2,1,-1),
    L1(FaceletEnum.L1,-2,1,0),
    L2(FaceletEnum.L2,-2,1,1),
    L3(FaceletEnum.L3,-2,0,-1),
    L5(FaceletEnum.L5,-2,0,1),
    L6(FaceletEnum.L6,-2,-1,-1),
    L7(FaceletEnum.L7,-2,-1,0),
    L8(FaceletEnum.L8,-2,-1,1);

    final FaceletEnum faceletEnum;
    final int x;
    final int y;
    final int z;

    public static Facelet3DEnum of(Point3D p) {
        int x = (int) Math.round(p.getX());
        int y = (int) Math.round(p.getY());
        int z = (int) Math.round(p.getZ());
        for (Facelet3DEnum facelet3DEnum : values()) {
            if (facelet3DEnum.x == x && facelet3DEnum.y == y && facelet3DEnum.z == z) {
                return facelet3DEnum;
            }
        }
        return null;
    }
}

package io.github.glandais.rubikscube.model.rotation.moves;

import lombok.experimental.UtilityClass;

import static io.github.glandais.rubikscube.model.rotation.moves.FaceletConstants.B0;
import static io.github.glandais.rubikscube.model.rotation.moves.FaceletConstants.B1;
import static io.github.glandais.rubikscube.model.rotation.moves.FaceletConstants.B2;
import static io.github.glandais.rubikscube.model.rotation.moves.FaceletConstants.B3;
import static io.github.glandais.rubikscube.model.rotation.moves.FaceletConstants.B5;
import static io.github.glandais.rubikscube.model.rotation.moves.FaceletConstants.B6;
import static io.github.glandais.rubikscube.model.rotation.moves.FaceletConstants.B7;
import static io.github.glandais.rubikscube.model.rotation.moves.FaceletConstants.B8;
import static io.github.glandais.rubikscube.model.rotation.moves.FaceletConstants.D0;
import static io.github.glandais.rubikscube.model.rotation.moves.FaceletConstants.D1;
import static io.github.glandais.rubikscube.model.rotation.moves.FaceletConstants.D2;
import static io.github.glandais.rubikscube.model.rotation.moves.FaceletConstants.D3;
import static io.github.glandais.rubikscube.model.rotation.moves.FaceletConstants.D5;
import static io.github.glandais.rubikscube.model.rotation.moves.FaceletConstants.D6;
import static io.github.glandais.rubikscube.model.rotation.moves.FaceletConstants.D7;
import static io.github.glandais.rubikscube.model.rotation.moves.FaceletConstants.D8;
import static io.github.glandais.rubikscube.model.rotation.moves.FaceletConstants.F0;
import static io.github.glandais.rubikscube.model.rotation.moves.FaceletConstants.F1;
import static io.github.glandais.rubikscube.model.rotation.moves.FaceletConstants.F2;
import static io.github.glandais.rubikscube.model.rotation.moves.FaceletConstants.F3;
import static io.github.glandais.rubikscube.model.rotation.moves.FaceletConstants.F5;
import static io.github.glandais.rubikscube.model.rotation.moves.FaceletConstants.F6;
import static io.github.glandais.rubikscube.model.rotation.moves.FaceletConstants.F7;
import static io.github.glandais.rubikscube.model.rotation.moves.FaceletConstants.F8;
import static io.github.glandais.rubikscube.model.rotation.moves.FaceletConstants.L0;
import static io.github.glandais.rubikscube.model.rotation.moves.FaceletConstants.L1;
import static io.github.glandais.rubikscube.model.rotation.moves.FaceletConstants.L2;
import static io.github.glandais.rubikscube.model.rotation.moves.FaceletConstants.L3;
import static io.github.glandais.rubikscube.model.rotation.moves.FaceletConstants.L5;
import static io.github.glandais.rubikscube.model.rotation.moves.FaceletConstants.L6;
import static io.github.glandais.rubikscube.model.rotation.moves.FaceletConstants.L7;
import static io.github.glandais.rubikscube.model.rotation.moves.FaceletConstants.L8;
import static io.github.glandais.rubikscube.model.rotation.moves.FaceletConstants.R0;
import static io.github.glandais.rubikscube.model.rotation.moves.FaceletConstants.R1;
import static io.github.glandais.rubikscube.model.rotation.moves.FaceletConstants.R2;
import static io.github.glandais.rubikscube.model.rotation.moves.FaceletConstants.R3;
import static io.github.glandais.rubikscube.model.rotation.moves.FaceletConstants.R5;
import static io.github.glandais.rubikscube.model.rotation.moves.FaceletConstants.R6;
import static io.github.glandais.rubikscube.model.rotation.moves.FaceletConstants.R7;
import static io.github.glandais.rubikscube.model.rotation.moves.FaceletConstants.R8;
import static io.github.glandais.rubikscube.model.rotation.moves.FaceletConstants.U0;
import static io.github.glandais.rubikscube.model.rotation.moves.FaceletConstants.U1;
import static io.github.glandais.rubikscube.model.rotation.moves.FaceletConstants.U2;
import static io.github.glandais.rubikscube.model.rotation.moves.FaceletConstants.U3;
import static io.github.glandais.rubikscube.model.rotation.moves.FaceletConstants.U5;
import static io.github.glandais.rubikscube.model.rotation.moves.FaceletConstants.U6;
import static io.github.glandais.rubikscube.model.rotation.moves.FaceletConstants.U7;
import static io.github.glandais.rubikscube.model.rotation.moves.FaceletConstants.U8;

@UtilityClass
public class RotationMoves {
    public static final byte[][] NEW_POSITIONS = {
            // F (F)
            {
                    // F0 F1 F2
                    F2, F5, F8,
                    // F3 F5
                    F1, F7,
                    // F6 F7 F8
                    F0, F3, F6,
                    // B0 B1 B2
                    B0, B1, B2,
                    // B3 B5
                    B3, B5,
                    // B6 B7 B8
                    B6, B7, B8,
                    // U0 U1 U2
                    U0, U1, U2,
                    // U3 U5
                    U3, U5,
                    // U6 U7 U8
                    R0, R3, R6,
                    // D0 D1 D2
                    L2, L5, L8,
                    // D3 D5
                    D3, D5,
                    // D6 D7 D8
                    D6, D7, D8,
                    // R0 R1 R2
                    D2, R1, R2,
                    // R3 R5
                    D1, R5,
                    // R6 R7 R8
                    D0, R7, R8,
                    // L0 L1 L2
                    L0, L1, U8,
                    // L3 L5
                    L3, U7,
                    // L6 L7 L8
                    L6, L7, U6
            },
            // F2 (F_DOUBLE)
            {
                    // F0 F1 F2
                    F8, F7, F6,
                    // F3 F5
                    F5, F3,
                    // F6 F7 F8
                    F2, F1, F0,
                    // B0 B1 B2
                    B0, B1, B2,
                    // B3 B5
                    B3, B5,
                    // B6 B7 B8
                    B6, B7, B8,
                    // U0 U1 U2
                    U0, U1, U2,
                    // U3 U5
                    U3, U5,
                    // U6 U7 U8
                    D2, D1, D0,
                    // D0 D1 D2
                    U8, U7, U6,
                    // D3 D5
                    D3, D5,
                    // D6 D7 D8
                    D6, D7, D8,
                    // R0 R1 R2
                    L8, R1, R2,
                    // R3 R5
                    L5, R5,
                    // R6 R7 R8
                    L2, R7, R8,
                    // L0 L1 L2
                    L0, L1, R6,
                    // L3 L5
                    L3, R3,
                    // L6 L7 L8
                    L6, L7, R0
            },
            // F' (F_REVERSE)
            {
                    // F0 F1 F2
                    F6, F3, F0,
                    // F3 F5
                    F7, F1,
                    // F6 F7 F8
                    F8, F5, F2,
                    // B0 B1 B2
                    B0, B1, B2,
                    // B3 B5
                    B3, B5,
                    // B6 B7 B8
                    B6, B7, B8,
                    // U0 U1 U2
                    U0, U1, U2,
                    // U3 U5
                    U3, U5,
                    // U6 U7 U8
                    L8, L5, L2,
                    // D0 D1 D2
                    R6, R3, R0,
                    // D3 D5
                    D3, D5,
                    // D6 D7 D8
                    D6, D7, D8,
                    // R0 R1 R2
                    U6, R1, R2,
                    // R3 R5
                    U7, R5,
                    // R6 R7 R8
                    U8, R7, R8,
                    // L0 L1 L2
                    L0, L1, D0,
                    // L3 L5
                    L3, D1,
                    // L6 L7 L8
                    L6, L7, D2
            },
            // B (B)
            {
                    // F0 F1 F2
                    F0, F1, F2,
                    // F3 F5
                    F3, F5,
                    // F6 F7 F8
                    F6, F7, F8,
                    // B0 B1 B2
                    B2, B5, B8,
                    // B3 B5
                    B1, B7,
                    // B6 B7 B8
                    B0, B3, B6,
                    // U0 U1 U2
                    L6, L3, L0,
                    // U3 U5
                    U3, U5,
                    // U6 U7 U8
                    U6, U7, U8,
                    // D0 D1 D2
                    D0, D1, D2,
                    // D3 D5
                    D3, D5,
                    // D6 D7 D8
                    R8, R5, R2,
                    // R0 R1 R2
                    R0, R1, U0,
                    // R3 R5
                    R3, U1,
                    // R6 R7 R8
                    R6, R7, U2,
                    // L0 L1 L2
                    D6, L1, L2,
                    // L3 L5
                    D7, L5,
                    // L6 L7 L8
                    D8, L7, L8
            },
            // B2 (B_DOUBLE)
            {
                    // F0 F1 F2
                    F0, F1, F2,
                    // F3 F5
                    F3, F5,
                    // F6 F7 F8
                    F6, F7, F8,
                    // B0 B1 B2
                    B8, B7, B6,
                    // B3 B5
                    B5, B3,
                    // B6 B7 B8
                    B2, B1, B0,
                    // U0 U1 U2
                    D8, D7, D6,
                    // U3 U5
                    U3, U5,
                    // U6 U7 U8
                    U6, U7, U8,
                    // D0 D1 D2
                    D0, D1, D2,
                    // D3 D5
                    D3, D5,
                    // D6 D7 D8
                    U2, U1, U0,
                    // R0 R1 R2
                    R0, R1, L6,
                    // R3 R5
                    R3, L3,
                    // R6 R7 R8
                    R6, R7, L0,
                    // L0 L1 L2
                    R8, L1, L2,
                    // L3 L5
                    R5, L5,
                    // L6 L7 L8
                    R2, L7, L8
            },
            // B' (B_REVERSE)
            {
                    // F0 F1 F2
                    F0, F1, F2,
                    // F3 F5
                    F3, F5,
                    // F6 F7 F8
                    F6, F7, F8,
                    // B0 B1 B2
                    B6, B3, B0,
                    // B3 B5
                    B7, B1,
                    // B6 B7 B8
                    B8, B5, B2,
                    // U0 U1 U2
                    R2, R5, R8,
                    // U3 U5
                    U3, U5,
                    // U6 U7 U8
                    U6, U7, U8,
                    // D0 D1 D2
                    D0, D1, D2,
                    // D3 D5
                    D3, D5,
                    // D6 D7 D8
                    L0, L3, L6,
                    // R0 R1 R2
                    R0, R1, D8,
                    // R3 R5
                    R3, D7,
                    // R6 R7 R8
                    R6, R7, D6,
                    // L0 L1 L2
                    U2, L1, L2,
                    // L3 L5
                    U1, L5,
                    // L6 L7 L8
                    U0, L7, L8
            },
            // U (U)
            {
                    // F0 F1 F2
                    L0, L1, L2,
                    // F3 F5
                    F3, F5,
                    // F6 F7 F8
                    F6, F7, F8,
                    // B0 B1 B2
                    R0, R1, R2,
                    // B3 B5
                    B3, B5,
                    // B6 B7 B8
                    B6, B7, B8,
                    // U0 U1 U2
                    U2, U5, U8,
                    // U3 U5
                    U1, U7,
                    // U6 U7 U8
                    U0, U3, U6,
                    // D0 D1 D2
                    D0, D1, D2,
                    // D3 D5
                    D3, D5,
                    // D6 D7 D8
                    D6, D7, D8,
                    // R0 R1 R2
                    F0, F1, F2,
                    // R3 R5
                    R3, R5,
                    // R6 R7 R8
                    R6, R7, R8,
                    // L0 L1 L2
                    B0, B1, B2,
                    // L3 L5
                    L3, L5,
                    // L6 L7 L8
                    L6, L7, L8
            },
            // U2 (U_DOUBLE)
            {
                    // F0 F1 F2
                    B0, B1, B2,
                    // F3 F5
                    F3, F5,
                    // F6 F7 F8
                    F6, F7, F8,
                    // B0 B1 B2
                    F0, F1, F2,
                    // B3 B5
                    B3, B5,
                    // B6 B7 B8
                    B6, B7, B8,
                    // U0 U1 U2
                    U8, U7, U6,
                    // U3 U5
                    U5, U3,
                    // U6 U7 U8
                    U2, U1, U0,
                    // D0 D1 D2
                    D0, D1, D2,
                    // D3 D5
                    D3, D5,
                    // D6 D7 D8
                    D6, D7, D8,
                    // R0 R1 R2
                    L0, L1, L2,
                    // R3 R5
                    R3, R5,
                    // R6 R7 R8
                    R6, R7, R8,
                    // L0 L1 L2
                    R0, R1, R2,
                    // L3 L5
                    L3, L5,
                    // L6 L7 L8
                    L6, L7, L8
            },
            // U' (U_REVERSE)
            {
                    // F0 F1 F2
                    R0, R1, R2,
                    // F3 F5
                    F3, F5,
                    // F6 F7 F8
                    F6, F7, F8,
                    // B0 B1 B2
                    L0, L1, L2,
                    // B3 B5
                    B3, B5,
                    // B6 B7 B8
                    B6, B7, B8,
                    // U0 U1 U2
                    U6, U3, U0,
                    // U3 U5
                    U7, U1,
                    // U6 U7 U8
                    U8, U5, U2,
                    // D0 D1 D2
                    D0, D1, D2,
                    // D3 D5
                    D3, D5,
                    // D6 D7 D8
                    D6, D7, D8,
                    // R0 R1 R2
                    B0, B1, B2,
                    // R3 R5
                    R3, R5,
                    // R6 R7 R8
                    R6, R7, R8,
                    // L0 L1 L2
                    F0, F1, F2,
                    // L3 L5
                    L3, L5,
                    // L6 L7 L8
                    L6, L7, L8
            },
            // D (D)
            {
                    // F0 F1 F2
                    F0, F1, F2,
                    // F3 F5
                    F3, F5,
                    // F6 F7 F8
                    R6, R7, R8,
                    // B0 B1 B2
                    B0, B1, B2,
                    // B3 B5
                    B3, B5,
                    // B6 B7 B8
                    L6, L7, L8,
                    // U0 U1 U2
                    U0, U1, U2,
                    // U3 U5
                    U3, U5,
                    // U6 U7 U8
                    U6, U7, U8,
                    // D0 D1 D2
                    D2, D5, D8,
                    // D3 D5
                    D1, D7,
                    // D6 D7 D8
                    D0, D3, D6,
                    // R0 R1 R2
                    R0, R1, R2,
                    // R3 R5
                    R3, R5,
                    // R6 R7 R8
                    B6, B7, B8,
                    // L0 L1 L2
                    L0, L1, L2,
                    // L3 L5
                    L3, L5,
                    // L6 L7 L8
                    F6, F7, F8
            },
            // D2 (D_DOUBLE)
            {
                    // F0 F1 F2
                    F0, F1, F2,
                    // F3 F5
                    F3, F5,
                    // F6 F7 F8
                    B6, B7, B8,
                    // B0 B1 B2
                    B0, B1, B2,
                    // B3 B5
                    B3, B5,
                    // B6 B7 B8
                    F6, F7, F8,
                    // U0 U1 U2
                    U0, U1, U2,
                    // U3 U5
                    U3, U5,
                    // U6 U7 U8
                    U6, U7, U8,
                    // D0 D1 D2
                    D8, D7, D6,
                    // D3 D5
                    D5, D3,
                    // D6 D7 D8
                    D2, D1, D0,
                    // R0 R1 R2
                    R0, R1, R2,
                    // R3 R5
                    R3, R5,
                    // R6 R7 R8
                    L6, L7, L8,
                    // L0 L1 L2
                    L0, L1, L2,
                    // L3 L5
                    L3, L5,
                    // L6 L7 L8
                    R6, R7, R8
            },
            // D' (D_REVERSE)
            {
                    // F0 F1 F2
                    F0, F1, F2,
                    // F3 F5
                    F3, F5,
                    // F6 F7 F8
                    L6, L7, L8,
                    // B0 B1 B2
                    B0, B1, B2,
                    // B3 B5
                    B3, B5,
                    // B6 B7 B8
                    R6, R7, R8,
                    // U0 U1 U2
                    U0, U1, U2,
                    // U3 U5
                    U3, U5,
                    // U6 U7 U8
                    U6, U7, U8,
                    // D0 D1 D2
                    D6, D3, D0,
                    // D3 D5
                    D7, D1,
                    // D6 D7 D8
                    D8, D5, D2,
                    // R0 R1 R2
                    R0, R1, R2,
                    // R3 R5
                    R3, R5,
                    // R6 R7 R8
                    F6, F7, F8,
                    // L0 L1 L2
                    L0, L1, L2,
                    // L3 L5
                    L3, L5,
                    // L6 L7 L8
                    B6, B7, B8
            },
            // R (R)
            {
                    // F0 F1 F2
                    F0, F1, U2,
                    // F3 F5
                    F3, U5,
                    // F6 F7 F8
                    F6, F7, U8,
                    // B0 B1 B2
                    D8, B1, B2,
                    // B3 B5
                    D5, B5,
                    // B6 B7 B8
                    D2, B7, B8,
                    // U0 U1 U2
                    U0, U1, B6,
                    // U3 U5
                    U3, B3,
                    // U6 U7 U8
                    U6, U7, B0,
                    // D0 D1 D2
                    D0, D1, F2,
                    // D3 D5
                    D3, F5,
                    // D6 D7 D8
                    D6, D7, F8,
                    // R0 R1 R2
                    R2, R5, R8,
                    // R3 R5
                    R1, R7,
                    // R6 R7 R8
                    R0, R3, R6,
                    // L0 L1 L2
                    L0, L1, L2,
                    // L3 L5
                    L3, L5,
                    // L6 L7 L8
                    L6, L7, L8
            },
            // R2 (R_DOUBLE)
            {
                    // F0 F1 F2
                    F0, F1, B6,
                    // F3 F5
                    F3, B3,
                    // F6 F7 F8
                    F6, F7, B0,
                    // B0 B1 B2
                    F8, B1, B2,
                    // B3 B5
                    F5, B5,
                    // B6 B7 B8
                    F2, B7, B8,
                    // U0 U1 U2
                    U0, U1, D2,
                    // U3 U5
                    U3, D5,
                    // U6 U7 U8
                    U6, U7, D8,
                    // D0 D1 D2
                    D0, D1, U2,
                    // D3 D5
                    D3, U5,
                    // D6 D7 D8
                    D6, D7, U8,
                    // R0 R1 R2
                    R8, R7, R6,
                    // R3 R5
                    R5, R3,
                    // R6 R7 R8
                    R2, R1, R0,
                    // L0 L1 L2
                    L0, L1, L2,
                    // L3 L5
                    L3, L5,
                    // L6 L7 L8
                    L6, L7, L8
            },
            // R' (R_REVERSE)
            {
                    // F0 F1 F2
                    F0, F1, D2,
                    // F3 F5
                    F3, D5,
                    // F6 F7 F8
                    F6, F7, D8,
                    // B0 B1 B2
                    U8, B1, B2,
                    // B3 B5
                    U5, B5,
                    // B6 B7 B8
                    U2, B7, B8,
                    // U0 U1 U2
                    U0, U1, F2,
                    // U3 U5
                    U3, F5,
                    // U6 U7 U8
                    U6, U7, F8,
                    // D0 D1 D2
                    D0, D1, B6,
                    // D3 D5
                    D3, B3,
                    // D6 D7 D8
                    D6, D7, B0,
                    // R0 R1 R2
                    R6, R3, R0,
                    // R3 R5
                    R7, R1,
                    // R6 R7 R8
                    R8, R5, R2,
                    // L0 L1 L2
                    L0, L1, L2,
                    // L3 L5
                    L3, L5,
                    // L6 L7 L8
                    L6, L7, L8
            },
            // L (L)
            {
                    // F0 F1 F2
                    D0, F1, F2,
                    // F3 F5
                    D3, F5,
                    // F6 F7 F8
                    D6, F7, F8,
                    // B0 B1 B2
                    B0, B1, U6,
                    // B3 B5
                    B3, U3,
                    // B6 B7 B8
                    B6, B7, U0,
                    // U0 U1 U2
                    F0, U1, U2,
                    // U3 U5
                    F3, U5,
                    // U6 U7 U8
                    F6, U7, U8,
                    // D0 D1 D2
                    B8, D1, D2,
                    // D3 D5
                    B5, D5,
                    // D6 D7 D8
                    B2, D7, D8,
                    // R0 R1 R2
                    R0, R1, R2,
                    // R3 R5
                    R3, R5,
                    // R6 R7 R8
                    R6, R7, R8,
                    // L0 L1 L2
                    L2, L5, L8,
                    // L3 L5
                    L1, L7,
                    // L6 L7 L8
                    L0, L3, L6
            },
            // L2 (L_DOUBLE)
            {
                    // F0 F1 F2
                    B8, F1, F2,
                    // F3 F5
                    B5, F5,
                    // F6 F7 F8
                    B2, F7, F8,
                    // B0 B1 B2
                    B0, B1, F6,
                    // B3 B5
                    B3, F3,
                    // B6 B7 B8
                    B6, B7, F0,
                    // U0 U1 U2
                    D0, U1, U2,
                    // U3 U5
                    D3, U5,
                    // U6 U7 U8
                    D6, U7, U8,
                    // D0 D1 D2
                    U0, D1, D2,
                    // D3 D5
                    U3, D5,
                    // D6 D7 D8
                    U6, D7, D8,
                    // R0 R1 R2
                    R0, R1, R2,
                    // R3 R5
                    R3, R5,
                    // R6 R7 R8
                    R6, R7, R8,
                    // L0 L1 L2
                    L8, L7, L6,
                    // L3 L5
                    L5, L3,
                    // L6 L7 L8
                    L2, L1, L0
            },
            // L' (L_REVERSE)
            {
                    // F0 F1 F2
                    U0, F1, F2,
                    // F3 F5
                    U3, F5,
                    // F6 F7 F8
                    U6, F7, F8,
                    // B0 B1 B2
                    B0, B1, D6,
                    // B3 B5
                    B3, D3,
                    // B6 B7 B8
                    B6, B7, D0,
                    // U0 U1 U2
                    B8, U1, U2,
                    // U3 U5
                    B5, U5,
                    // U6 U7 U8
                    B2, U7, U8,
                    // D0 D1 D2
                    F0, D1, D2,
                    // D3 D5
                    F3, D5,
                    // D6 D7 D8
                    F6, D7, D8,
                    // R0 R1 R2
                    R0, R1, R2,
                    // R3 R5
                    R3, R5,
                    // R6 R7 R8
                    R6, R7, R8,
                    // L0 L1 L2
                    L6, L3, L0,
                    // L3 L5
                    L7, L1,
                    // L6 L7 L8
                    L8, L5, L2
            },
    };
}

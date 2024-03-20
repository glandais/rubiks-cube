package io.github.glandais.rubikscube.model.solver.dummy;

import io.github.glandais.rubikscube.model.Cube3Model;
import io.github.glandais.rubikscube.model.FaceletEnum;
import io.github.glandais.rubikscube.model.rotation.RotationEnum;
import io.github.glandais.rubikscube.model.view.CubeVisibleOrientation;
import io.github.glandais.rubikscube.print.ConsolePrinter;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static io.github.glandais.rubikscube.model.FaceletEnum.B2;
import static io.github.glandais.rubikscube.model.FaceletEnum.F2;
import static io.github.glandais.rubikscube.model.FaceletEnum.L2;
import static io.github.glandais.rubikscube.model.FaceletEnum.R2;
import static io.github.glandais.rubikscube.model.FaceletEnum.U1;
import static io.github.glandais.rubikscube.model.FaceletEnum.U3;
import static io.github.glandais.rubikscube.model.FaceletEnum.U5;
import static io.github.glandais.rubikscube.model.FaceletEnum.U7;

public class DummySolverInstance {

    private static final boolean DEBUG = false;

    private static final ConsolePrinter consolePrinter = new ConsolePrinter();

    private final Cube3Model cube;

    @Getter
    private final List<RotationEnum> moves;
    private final String state;

    public DummySolverInstance(String state) {
        this.state = state;
        cube = new Cube3Model();
        if (DEBUG) {
            consolePrinter.print(cube);
        }
        List<RotationEnum> rotationEnums = RotationEnum.parse(state, null);
        for (RotationEnum rotationEnum : rotationEnums) {
            cube.apply(rotationEnum, false);
        }
        if (DEBUG) {
            consolePrinter.print(cube);
        }
        moves = new ArrayList<>();
    }

    public void solve() {
        solve(7);
    }

    public void solve(int toPhase) {
        solvePhase1();
        if (toPhase >= 2) {
            solvePhase2();
        }
    }

    public String getMovesNotation() {
        return moves.stream()
                .map(RotationEnum::getNotation)
                .collect(Collectors.joining(" "));
    }

    private void apply(String movesStr) {
        if (DEBUG) {
            System.out.println(movesStr);
        }
        List<RotationEnum> rotationEnums = RotationEnum.parse(movesStr, cube.getView());
        for (RotationEnum rotationEnum : rotationEnums) {
            moves.add(rotationEnum);
            cube.apply(rotationEnum, false);
        }
    }

    private void loop(Supplier<Boolean> checker, Runnable step, int maxIterations) {
        int i = 0;
        while (!checker.get()) {
            if (i > maxIterations) {
                System.err.println("FAILED");
                step.run();
                moves.clear();
                return;
            }
            step.run();
            i++;
        }
    }

    private boolean wellPlaced(FaceletEnum faceletEnum) {
        cube.setView(CubeVisibleOrientation.DEFAULT);
        return cube.getFacelet(faceletEnum) == faceletEnum;
    }

    private void solvePhase1() {
        loop(() -> wellPlaced(U7), () -> solvePhase1Edge(U7, CubeVisibleOrientation.DEFAULT), 100);
        loop(() -> wellPlaced(U5), () -> solvePhase1Edge(U5, CubeVisibleOrientation.UP_RIGHT), 100);
        loop(() -> wellPlaced(U1), () -> solvePhase1Edge(U1, CubeVisibleOrientation.UP_BACK), 100);
        loop(() -> wellPlaced(U3), () -> solvePhase1Edge(U3, CubeVisibleOrientation.UP_LEFT), 100);

        if (DEBUG) {
            System.out.println("Phase 1 solved");
            System.out.println("Moves : " + getMovesNotation());
        }
    }

    private void solvePhase1Edge(FaceletEnum edge, CubeVisibleOrientation cubeVisibleOrientation) {
        cube.setView(cubeVisibleOrientation);
        FaceletEnum faceletEnum = cube.find(edge);
        if (DEBUG) {
            System.out.println(edge + " is at " + faceletEnum);
        }
        // correctly placed
        if (faceletEnum == U7) {
            return;
        }
        String moves = switch (faceletEnum) {
            // move on D
            case U1 -> "B2";
            // move on D
            case U3 -> "L2";
            // move on D
            case U5 -> "R2";
            // move to F3
            case F1 -> "F'";
            // move to U7 without modifying other correct white edges
            case F3 -> "U L' U'";
            // move to F3
            case F5 -> "F2";
            // move to F3
            case F7 -> "F";
            // move to U7
            case D1 -> "F2";
            // move to D1
            case D3 -> "D";
            // move to D1
            case D5 -> "D'";
            // move to D1
            case D7 -> "D2";
            // move to F7
            case L1 -> "L2 D";
            // move to F7, keeping L1/U3
            case L3 -> "L' D L";
            // move to U7
            case L5 -> "F";
            // move to F7
            case L7 -> "D";
            // move to F7
            case R1 -> "R2 D'";
            // move to U7
            case R3 -> "F'";
            // move to F7, keeping R1/U5
            case R5 -> "R D' R'";
            // move to F7
            case R7 -> "D'";
            // move to F7
            case B1 -> "B2 D2";
            // move to F7, keeping B1/U1
            case B3 -> "B' D2 B";
            // move to F7, keeping B1/U1
            case B5 -> "B D2 B'";
            // move to F7
            case B7 -> "D2";
            default -> throw new IllegalStateException("White edge can't be at " + faceletEnum);
        };
        apply(moves);
    }

    private void solvePhase2() {

        loop(() -> wellPlaced(F2), () -> solvePhase2Corner(F2, CubeVisibleOrientation.DEFAULT), 100);
        loop(() -> wellPlaced(R2), () -> solvePhase2Corner(R2, CubeVisibleOrientation.UP_RIGHT), 100);
        loop(() -> wellPlaced(B2), () -> solvePhase2Corner(B2, CubeVisibleOrientation.UP_BACK), 100);
        loop(() -> wellPlaced(L2), () -> solvePhase2Corner(L2, CubeVisibleOrientation.UP_LEFT), 100);

        if (DEBUG) {
            System.out.println("Phase 2 solved");
            System.out.println("Moves : " + getMovesNotation());
        }
    }

    private void solvePhase2Corner(FaceletEnum corner, CubeVisibleOrientation view) {
        cube.setView(view);
        FaceletEnum faceletEnum = cube.find(corner);
        if (DEBUG) {
            System.out.println(corner + " is at " + faceletEnum);
        }
        // correctly placed
        if (faceletEnum == F2) {
            return;
        }
        String moves = switch (faceletEnum) {
            // FRD -> FRU (correct)
            case F8 -> "R' D' R";
            case D2 -> "F D F'";
            case R6 -> "R2 D' R2 D R2";

            // FRU -> FRD
            case R0, U8 -> "R' D' R D";

            // FLD -> FRD
            case F6, L8, D0 -> "D";

            // FLU -> FRD
            case F0, L2, U6 -> "F' D F";

            // BRD -> FRD
            case B6, R8, D8 -> "D'";

            // BRU -> FRD
            case B0, R2, U2 -> "B' D' B";

            // BLD -> FRD
            case B8, L6, D6 -> "D2";

            // BLU -> FRD
            case B2, L0, U0 -> "B D2 B'";

            default -> throw new IllegalStateException("Face edge can't be at " + faceletEnum);
        };
        apply(moves);
        if (!isPhase1Ok()) {
            throw new IllegalStateException("something was wrong with " + state);
        }
    }

    private boolean isPhase1Ok() {
        return wellPlaced(U1) && wellPlaced(U3) && wellPlaced(U5) && wellPlaced(U7);
    }

    private boolean isPhase2Ok() {
        return wellPlaced(F2) && wellPlaced(R2) && wellPlaced(B2) && wellPlaced(L2);
    }

}

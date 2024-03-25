package io.github.glandais.rubikscube.solver;

import io.github.glandais.rubikscube.model.Action;
import io.github.glandais.rubikscube.model.CornerEnum;
import io.github.glandais.rubikscube.model.Cube3Model;
import io.github.glandais.rubikscube.model.FaceletEnum;
import io.github.glandais.rubikscube.model.SideEnum;
import io.github.glandais.rubikscube.model.rotation.RotationEnum;
import io.github.glandais.rubikscube.model.view.CubeVisibleOrientation;
import io.github.glandais.rubikscube.print.ConsolePrinter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import static io.github.glandais.rubikscube.model.FaceletEnum.B1;
import static io.github.glandais.rubikscube.model.FaceletEnum.B2;
import static io.github.glandais.rubikscube.model.FaceletEnum.B3;
import static io.github.glandais.rubikscube.model.FaceletEnum.B5;
import static io.github.glandais.rubikscube.model.FaceletEnum.B7;
import static io.github.glandais.rubikscube.model.FaceletEnum.D0;
import static io.github.glandais.rubikscube.model.FaceletEnum.D1;
import static io.github.glandais.rubikscube.model.FaceletEnum.D2;
import static io.github.glandais.rubikscube.model.FaceletEnum.D3;
import static io.github.glandais.rubikscube.model.FaceletEnum.D5;
import static io.github.glandais.rubikscube.model.FaceletEnum.D6;
import static io.github.glandais.rubikscube.model.FaceletEnum.D7;
import static io.github.glandais.rubikscube.model.FaceletEnum.D8;
import static io.github.glandais.rubikscube.model.FaceletEnum.F1;
import static io.github.glandais.rubikscube.model.FaceletEnum.F2;
import static io.github.glandais.rubikscube.model.FaceletEnum.F3;
import static io.github.glandais.rubikscube.model.FaceletEnum.F5;
import static io.github.glandais.rubikscube.model.FaceletEnum.F7;
import static io.github.glandais.rubikscube.model.FaceletEnum.L1;
import static io.github.glandais.rubikscube.model.FaceletEnum.L2;
import static io.github.glandais.rubikscube.model.FaceletEnum.L3;
import static io.github.glandais.rubikscube.model.FaceletEnum.L5;
import static io.github.glandais.rubikscube.model.FaceletEnum.L7;
import static io.github.glandais.rubikscube.model.FaceletEnum.R1;
import static io.github.glandais.rubikscube.model.FaceletEnum.R2;
import static io.github.glandais.rubikscube.model.FaceletEnum.R3;
import static io.github.glandais.rubikscube.model.FaceletEnum.R5;
import static io.github.glandais.rubikscube.model.FaceletEnum.R7;
import static io.github.glandais.rubikscube.model.FaceletEnum.U1;
import static io.github.glandais.rubikscube.model.FaceletEnum.U3;
import static io.github.glandais.rubikscube.model.FaceletEnum.U5;
import static io.github.glandais.rubikscube.model.FaceletEnum.U7;
import static io.github.glandais.rubikscube.model.FaceletEnum.U8;

public class DummySolverInstance {

    private static final boolean DEBUG = false;

    private static final ConsolePrinter consolePrinter = new ConsolePrinter();

    private final Cube3Model cube;

    private final List<SolveMoves> moves;
    private final String state;

    public DummySolverInstance(String state) {
        this.state = state;
        cube = new Cube3Model();
        if (DEBUG) {
            consolePrinter.print(cube);
        }
        List<Action> actions = Action.parse(state, null);
        for (Action action : actions) {
            if (action instanceof RotationEnum rotationEnum) {
                cube.apply(rotationEnum, false);
            }
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
        if (toPhase >= 3) {
            solvePhase3();
        }
        if (toPhase >= 4) {
            solvePhase4();
        }
        if (toPhase >= 5) {
            solvePhase5();
        }
        if (toPhase >= 6) {
            solvePhase6();
        }
        if (toPhase >= 7) {
            solvePhase7();
        }
    }

    public List<SolveMoves> getMovesNotation() {
        return moves;
    }

    private void apply(String movesStr) {
        if (DEBUG) {
            System.out.println(movesStr);
        }
        List<Action> actions = Action.parse(movesStr, cube.getView());
        for (Action action : actions) {
            moves.getLast().getMoves().append(" ").append(action.getNotation());
            if (action instanceof RotationEnum rotationEnum) {
                cube.apply(rotationEnum, false);
            }
        }
    }

    private void loop(Supplier<Boolean> checker, int maxIterations) {
        int i = 0;
        while (!checker.get()) {
            if (i > maxIterations) {
                System.err.println("FAILED");
                System.err.println("something was wrong with " + state);
                consolePrinter.print(cube);
                System.out.println(checker.get());
//                throw new IllegalStateException("something was wrong with " + state);
                return;
            }
            i++;
        }
    }

    private boolean wellPlaced(FaceletEnum... faceletEnums) {
        cube.setView(CubeVisibleOrientation.DEFAULT);
        for (FaceletEnum faceletEnum : faceletEnums) {
            FaceletEnum cubeValue = cube.getFacelet(faceletEnum);
            if (cubeValue != faceletEnum) {
                return false;
            }
        }
        return true;
    }

    private void solvePhase1() {
        createPhase1Moves(CubeVisibleOrientation.DEFAULT);
        loop(() -> solvePhase1Edge(U7, CubeVisibleOrientation.DEFAULT), 100);
        createPhase1Moves(CubeVisibleOrientation.UP_RIGHT);
        loop(() -> solvePhase1Edge(U5, CubeVisibleOrientation.UP_RIGHT), 100);
        createPhase1Moves(CubeVisibleOrientation.UP_BACK);
        loop(() -> solvePhase1Edge(U1, CubeVisibleOrientation.UP_BACK), 100);
        createPhase1Moves(CubeVisibleOrientation.UP_LEFT);
        loop(() -> solvePhase1Edge(U3, CubeVisibleOrientation.UP_LEFT), 100);

        if (DEBUG) {
            System.out.println("Phase 1 solved");
            System.out.println("Moves : " + getMovesNotation());
        }
    }

    private void createPhase1Moves(CubeVisibleOrientation cubeVisibleOrientation) {
        StringBuilder sb = new StringBuilder(cubeVisibleOrientation.notation() + " ");
        moves.add(new SolveMoves(cubeVisibleOrientation.f().getColorName() + "/White edge", sb));
    }

    private boolean solvePhase1Edge(FaceletEnum edge, CubeVisibleOrientation cubeVisibleOrientation) {
        cube.setView(cubeVisibleOrientation);
        FaceletEnum faceletEnum = cube.find(edge);
        if (DEBUG) {
            System.out.println(edge + " is at " + faceletEnum);
        }
        // correctly placed
        if (faceletEnum == U7) {
            return true;
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
        return wellPlaced(edge);
    }

    private boolean isPhase1Ok() {
        return wellPlaced(U1, U3, U5, U7);
    }

    private void solvePhase2() {
        createPhase2Moves(CubeVisibleOrientation.DEFAULT);
        loop(() -> solvePhase2Corner(F2, CubeVisibleOrientation.DEFAULT), 100);
        createPhase2Moves(CubeVisibleOrientation.UP_RIGHT);
        loop(() -> solvePhase2Corner(R2, CubeVisibleOrientation.UP_RIGHT), 100);
        createPhase2Moves(CubeVisibleOrientation.UP_BACK);
        loop(() -> solvePhase2Corner(B2, CubeVisibleOrientation.UP_BACK), 100);
        createPhase2Moves(CubeVisibleOrientation.UP_LEFT);
        loop(() -> solvePhase2Corner(L2, CubeVisibleOrientation.UP_LEFT), 100);

        if (DEBUG) {
            System.out.println("Phase 2 solved");
            System.out.println("Moves : " + getMovesNotation());
        }
    }

    private void createPhase2Moves(CubeVisibleOrientation cubeVisibleOrientation) {
        StringBuilder sb = new StringBuilder(cubeVisibleOrientation.notation() + " ");
        moves.add(new SolveMoves(cubeVisibleOrientation.f().getColorName() + "/" +
                                 cubeVisibleOrientation.r().getColorName() +
                                 "/White corner", sb));
    }

    private boolean solvePhase2Corner(FaceletEnum corner, CubeVisibleOrientation view) {
        cube.setView(view);
        FaceletEnum faceletEnum = cube.find(corner);
        if (DEBUG) {
            System.out.println(corner + " is at " + faceletEnum);
        }
        // correctly placed
        if (faceletEnum == F2) {
            return true;
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
        return wellPlaced(corner);
    }

    private boolean isPhase2Ok() {
        return wellPlaced(F2, R2, B2, L2);
    }

    private void solvePhase3() {
        loop(this::solvePhase3Edges, 200);

        if (DEBUG) {
            System.out.println("Phase 3 solved");
            System.out.println("Moves : " + getMovesNotation());
        }
    }

    private boolean solvePhase3Edges() {

        creatPhase3Moves(CubeVisibleOrientation.DOWN_FRONT);
        solvePhase3Edge(F3, CubeVisibleOrientation.DOWN_FRONT, CubeVisibleOrientation.DOWN_LEFT);
        creatPhase3Moves(CubeVisibleOrientation.DOWN_LEFT);
        solvePhase3Edge(L3, CubeVisibleOrientation.DOWN_LEFT, CubeVisibleOrientation.DOWN_BACK);
        creatPhase3Moves(CubeVisibleOrientation.DOWN_BACK);
        solvePhase3Edge(B3, CubeVisibleOrientation.DOWN_BACK, CubeVisibleOrientation.DOWN_RIGHT);
        creatPhase3Moves(CubeVisibleOrientation.DOWN_RIGHT);
        solvePhase3Edge(R3, CubeVisibleOrientation.DOWN_RIGHT, CubeVisibleOrientation.DOWN_FRONT);

        if (!isPhase1Ok() || !isPhase2Ok()) {
            throw new IllegalStateException("something was wrong with " + state);
        }
        return isPhase3Ok();
    }

    private void creatPhase3Moves(CubeVisibleOrientation cubeVisibleOrientation) {
        StringBuilder sb = new StringBuilder(cubeVisibleOrientation.notation() + " ");
        moves.add(new SolveMoves(cubeVisibleOrientation.f().getColorName() + "/" +
                                 cubeVisibleOrientation.r().getColorName() +
                                 " edge", sb));
    }

    private void solvePhase3Edge(FaceletEnum edge, CubeVisibleOrientation view, CubeVisibleOrientation viewU5F5) {
        cube.setView(view);
        FaceletEnum faceletEnum = cube.find(edge);
        if (DEBUG) {
            System.out.println(edge + " is at " + faceletEnum);
        }

        // target is F5
        if (faceletEnum == F5) {
            return;
        }

        // U1 -> U5
        if (faceletEnum == U1) {
            apply("U");
            faceletEnum = cube.find(edge);
        }
        // U3 -> U5
        if (faceletEnum == U3) {
            apply("U2");
            faceletEnum = cube.find(edge);
        }
        // U7 -> U5
        if (faceletEnum == U7) {
            apply("U'");
            faceletEnum = cube.find(edge);
        }
        // U5 -> F5
        if (faceletEnum == U5) {
            cube.setView(viewU5F5);
            apply("U' L' U L U F U' F'");
            cube.setView(view);
            return;
        }

        final String f1Tof5 = "U R U' R' U' F' U F";
        // R3 -> B1
        if (faceletEnum == R3) {
            apply(f1Tof5);
            faceletEnum = cube.find(edge);
        }

        // R1 -> F1
        if (faceletEnum == R1) {
            apply("U");
            faceletEnum = cube.find(edge);
        }
        // B1 -> F1
        if (faceletEnum == B1) {
            apply("U2");
            faceletEnum = cube.find(edge);
        }
        // L1 -> F1
        if (faceletEnum == L1) {
            apply("U'");
            faceletEnum = cube.find(edge);
        }
        // F1 -> F5
        if (faceletEnum == F1) {
            apply(f1Tof5);
            return;
        }

        SolveMoves last = moves.getLast();
        last.setPhase(last.getPhase() + " - not yet possible");
        // move wrong edge (R3 -> B1)
        apply(f1Tof5);

    }

    private boolean isPhase3Ok() {
        return wellPlaced(F5, R5, B5, L5);
    }

    private void solvePhase4() {
        loop(this::solvePhase4YellowCross, 200);

        if (DEBUG) {
            System.out.println("Phase 4 solved");
            System.out.println("Moves : " + getMovesNotation());
        }
    }

    private boolean solvePhase4YellowCross() {
        createPhase4Moves(CubeVisibleOrientation.DOWN_FRONT);
        solvePhase4YellowCross(CubeVisibleOrientation.DOWN_FRONT);
        createPhase4Moves(CubeVisibleOrientation.DOWN_LEFT);
        solvePhase4YellowCross(CubeVisibleOrientation.DOWN_LEFT);
        createPhase4Moves(CubeVisibleOrientation.DOWN_BACK);
        solvePhase4YellowCross(CubeVisibleOrientation.DOWN_BACK);
        createPhase4Moves(CubeVisibleOrientation.DOWN_RIGHT);
        solvePhase4YellowCross(CubeVisibleOrientation.DOWN_RIGHT);

        if (!isPhase1Ok() || !isPhase2Ok() || !isPhase3Ok()) {
            throw new IllegalStateException("something was wrong with " + state);
        }
        return isPhase4Ok();
    }

    private void createPhase4Moves(CubeVisibleOrientation cubeVisibleOrientation) {
        StringBuilder sb = new StringBuilder(cubeVisibleOrientation.notation() + " ");
        moves.add(new SolveMoves(cubeVisibleOrientation.f().getColorName() + " - Yellow cross", sb));
    }

    private void solvePhase4YellowCross(CubeVisibleOrientation view) {
        cube.setView(view);
        String phase4Move = "F R U R' U' F'";
        if (cube.getFacelet(U1).getSide() != SideEnum.D &&
            cube.getFacelet(U3).getSide() != SideEnum.D &&
            cube.getFacelet(U5).getSide() != SideEnum.D &&
            cube.getFacelet(U7).getSide() != SideEnum.D
        ) {
            apply(phase4Move);
        }
        if (cube.getFacelet(U1).getSide() == SideEnum.D &&
            cube.getFacelet(U3).getSide() == SideEnum.D &&
            cube.getFacelet(U5).getSide() != SideEnum.D &&
            cube.getFacelet(U7).getSide() != SideEnum.D
        ) {
            apply("F U R U' R' F'");
        }
        if (cube.getFacelet(U1).getSide() != SideEnum.D &&
            cube.getFacelet(U3).getSide() == SideEnum.D &&
            cube.getFacelet(U5).getSide() == SideEnum.D &&
            cube.getFacelet(U7).getSide() != SideEnum.D
        ) {
            apply(phase4Move);
        }
    }

    private boolean isPhase4Ok() {
        cube.setView(CubeVisibleOrientation.DEFAULT);
        return cube.getFacelet(D1).getSide() == SideEnum.D &&
               cube.getFacelet(D3).getSide() == SideEnum.D &&
               cube.getFacelet(D5).getSide() == SideEnum.D &&
               cube.getFacelet(D7).getSide() == SideEnum.D;
    }

    private void solvePhase5() {
        moves.add(new SolveMoves("Yellow edges", new StringBuilder()));

        loop(this::solvePhase5YellowEdges, 200);

        if (DEBUG) {
            System.out.println("Phase 5 solved");
            System.out.println("Moves : " + getMovesNotation());
        }
    }

    private boolean solvePhase5YellowEdges() {
        cube.setView(CubeVisibleOrientation.DEFAULT);
        apply(CubeVisibleOrientation.DOWN_FRONT.notation());
        if (isPhase5Ok()) {
            return true;
        }
        if (cube.getFacelet(D3) == D1) {
            apply("D");
        }
        if (cube.getFacelet(D7) == D1) {
            apply("D2");
        }
        if (cube.getFacelet(D5) == D1) {
            apply("D'");
        }
        if (isPhase5Ok()) {
            return true;
        }
        String swapper = "R U R' U R U2 R' U";
        if (cube.getFacelet(D3) == D3) {
            // D5 <-> D7
            apply(CubeVisibleOrientation.DOWN_RIGHT.notation());
            cube.setView(CubeVisibleOrientation.DOWN_RIGHT);
            apply(swapper);
        } else {
            if (cube.getFacelet(D7) == D3) {
                // D3 <-> D7
                apply(CubeVisibleOrientation.DOWN_BACK.notation());
                cube.setView(CubeVisibleOrientation.DOWN_BACK);
                apply(swapper);
                // D5 <-> D7
                apply(CubeVisibleOrientation.DOWN_RIGHT.notation());
                cube.setView(CubeVisibleOrientation.DOWN_RIGHT);
                apply(swapper);
            } else {
                // D3 is in D5
                // D5 <-> D7
                apply(CubeVisibleOrientation.DOWN_RIGHT.notation());
                cube.setView(CubeVisibleOrientation.DOWN_RIGHT);
                apply(swapper);
                // D3 <-> D7
                apply(CubeVisibleOrientation.DOWN_BACK.notation());
                cube.setView(CubeVisibleOrientation.DOWN_BACK);
                apply(swapper);
            }
        }

        if (!isPhase1Ok() || !isPhase2Ok() || !isPhase3Ok() || !isPhase4Ok()) {
            throw new IllegalStateException("something was wrong with " + state);
        }
        return isPhase5Ok();
    }

    private boolean isPhase5Ok() {
        return wellPlaced(D1, D3, D5, D7, F7, R7, B7, L7);
    }

    private void solvePhase6() {
        moves.add(new SolveMoves("Yellow corners", new StringBuilder()));

        loop(this::solvePhase6YellowCornersPlaces, 200);

        if (DEBUG) {
            System.out.println("Phase 6 solved");
            System.out.println("Moves : " + getMovesNotation());
        }
    }

    private boolean solvePhase6YellowCornersPlaces() {
        if (isPhase6Ok()) {
            return true;
        }

        if (!phase6RightCorner(CornerEnum.DLF) &&
            !phase6RightCorner(CornerEnum.DFR) &&
            !phase6RightCorner(CornerEnum.DRB) &&
            !phase6RightCorner(CornerEnum.DBL)) {
            moves.add(new SolveMoves("Swap wrong yellow corners", new StringBuilder()));
            apply(CubeVisibleOrientation.DOWN_LEFT.notation());
            doSwapYellowCorners(CubeVisibleOrientation.DOWN_LEFT);
        }

        swapYellowCorners(CornerEnum.DLF, CubeVisibleOrientation.DOWN_FRONT);
        swapYellowCorners(CornerEnum.DFR, CubeVisibleOrientation.DOWN_RIGHT);
        swapYellowCorners(CornerEnum.DRB, CubeVisibleOrientation.DOWN_BACK);
        swapYellowCorners(CornerEnum.DBL, CubeVisibleOrientation.DOWN_LEFT);

        if (!isPhase1Ok() || !isPhase2Ok() || !isPhase3Ok() || !isPhase4Ok() || !isPhase5Ok()) {
            throw new IllegalStateException("something was wrong with " + state);
        }
        return isPhase6Ok();
    }

    private void swapYellowCorners(CornerEnum corner, CubeVisibleOrientation view) {
        moves.add(new SolveMoves(corner.getL().getSide().getColorName() + "/" +
                                 corner.getR().getSide().getColorName() + "/" +
                                 corner.getD().getSide().getColorName() +
                                 " corner", new StringBuilder()));
        apply(view.notation());
        if (!isPhase6Ok() && phase6RightCorner(corner)) {
            doSwapYellowCorners(view);
            if (!isPhase6Ok()) {
                doSwapYellowCorners(view);
            }
        }
    }

    private void doSwapYellowCorners(CubeVisibleOrientation view) {
        cube.setView(view);
        apply("U R U' L' U R' U' L");
    }

    private boolean isPhase6Ok() {
        return phase6RightCorner(CornerEnum.DLF) &&
               phase6RightCorner(CornerEnum.DFR) &&
               phase6RightCorner(CornerEnum.DRB) &&
               phase6RightCorner(CornerEnum.DBL);
    }

    private boolean phase6RightCorner(CornerEnum corner) {
        cube.setView(CubeVisibleOrientation.DEFAULT);
        return corner.getFacelets().equals(
                Set.of(
                        cube.getFacelet(corner.getD()),
                        cube.getFacelet(corner.getL()),
                        cube.getFacelet(corner.getR())
                )
        );
    }

    private void solvePhase7() {
        moves.add(new SolveMoves("Orient yellow corners", new StringBuilder()));
        apply(CubeVisibleOrientation.DOWN_FRONT.notation());
        cube.setView(CubeVisibleOrientation.DOWN_FRONT);
        for (FaceletEnum u8Target : List.of(D0, D6, D8, D2)) {
            if (cube.getFacelet(U8) != u8Target) {
                apply("R' D' R D R' D' R D");
            }
            if (cube.getFacelet(U8) != u8Target) {
                apply("R' D' R D R' D' R D");
            }
            apply("U");
        }

        if (DEBUG) {
            System.out.println("Phase 7 solved");
            System.out.println("Moves : " + getMovesNotation());
        }
    }

}

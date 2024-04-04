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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

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

    private void comment(String comment) {
        apply("(" + comment.replace('(', '[').replace(')', ']') + ")");
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

    public static Map<String, AtomicInteger> counters = new LinkedHashMap<>();

    public static Map<String, AtomicInteger> counterLooped = new LinkedHashMap<>();

    public static Map<String, Map<Integer, AtomicInteger>> counterLoops = new LinkedHashMap<>();

    private void loop(String loopName, Function<Integer, Boolean> checker, int maxIterations) {
        int i = 0;
        while (!checker.apply(i)) {
            if (i > maxIterations) {
                System.err.println("FAILED");
                System.err.println("something was wrong with " + state);
                consolePrinter.print(cube);
                System.out.println(checker.apply(i));
//                throw new IllegalStateException("something was wrong with " + state);
                return;
            }
            i++;
        }
        counterLoops.computeIfAbsent(loopName, s -> new TreeMap<>())
                .computeIfAbsent(i, c -> new AtomicInteger())
                .incrementAndGet();
        if (i > 0) {
            counterLooped.computeIfAbsent(loopName, s -> new AtomicInteger()).incrementAndGet();
        }
        counters.computeIfAbsent(loopName, s -> new AtomicInteger()).addAndGet(i);
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
        moves.add(new SolveMoves("White edges", new StringBuilder()));
        createPhase1Moves(CubeVisibleOrientation.DEFAULT);
        loop("p1u7", i -> solvePhase1Edge(U7, CubeVisibleOrientation.DEFAULT), 100);
        createPhase1Moves(CubeVisibleOrientation.UP_RIGHT);
        loop("p1u5", i -> solvePhase1Edge(U5, CubeVisibleOrientation.UP_RIGHT), 100);
        createPhase1Moves(CubeVisibleOrientation.UP_BACK);
        loop("p1u1", i -> solvePhase1Edge(U1, CubeVisibleOrientation.UP_BACK), 100);
        createPhase1Moves(CubeVisibleOrientation.UP_LEFT);
        loop("p1u3", i -> solvePhase1Edge(U3, CubeVisibleOrientation.UP_LEFT), 100);

        comment("White edges solved");

        if (DEBUG) {
            System.out.println("Phase 1 solved");
            System.out.println("Moves : " + getMovesNotation());
        }
    }

    private void createPhase1Moves(CubeVisibleOrientation cubeVisibleOrientation) {
        moves.add(new SolveMoves(cubeVisibleOrientation.f().getColorName() + "/White edge", new StringBuilder()));
        apply(cubeVisibleOrientation.notation());
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
        MovesComment movesComment = switch (faceletEnum) {
            // move on D
            case U1 -> new MovesComment("B2", "to D");
            // move on D
            case U3 -> new MovesComment("L2", "to D");
            // move on D
            case U5 -> new MovesComment("R2", "to D");
            // move to U7 without modifying other correct white edges
            case F1 -> new MovesComment("F U' R U", "Edge flip");
            // move to U7 without modifying other correct white edges
            case F3 -> new MovesComment("U L' U'", "Middle layer mirrored");
            // move to U7 without modifying other correct white edges
            case F5 -> new MovesComment("U' R U", "Middle layer");
            // move to U7 without modifying other correct white edges
            case F7 -> new MovesComment("F' U' R U", "Bottom Layer");
            // move to U7
            case D1 -> new MovesComment("F2", "to U7");
            // move to D1
            case D3 -> new MovesComment("D", "to D1");
            // move to D1
            case D5 -> new MovesComment("D'", "to D1");
            // move to D1
            case D7 -> new MovesComment("D2", "to D1");
            // move to F7
            case L1 -> new MovesComment("L2 D", "to F7");
            // move to F7, keeping L1/U3
            case L3 -> new MovesComment("L' D L", "to F7");
            // move to U7
            case L5 -> new MovesComment("F", "to U7");
            // move to F7
            case L7 -> new MovesComment("D", "to F7");
            // move to F7
            case R1 -> new MovesComment("R2 D'", "to F7");
            // move to U7
            case R3 -> new MovesComment("F'", "to U7");
            // move to F7, keeping R1/U5
            case R5 -> new MovesComment("R D' R'", "to F7");
            // move to F7
            case R7 -> new MovesComment("D'", "to F7");
            // move to F7
            case B1 -> new MovesComment("B2 D2", "to F7");
            // move to F7, keeping B1/U1
            case B3 -> new MovesComment("B' D2 B", "to F7");
            // move to F7, keeping B1/U1
            case B5 -> new MovesComment("B D2 B'", "to F7");
            // move to F7
            case B7 -> new MovesComment("D2", "to F7");
            default -> throw new IllegalStateException("White edge can't be at " + faceletEnum);
        };
        comment("From " + faceletEnum + " " + movesComment.comment());
        apply(movesComment.moves());
        return wellPlaced(edge);
    }

    private boolean isPhase1Ok() {
        return wellPlaced(U1, U3, U5, U7);
    }

    private void solvePhase2() {
        moves.add(new SolveMoves("First layer", new StringBuilder()));
        createPhase2Moves(CubeVisibleOrientation.DEFAULT);
        loop("p2f2", i -> solvePhase2Corner(F2, CubeVisibleOrientation.DEFAULT), 100);
        createPhase2Moves(CubeVisibleOrientation.UP_RIGHT);
        loop("p2r2", i -> solvePhase2Corner(R2, CubeVisibleOrientation.UP_RIGHT), 100);
        createPhase2Moves(CubeVisibleOrientation.UP_BACK);
        loop("p2b2", i -> solvePhase2Corner(B2, CubeVisibleOrientation.UP_BACK), 100);
        createPhase2Moves(CubeVisibleOrientation.UP_LEFT);
        loop("p2l2", i -> solvePhase2Corner(L2, CubeVisibleOrientation.UP_LEFT), 100);

        comment("First layer solved");

        if (DEBUG) {
            System.out.println("Phase 2 solved");
            System.out.println("Moves : " + getMovesNotation());
        }
    }

    private void createPhase2Moves(CubeVisibleOrientation cubeVisibleOrientation) {
        moves.add(new SolveMoves(cubeVisibleOrientation.f().getColorName() + "/" +
                                 cubeVisibleOrientation.r().getColorName() +
                                 "/White corner", new StringBuilder()));
        apply(cubeVisibleOrientation.notation());
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
        MovesComment movesComment = switch (faceletEnum) {
            // FRD -> FRU (correct)
            case F8 -> new MovesComment("R' D' R", "On frd - White sticker to the right");
            case D2 -> new MovesComment("F D F'", "On frd - White facing you");
            case R6 -> new MovesComment("R2 D' R2 D R2", "On frd - White pointing down");

            // FRU -> FRD
            case R0, U8 -> new MovesComment("R' D' R D", "From fru to frd");

            // FLD -> FRD
            case F6, L8, D0 -> new MovesComment("D", "From fld to frd");

            // FLU -> FRD
            case F0, L2, U6 -> new MovesComment("F' D F", "From flu to frd");

            // BRD -> FRD
            case B6, R8, D8 -> new MovesComment("D'", "From brd to frd");

            // BRU -> FRD
            case B0, R2, U2 -> new MovesComment("B' D' B", "From bru to frd");

            // BLD -> FRD
            case B8, L6, D6 -> new MovesComment("D2", "From bld to frd");

            // BLU -> FRD
            case B2, L0, U0 -> new MovesComment("B D2 B'", "From blu to frd");

            default -> throw new IllegalStateException("Face edge can't be at " + faceletEnum);
        };
        comment(movesComment.comment());
        apply(movesComment.moves());
        if (!isPhase1Ok()) {
            throw new IllegalStateException("something was wrong with " + state);
        }
        return wellPlaced(corner);
    }

    private boolean isPhase2Ok() {
        return wellPlaced(F2, R2, B2, L2);
    }

    private void solvePhase3() {
        moves.add(new SolveMoves("Second layer", new StringBuilder()));
        loop("p3", this::solvePhase3Edges, 200);

        comment("Second layer solved");

        if (DEBUG) {
            System.out.println("Phase 3 solved");
            System.out.println("Moves : " + getMovesNotation());
        }
    }

    private boolean solvePhase3Edges(int i) {

        for (int j = 0; j < 8; j++) {
            solvePhase3Edge(i, F3, CubeVisibleOrientation.DOWN_FRONT, CubeVisibleOrientation.DOWN_LEFT);
            solvePhase3Edge(i, L3, CubeVisibleOrientation.DOWN_LEFT, CubeVisibleOrientation.DOWN_BACK);
            solvePhase3Edge(i, B3, CubeVisibleOrientation.DOWN_BACK, CubeVisibleOrientation.DOWN_RIGHT);
            solvePhase3Edge(i, R3, CubeVisibleOrientation.DOWN_RIGHT, CubeVisibleOrientation.DOWN_FRONT);
        }
        if (!isPhase1Ok() || !isPhase2Ok()) {
            throw new IllegalStateException("something was wrong with " + state);
        }
        if (isPhase3Ok()) {
            return true;
        }
        if (!getPhase3Status(F3, CubeVisibleOrientation.DOWN_FRONT)) {
            phase3ExtractEdge(CubeVisibleOrientation.DOWN_FRONT);
        } else if (!getPhase3Status(L3, CubeVisibleOrientation.DOWN_LEFT)) {
            phase3ExtractEdge(CubeVisibleOrientation.DOWN_LEFT);
        } else if (!getPhase3Status(B3, CubeVisibleOrientation.DOWN_BACK)) {
            phase3ExtractEdge(CubeVisibleOrientation.DOWN_BACK);
        } else if (!getPhase3Status(R3, CubeVisibleOrientation.DOWN_RIGHT)) {
            phase3ExtractEdge(CubeVisibleOrientation.DOWN_RIGHT);
        }
        return isPhase3Ok();
    }

    private Boolean getPhase3Status(FaceletEnum edge, CubeVisibleOrientation view) {
        cube.setView(view);
        return cube.find(edge) == F5;
    }

    private void phase3ExtractEdge(CubeVisibleOrientation view) {
        moves.add(new SolveMoves("Extract " + view.f().getColorName() + "/" +
                                 view.r().getColorName() +
                                 " edge", new StringBuilder()));
        cube.setView(view);
        apply(view.notation());
        apply("U R U' R' U' F' U F");
    }

    private void solvePhase3Edge(int i, FaceletEnum edge, CubeVisibleOrientation view, CubeVisibleOrientation viewU5F5) {
        moves.add(new SolveMoves(view.f().getColorName() + "/" +
                                 view.r().getColorName() +
                                 " edge", new StringBuilder()));
        apply(view.notation());

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
            comment("From U1 to U5");
            apply("U");
            faceletEnum = cube.find(edge);
        }
        // U3 -> U5
        if (faceletEnum == U3) {
            comment("From U3 to U5");
            apply("U2");
            faceletEnum = cube.find(edge);
        }
        // U7 -> U5
        if (faceletEnum == U7) {
            comment("From U7 to U5");
            apply("U'");
            faceletEnum = cube.find(edge);
        }
        // U5 -> F5
        if (faceletEnum == U5) {
            comment("Left algorithm (U5 to F5)");
            cube.setView(viewU5F5);
            apply("U' L' U L U F U' F'");
            cube.setView(view);
            return;
        }

        // R1 -> F1
        if (faceletEnum == R1) {
            comment("From R1 to F1");
            apply("U");
            faceletEnum = cube.find(edge);
        }
        // B1 -> F1
        if (faceletEnum == B1) {
            comment("From B1 to F1");
            apply("U2");
            faceletEnum = cube.find(edge);
        }
        // L1 -> F1
        if (faceletEnum == L1) {
            comment("From L1 to F1");
            apply("U'");
            faceletEnum = cube.find(edge);
        }
        // F1 -> F5
        if (faceletEnum == F1) {
            comment("Right algorithm (F1 to F5)");
            apply("U R U' R' U' F' U F");
        }
    }

    private boolean isPhase3Ok() {
        return wellPlaced(F5, R5, B5, L5);
    }

    private void solvePhase4() {
        moves.add(new SolveMoves("Yellow cross", new StringBuilder()));
        solvePhase4YellowCross();

        comment("Yellow cross solved");

        if (DEBUG) {
            System.out.println("Phase 4 solved");
            System.out.println("Moves : " + getMovesNotation());
        }
    }

    private boolean solvePhase4YellowCross() {
        solvePhase4YellowCross(CubeVisibleOrientation.DOWN_FRONT);
        solvePhase4YellowCross(CubeVisibleOrientation.DOWN_LEFT);
        solvePhase4YellowCross(CubeVisibleOrientation.DOWN_BACK);
        solvePhase4YellowCross(CubeVisibleOrientation.DOWN_RIGHT);

        if (!isPhase1Ok() || !isPhase2Ok() || !isPhase3Ok()) {
            throw new IllegalStateException("something was wrong with " + state);
        }
        return isPhase4Ok();
    }

    private void solvePhase4YellowCross(CubeVisibleOrientation view) {
        apply(view.notation());
        cube.setView(view);
        String phase4Move = "F R U R' U' F'";
        if (cube.getFacelet(U1).getSide() != SideEnum.D &&
            cube.getFacelet(U3).getSide() != SideEnum.D &&
            cube.getFacelet(U5).getSide() != SideEnum.D &&
            cube.getFacelet(U7).getSide() != SideEnum.D
        ) {
            comment("\"dot\"");
            apply(phase4Move);
        }
        if (cube.getFacelet(U1).getSide() == SideEnum.D &&
            cube.getFacelet(U3).getSide() == SideEnum.D &&
            cube.getFacelet(U5).getSide() != SideEnum.D &&
            cube.getFacelet(U7).getSide() != SideEnum.D
        ) {
            comment("\"L\" shortcut");
            apply("F U R U' R' F'");
        }
        if (cube.getFacelet(U1).getSide() != SideEnum.D &&
            cube.getFacelet(U3).getSide() == SideEnum.D &&
            cube.getFacelet(U5).getSide() == SideEnum.D &&
            cube.getFacelet(U7).getSide() != SideEnum.D
        ) {
            comment("\"line\"");
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

        loop("p5", i -> solvePhase5YellowEdges(), 200);

        comment("Yellow edges solved");

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
            comment("Fix D1 : D3<->D1");
            apply("D");
        }
        if (cube.getFacelet(D7) == D1) {
            comment("Fix D1 : D7<->D1");
            apply("D2");
        }
        if (cube.getFacelet(D5) == D1) {
            comment("Fix D1 : D5<->D1");
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
            comment("Fix D5 and D7 : D5<->D7");
            apply(swapper);
        } else {
            if (cube.getFacelet(D7) == D3) {
                // D3 <-> D7
                apply(CubeVisibleOrientation.DOWN_BACK.notation());
                cube.setView(CubeVisibleOrientation.DOWN_BACK);
                comment("Fix D3 : D3<->D7");
                apply(swapper);
            } else {
                // D3 is in D5
                // D5 <-> D7
                apply(CubeVisibleOrientation.DOWN_RIGHT.notation());
                cube.setView(CubeVisibleOrientation.DOWN_RIGHT);
                comment("Move D3 (on opposite) : D7<->D5");
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
        moves.add(new SolveMoves("Yellow corners position", new StringBuilder()));

        solvePhase6YellowCornersPlaces();

        comment("Yellow corners solved");

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
            doSwapYellowCorners(CubeVisibleOrientation.DOWN_LEFT,
                    "Not a single corner OK, swap from here");
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
        if (!isPhase6Ok() && phase6RightCorner(corner)) {
            String comment = corner.getL().getSide().getColorName() + "/" +
                             corner.getR().getSide().getColorName() + "/" +
                             corner.getD().getSide().getColorName() + " OK, swap others";
            doSwapYellowCorners(view, comment);
            if (!isPhase6Ok()) {
                doSwapYellowCorners(view, "Corners not OK, swap again");
            }
        }
    }

    private void doSwapYellowCorners(CubeVisibleOrientation view, String comment) {
        apply(view.notation());
        cube.setView(view);
        comment(comment);
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
                comment("Move yellow to top");
                apply("R' D' R D R' D' R D");
            }
            if (cube.getFacelet(U8) != u8Target) {
                comment("Still not yellow");
                apply("R' D' R D R' D' R D");
            }
            comment("Yellow on top");
            apply("U");
        }
        comment("Orient yellow corners solved");

        apply(CubeVisibleOrientation.DEFAULT.notation());

        if (DEBUG) {
            System.out.println("Phase 7 solved");
            System.out.println("Moves : " + getMovesNotation());
        }
    }

}

package io.github.glandais.rubikscube.model.solver.dummy;

import io.github.glandais.rubikscube.Scrambles;
import io.github.glandais.rubikscube.solver.dummy.DummySolverInstance;

public class DummySolverTest {

    public static void main(String[] args) {
        solve("R' U' F U2 R D2 U2 L' U2 R' B2 L2 R' B2 R2 D R' F U2 L' B' R' U' R' U' F");
        solve("R' U' F R' F' D U2 F R2 B2 L2 B D2 U2 B' R2 B' L' D2 U' F L' D R' U' F");
        for (String scramble : Scrambles.getScrambles()) {
            solve(scramble);
        }
    }

    private static void solve(String scramble) {
        DummySolverInstance dummySolverInstance = new DummySolverInstance(scramble);
        dummySolverInstance.solve();
    }

}

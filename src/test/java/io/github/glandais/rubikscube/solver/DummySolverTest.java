package io.github.glandais.rubikscube.solver;

public class DummySolverTest {

    public static void main(String[] args) {
        for (String scramble : Scrambler.getScrambles()) {
            solve(scramble);
        }
    }

    private static void solve(String scramble) {
        DummySolverInstance dummySolverInstance = new DummySolverInstance(scramble);
        dummySolverInstance.solve();
    }

}

package io.github.glandais.rubikscube.solver;

public class DummySolver {

    public String solve(String state) {
        DummySolverInstance dummySolverInstance = new DummySolverInstance(state);
        dummySolverInstance.solve();
        return dummySolverInstance.getMovesNotation();
    }

}

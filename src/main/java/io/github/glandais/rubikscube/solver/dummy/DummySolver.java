package io.github.glandais.rubikscube.solver.dummy;

import io.github.glandais.rubikscube.solver.Solver;

public class DummySolver implements Solver {

    @Override
    public String solve(String state) {
        DummySolverInstance dummySolverInstance = new DummySolverInstance(state);
        dummySolverInstance.solve();
        return dummySolverInstance.getMovesNotation();
    }

}

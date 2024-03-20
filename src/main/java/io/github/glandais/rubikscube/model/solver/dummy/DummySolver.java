package io.github.glandais.rubikscube.model.solver.dummy;

import io.github.glandais.rubikscube.model.solver.Solver;

public class DummySolver implements Solver {

    @Override
    public String solve(String state) {
        DummySolverInstance dummySolverInstance = new DummySolverInstance(state);
        dummySolverInstance.solve();
        return dummySolverInstance.getMovesNotation();
    }

}

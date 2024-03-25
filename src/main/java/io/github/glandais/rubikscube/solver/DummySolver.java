package io.github.glandais.rubikscube.solver;

import java.util.List;

public class DummySolver {

    public List<SolveMoves> solve(String state) {
        DummySolverInstance dummySolverInstance = new DummySolverInstance(state);
        dummySolverInstance.solve();
        return dummySolverInstance.getMovesNotation();
    }

}

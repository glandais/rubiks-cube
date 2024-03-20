package io.github.glandais.rubikscube.model.solver.tnoodle;

import cs.min2phase.Search;
import cs.min2phase.Tools;
import io.github.glandais.rubikscube.model.solver.Solver;
import org.worldcubeassociation.tnoodle.puzzle.ThreeByThreeCubeFewestMovesPuzzle;

public class TNoodleSolver implements Solver {

    static {
        Search.init();
    }

    @Override
    public String solve(String moves) {
        String facelets = Tools.fromScramble(moves);
        return new Search().solution(facelets, 21, 100000000, 20000, 0);
    }

}

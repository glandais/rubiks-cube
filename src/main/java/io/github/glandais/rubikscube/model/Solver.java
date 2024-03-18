package io.github.glandais.rubikscube.model;

import cs.min2phase.Search;
import cs.min2phase.Tools;
import lombok.experimental.UtilityClass;
import org.worldcubeassociation.tnoodle.puzzle.ThreeByThreeCubeFewestMovesPuzzle;

@UtilityClass
public class Solver {

    static {
        Search.init();
    }

    public String scramble() {
        return new ThreeByThreeCubeFewestMovesPuzzle().generateScramble();
    }

    public String solve(String moves) {
        String facelets = Tools.fromScramble(moves);
        return new Search().solution(facelets, 21, 100000000, 20000, 0);
    }

}

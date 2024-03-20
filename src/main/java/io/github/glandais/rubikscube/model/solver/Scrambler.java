package io.github.glandais.rubikscube.model.solver;

import cs.min2phase.Search;
import lombok.experimental.UtilityClass;
import org.worldcubeassociation.tnoodle.puzzle.ThreeByThreeCubeFewestMovesPuzzle;

@UtilityClass
public class Scrambler {

    static {
        Search.init();
    }

    public String scramble() {
        return new ThreeByThreeCubeFewestMovesPuzzle().generateScramble();
    }

}

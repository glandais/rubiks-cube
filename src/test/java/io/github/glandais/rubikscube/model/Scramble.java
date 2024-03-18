package io.github.glandais.rubikscube.model;

import cs.min2phase.Search;
import cs.min2phase.Tools;
import org.worldcubeassociation.tnoodle.puzzle.ThreeByThreeCubeFewestMovesPuzzle;

public class Scramble {

    public static void main(String[] args) {
        ThreeByThreeCubeFewestMovesPuzzle puzzle = new ThreeByThreeCubeFewestMovesPuzzle();
        String s = puzzle.generateScramble();
        System.out.println(s);
        String facelets = Tools.fromScramble(s);
        System.out.println(facelets);
        Search search = new Search();
        String solution = search.solution(facelets, 20, 10000, 0, 0);
        System.out.println(solution);
    }

}

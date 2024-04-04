package io.github.glandais.rubikscube.solver;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class DummySolverTest {

    public static void main(String[] args) {
        for (String scramble : Scrambler.getScrambles()) {
            solve(scramble);
        }
        for (Map.Entry<String, AtomicInteger> entry : DummySolverInstance.counters.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue().get());
        }
        System.out.println("looped");
        for (Map.Entry<String, AtomicInteger> entry : DummySolverInstance.counterLooped.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue().get());
        }
        System.out.println("loops");
        for (Map.Entry<String, Map<Integer, AtomicInteger>> entry : DummySolverInstance.counterLoops.entrySet()) {
            System.out.println(entry.getKey());
            for (Map.Entry<Integer, AtomicInteger> integerEntry : entry.getValue().entrySet()) {
                System.out.println(integerEntry.getKey() + " : " + integerEntry.getValue());
            }
        }
    }

    private static void solve(String scramble) {
        DummySolverInstance dummySolverInstance = new DummySolverInstance(scramble);
        dummySolverInstance.solve();
    }

}

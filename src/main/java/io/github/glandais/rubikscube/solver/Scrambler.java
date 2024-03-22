package io.github.glandais.rubikscube.solver;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class Scrambler {

    @Getter
    private static final List<String> scrambles;

    private static final SecureRandom r = new SecureRandom();

    static {
        scrambles = new ArrayList<>();
        initScrambles();
    }

    @SneakyThrows
    private static void initScrambles() {
        BufferedReader br = new BufferedReader(new InputStreamReader(Scrambler.class.getResourceAsStream("/scrambles.txt")));
        String line;
        while ((line = br.readLine()) != null) {
            if (!line.isEmpty()) {
                scrambles.add(line);
            }
        }
    }

    public String scramble() {
        return scrambles.get(r.nextInt(scrambles.size()));
    }

}

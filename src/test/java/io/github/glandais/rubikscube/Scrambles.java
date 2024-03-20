package io.github.glandais.rubikscube;

import io.github.glandais.rubikscube.solver.Scrambler;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class Scrambles {

    @SneakyThrows
    public static void main(String[] args) {
        BufferedWriter br = new BufferedWriter(new FileWriter("scrambles.txt"));
        for (int i = 0; i < 10000; i++) {
            System.out.println(i);
            br.write(Scrambler.scramble() + "\n");
        }
        br.close();
    }

    @SneakyThrows
    public static List<String> getScrambles() {
        List<String> result = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader("scrambles.txt"));
        String line;
        while ((line = br.readLine()) != null) {
            if (!line.isEmpty()) {
                result.add(line);
            }
        }
        return result;
    }

}

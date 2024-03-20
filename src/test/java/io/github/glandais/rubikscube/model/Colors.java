package io.github.glandais.rubikscube.model;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

public class Colors {

    public static void main(String[] args) {

        Ansi ansi = Ansi.ansi();

        for (int index = 0; index < 256; index++) {
            ansi.fg(index).a("FG %d ".formatted(index));
        }

        System.out.println(ansi);

        ansi = Ansi.ansi();

        for (int index = 0; index < 256; index++) {
            ansi.bg(index).a("BG %d ".formatted(index));
        }

        System.out.println(ansi);
    }

}

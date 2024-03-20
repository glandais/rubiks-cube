package io.github.glandais.rubikscube.print;

import io.github.glandais.rubikscube.model.Cube3Model;
import io.github.glandais.rubikscube.model.view.CubeVisibleOrientation;
import io.github.glandais.rubikscube.model.FaceletEnum;
import io.github.glandais.rubikscube.model.SideEnum;

import java.util.ArrayList;
import java.util.List;

import static org.fusesource.jansi.Ansi.ansi;

public class ConsolePrinter {

    static int charWidth = 2;
    static int charHeight = 1;

    public void print(Cube3Model cube3Model) {
        CubeVisibleOrientation previousView = cube3Model.getView();
        cube3Model.setView(CubeVisibleOrientation.DEFAULT);
        printModelView(cube3Model);
        cube3Model.setView(previousView);
    }

    public void printModelView(Cube3Model cube3Model) {
        System.out.println(cube3Model.getNotation());
        List<Char> chars = new ArrayList<>();

        int squareWidth = 3 * charWidth + 1;
        int squareHeight = 3 * charHeight + 1;

        for (SideEnum sideEnum : SideEnum.values()) {
            int is = switch (sideEnum) {
                case F -> squareWidth;
                case B -> squareWidth * 3;
                case U -> squareWidth;
                case D -> squareWidth;
                case R -> squareWidth * 2;
                case L -> 0;
            };
            int js = switch (sideEnum) {
                case F -> squareHeight;
                case B -> squareHeight;
                case U -> 0;
                case D -> squareHeight * 2;
                case R -> squareHeight;
                case L -> squareHeight;
            };
            for (int j = 0; j < 3; j++) {
                for (int i = 0; i < 3; i++) {
                    int n = i + 3 * j;
                    int color;
                    char c1 = ' ';
                    char c2 = ' ';
                    if (n == 4) {
                        color = sideEnum.getAnsiColor();
                    } else {
                        FaceletEnum faceletEnum = FaceletEnum.of(sideEnum, n);
                        FaceletEnum at = cube3Model.getFacelet(faceletEnum);
                        color = at.getSide().getAnsiColor();
                        c1 = at.getSide().name().charAt(0);
                        c2 = ("" + at.getI()).charAt(0);
                    }
                    chars.add(new Char(is + i * charWidth, js + j * charHeight, c1, color));
                    chars.add(new Char(is + i * charWidth + 1, js + j * charHeight, c2, color));
                }
            }
        }
        int maxI = chars.stream().mapToInt(Char::i).max().getAsInt();
        int maxJ = chars.stream().mapToInt(Char::j).max().getAsInt();
        for (int j = 0; j <= maxJ; j++) {
            for (int i = 0; i <= maxI; i++) {
                int fi = i;
                int fj = j;
                Char c = chars.stream().filter(ch -> ch.i() == fi && ch.j() == fj).findFirst().orElse(null);
                if (c == null) {
                    System.out.print(" ");
                } else {
                    System.out.print(ansi().bg(c.bg()).a(c.c()).reset());
                }
            }
            System.out.println();
        }
        System.out.println();
    }

}

package io.github.glandais.rubikscube.print;

import io.github.glandais.rubikscube.model.Cube3Model;
import io.github.glandais.rubikscube.model.FaceletEnum;
import io.github.glandais.rubikscube.model.SideEnum;
import org.fusesource.jansi.Ansi;

import java.util.List;

import static org.fusesource.jansi.Ansi.ansi;

public class ConsolePrinter {

    static int si = 2;
    static int sj = 1;

    private record Position(SideEnum sideEnum, Ansi.Color ansiColor, int startI, int startJ) {

        public boolean mayPrint(Cube3Model cube3Model, int i, int j) {
            if (i >= startI * si && i < (startI + 3) * si &&
                    j >= startJ * sj && j < (startJ + 3) * sj
            ) {
                int ri = i - startI * si;
                int rj = j - startJ * sj;
                int di = ri / si;
                int dj = rj / sj;
                int k = di + 3 * dj;
                FaceletEnum faceletEnum = FaceletEnum.of(sideEnum, k);
                ri = ri % si;
                rj = rj % sj;

                String label;
                Ansi.Color color = ansiColor;
                if (faceletEnum == null) {
                    label = " ";
                } else {
                    faceletEnum = cube3Model.at(faceletEnum);
                    color = faceletEnum.getSide().getAnsiColor();
                    if (rj == 0 && ri == 0) {
                        label = faceletEnum.getSide().name();
                    } else if (rj == 0 && ri == 1) {
                        label = "" + (faceletEnum.getI() + 1);
                    } else {
                        label = "X";
                    }
                }
                System.out.print(
                        ansi().bgBright(color)
                                .fgBright(Ansi.Color.BLACK)
                                .a(label)
                                .reset()
                );
                return true;
            }
            return false;
        }
    }

    List<Position> positions = List.of(
            new Position(SideEnum.U, SideEnum.U.getAnsiColor(), 3, 0),
            new Position(SideEnum.L, SideEnum.L.getAnsiColor(), 0, 3),
            new Position(SideEnum.F, SideEnum.F.getAnsiColor(), 3, 3),
            new Position(SideEnum.R, SideEnum.R.getAnsiColor(), 6, 3),
            new Position(SideEnum.B, SideEnum.B.getAnsiColor(), 9, 3),
            new Position(SideEnum.D, SideEnum.D.getAnsiColor(), 3, 6)
    );

    public void print(Cube3Model cube3Model) {
        for (int j = 0; j < 3 * 3 * sj; j++) {
            for (int i = 0; i < 4 * 3 * si; i++) {
                boolean printed = false;
                for (Position position : positions) {
                    printed = printed || position.mayPrint(cube3Model, i, j);
                }
                if (!printed) {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

}

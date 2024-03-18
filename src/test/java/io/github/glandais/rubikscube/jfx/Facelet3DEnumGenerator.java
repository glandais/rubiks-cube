package io.github.glandais.rubikscube.jfx;

import io.github.glandais.rubikscube.model.SideEnum;

public class Facelet3DEnumGenerator {

    public static void main(String[] args) {
        for (SideEnum sideEnum : SideEnum.values()) {
            int k = 0;
            for (int j = 0; j < 3; j++) {
                for (int i = 0; i < 3; i++) {
                    if (k != 4) {
                        String faceleteEnum = sideEnum.name() + k;
                        int x = 0;
                        int y = 0;
                        int z = 0;
                        switch (sideEnum) {
                            case F -> {
                                x = i - 1;
                                y = 1 - j;
                                z = 2;
                            }
                            case B -> {
                                x = 1 - i;
                                y = 1 - j;
                                z = -2;
                            }
                            case U -> {
                                x = i - 1;
                                y = 2;
                                z = j - 1;
                            }
                            case D -> {
                                x = i - 1;
                                y = -2;
                                z = 1 - j;
                            }
                            case R -> {
                                x = 2;
                                y = 1 - j;
                                z = 1 - i;
                            }
                            case L -> {
                                x = -2;
                                y = 1 - j;
                                z = i - 1;
                            }
                        }
                        System.out.println(faceleteEnum + "(FaceletEnum." + faceleteEnum + "," + x + "," + y + "," + z + "),");
                    }
                    k++;
                }
            }
        }
    }

}

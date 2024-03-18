package io.github.glandais.rubikscube.model;

public class FaceletEnumGenerator {

    public static void main(String[] args) {
        for (SideEnum value : SideEnum.values()) {
            for (int i = 0; i < 9; i++) {
                if (i != 4) {
                    System.out.println(value.name() + i + "(SideEnum." + value.name() + "," + i + "),");
                }
            }
        }
    }

}

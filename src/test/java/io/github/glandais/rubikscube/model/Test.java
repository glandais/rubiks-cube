package io.github.glandais.rubikscube.model;

import io.github.glandais.rubikscube.print.ConsolePrinter;

public class Test {

    public static void main(String[] args) {
        ConsolePrinter consolePrinter = new ConsolePrinter();
        for (RotationEnum rotationEnum : RotationEnum.values()) {
            rotate(consolePrinter, rotationEnum);
        }
    }

    private static void rotate(ConsolePrinter consolePrinter, RotationEnum rotationEnum) {
        Cube3Model cube3Model = new Cube3Model();
        System.out.println("Start");
        consolePrinter.print(cube3Model);
        System.out.println(rotationEnum.getNotation());
        cube3Model.apply(rotationEnum);
        consolePrinter.print(cube3Model);
    }
}

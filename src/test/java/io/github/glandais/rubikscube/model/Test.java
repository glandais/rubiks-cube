package io.github.glandais.rubikscube.model;

import io.github.glandais.rubikscube.model.rotation.RotationEnum;
import io.github.glandais.rubikscube.model.view.CubeVisibleOrientation;
import io.github.glandais.rubikscube.print.ConsolePrinter;

public class Test {

    public static void main(String[] args) {
        ConsolePrinter consolePrinter = new ConsolePrinter();
        printCube(consolePrinter, CubeVisibleOrientation.DEFAULT);
//        printCube(consolePrinter, CubeVisibleOrientation.UP_RIGHT);
//        printCube(consolePrinter, CubeVisibleOrientation.UP_BACK);
//        printCube(consolePrinter, CubeVisibleOrientation.UP_LEFT);
        printCube(consolePrinter, CubeVisibleOrientation.DOWN_FRONT);
//        printCube(consolePrinter, CubeVisibleOrientation.DOWN_RIGHT);
//        printCube(consolePrinter, CubeVisibleOrientation.DOWN_BACK);
//        printCube(consolePrinter, CubeVisibleOrientation.DOWN_LEFT);
//        print(consolePrinter, CubeVisibleOrientation.DEFAULT);
//        print(consolePrinter, CubeVisibleOrientation.UP_RIGHT);
//        print(consolePrinter, CubeVisibleOrientation.UP_BACK);
//        print(consolePrinter, CubeVisibleOrientation.UP_LEFT);
//        print(consolePrinter, CubeVisibleOrientation.DOWN_FRONT);
//        print(consolePrinter, CubeVisibleOrientation.DOWN_RIGHT);
//        print(consolePrinter, CubeVisibleOrientation.DOWN_BACK);
//        print(consolePrinter, CubeVisibleOrientation.DOWN_LEFT);
    }

    private static void printCube(ConsolePrinter consolePrinter, CubeVisibleOrientation view) {
        Cube3Model cube3Model = new Cube3Model();
        cube3Model.setView(view);
        consolePrinter.printModelView(cube3Model);
        System.out.println("Find F1 : " + cube3Model.find(FaceletEnum.F1));
        System.out.println("Find F3 : " + cube3Model.find(FaceletEnum.F3));
        System.out.println("Find F5 : " + cube3Model.find(FaceletEnum.F5));
        System.out.println("Find F7 : " + cube3Model.find(FaceletEnum.F7));
    }

    private static void print(ConsolePrinter consolePrinter, CubeVisibleOrientation view) {
        for (RotationEnum rotationEnum : RotationEnum.values()) {
            rotate(consolePrinter, rotationEnum, view);
        }
    }

    private static void rotate(ConsolePrinter consolePrinter, RotationEnum rotationEnum, CubeVisibleOrientation view) {
        Cube3Model cube3Model = new Cube3Model();
        cube3Model.setView(view);
        System.out.println("Start");
        System.out.println(view.toString());
        consolePrinter.printModelView(cube3Model);
        System.out.println("View rotation : " + rotationEnum.getNotation());
        cube3Model.apply(rotationEnum, true);
        consolePrinter.printModelView(cube3Model);
    }
}

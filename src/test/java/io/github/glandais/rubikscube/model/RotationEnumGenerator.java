package io.github.glandais.rubikscube.model;

import io.github.glandais.rubikscube.model.rotation.AxisEnum;

import static io.github.glandais.rubikscube.model.rotation.AxisEnum.X;
import static io.github.glandais.rubikscube.model.rotation.AxisEnum.Y;
import static io.github.glandais.rubikscube.model.rotation.AxisEnum.Z;

public class RotationEnumGenerator {

    public static void main(String[] args) {
        // code changed since
        for (SideEnum sideEnum : SideEnum.values()) {
            getRotation(sideEnum.name(), sideEnum.name());
            getRotation(sideEnum + "_DOUBLE", sideEnum + "2");
            getRotation(sideEnum + "_REVERSE", sideEnum + "'");
        }
    }

    private static void getRotation(String enumName, String rotationName) {
        char move = rotationName.charAt(0);

        double angle = 90.0;
        if (rotationName.length() > 1) {
            char mod = rotationName.charAt(1);
            if (mod == '2') {
                angle = 180;
            } else if (mod == '\'') {
                angle = -90;
            }
        }

        AxisEnum axis;
        Integer x = null;
        Integer y = null;
        Integer z = null;
        switch (move) {
            case 'F': {
                axis = Z;
                z = 1;
                angle = -angle;
                break;
            }
            case 'B': {
                axis = Z;
                z = -1;
                break;
            }
            case 'U': {
                axis = Y;
                y = 1;
                angle = -angle;
                break;
            }
            case 'D': {
                axis = Y;
                y = -1;
                break;
            }
            case 'R': {
                axis = X;
                x = 1;
                angle = -angle;
                break;
            }
            case 'L': {
                axis = X;
                x = -1;
                break;
            }
            default:
                throw new IllegalArgumentException("Invalid rotation " + enumName);
        }
        System.out.println(enumName + "(" +
                "\"" + rotationName + "\", " +
                "AxisEnum." + axis.name() + "," +
                angle + "," +
                x + "," +
                y + "," +
                z +
                "),");
    }
}

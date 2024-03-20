package io.github.glandais.rubikscube.model;

import io.github.glandais.rubikscube.jfx.scene.Cube3;
import io.github.glandais.rubikscube.jfx.scene.Facelet;
import io.github.glandais.rubikscube.jfx.scene.Facelet3DEnum;
import io.github.glandais.rubikscube.jfx.scene.RotationModel;
import io.github.glandais.rubikscube.jfx.scene.RotationPlayModel;
import io.github.glandais.rubikscube.model.rotation.RotationEnum;

public class RotationMovesGenerator {

    public static void main(String[] args) {
        System.out.println("    public static final byte[][] NEW_POSITIONS = {");
        for (RotationEnum rotationEnum : RotationEnum.values()) {
            getRotationMoves(rotationEnum);
        }
        System.out.println("    };");
    }

    private static void getRotationMoves(RotationEnum rotation) {
        byte[] moves = getMoves(rotation);
        StringBuilder s = new StringBuilder("\n\t\t\t\t\t");
        for (int i = 0; i < moves.length; i++) {
            FaceletEnum current = FaceletEnum.values()[i];
            if (current.i == 0 || current.i == 6) {
                s.append("// " + FaceletEnum.values()[i] + " " + FaceletEnum.values()[i + 1] + " " + FaceletEnum.values()[i + 2]);
                s.append("\n\t\t\t\t\t");
            }
            if (current.i == 3) {
                s.append("// " + FaceletEnum.values()[i] + " " + FaceletEnum.values()[i + 1]);
                s.append("\n\t\t\t\t\t");
            }
            s.append(FaceletEnum.values()[moves[i]]);
            if (i != moves.length - 1) {
                s.append(",");
            }
            if (current.i == 2 || current.i == 5 || current.i == 8) {
                s.append("\n\t\t\t\t\t");
            }
        }
        System.out.println("        // " + rotation.getNotation() + " (" + rotation.name() + ")");
        System.out.println("                {" + s + "},");
    }

    private static byte[] getMoves(RotationEnum rotation) {
        Cube3 cube3 = new Cube3();
        RotationModel rotationModel = new RotationPlayModel(cube3, rotation, -1);
        rotationModel.applyRotation();
        byte[] newPositions = new byte[48];
        for (Facelet facelet : cube3.getFacelets().values()) {
            Facelet3DEnum newPosition = Facelet3DEnum.of(facelet.getCurrentPosition3D());
            newPositions[facelet.getInitialPosition().ordinal()] = (byte) newPosition.ordinal();
        }
        return newPositions;
    }

}

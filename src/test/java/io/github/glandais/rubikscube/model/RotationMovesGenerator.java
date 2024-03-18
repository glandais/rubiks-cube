package io.github.glandais.rubikscube.model;

import io.github.glandais.rubikscube.jfx.RubiksCubeInteract;

public class RotationMovesGenerator {

    public static void main(String[] args) {
        RubiksCubeInteract rubiksCubeInteract = new RubiksCubeInteract();
        System.out.println("    public static final byte[][] NEW_POSITIONS = {");
        for (RotationEnum rotationEnum : RotationEnum.values()) {
            getRotationMoves(rubiksCubeInteract, rotationEnum);
        }
        System.out.println("    };");
    }

    private static void getRotationMoves(RubiksCubeInteract rubiksCubeInteract, RotationEnum rotation) {
        byte[] moves = rubiksCubeInteract.getMoves(rotation);
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

}

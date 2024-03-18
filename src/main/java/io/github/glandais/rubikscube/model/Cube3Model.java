package io.github.glandais.rubikscube.model;

public class Cube3Model {

    byte[] facelets;

    public Cube3Model() {
        this.facelets = new byte[48];
        for (byte i = 0; i < 48; i++) {
            this.facelets[i] = i;
        }
    }

    public FaceletEnum at(FaceletEnum position) {
        return FaceletEnum.values()[facelets[position.ordinal()]];
    }

    public void apply(RotationEnum rotationEnum) {
        byte[] newPosition = RotationMoves.NEW_POSITIONS[rotationEnum.ordinal()];
        byte[] oldFacelets = this.facelets;
        this.facelets = new byte[48];
        for (byte i = 0; i < 48; i++) {
            this.facelets[newPosition[i]] = oldFacelets[i];
        }
    }

}

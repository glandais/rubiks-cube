package io.github.glandais.rubikscube.model;

public class FaceletConstantsGenerator {

    public static void main(String[] args) {
        for (FaceletEnum faceletEnum : FaceletEnum.values()) {
            System.out.println("    public static final byte " + faceletEnum + " = (byte) " + faceletEnum.ordinal() + ";");
        }
    }
}

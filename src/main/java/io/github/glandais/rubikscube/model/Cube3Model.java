package io.github.glandais.rubikscube.model;

import io.github.glandais.rubikscube.model.rotation.RotationEnum;
import io.github.glandais.rubikscube.model.rotation.moves.RotationMoves;
import io.github.glandais.rubikscube.model.view.CubeVisibleOrientation;
import io.github.glandais.rubikscube.model.view.RotatedCubes;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Cube3Model {

    static final FaceletEnum[] FACELET_ENUMS = FaceletEnum.values();
    final byte[] facelets = new byte[48];
    @Getter
    final List<RotationEnum> moveList = new ArrayList<>();
    @Getter
    @Setter
    CubeVisibleOrientation view = CubeVisibleOrientation.DEFAULT;

    public Cube3Model() {
        resetFacelets();
    }

    protected Cube3Model(Cube3Model cube3Model) {
        for (byte i = 0; i < 48; i++) {
            this.facelets[i] = cube3Model.facelets[i];
        }
    }

    public Cube3Model copy() {
        return new Cube3Model(this);
    }

    public void reset() {
        resetFacelets();
        moveList.clear();
        view = CubeVisibleOrientation.DEFAULT;
    }

    private void resetFacelets() {
        for (byte i = 0; i < 48; i++) {
            this.facelets[i] = i;
        }
    }

    public FaceletEnum getFacelet(FaceletEnum position) {
        return FACELET_ENUMS[getFacelet(position.ordinal())];
    }

    private void setFacelet(FaceletEnum position, FaceletEnum value) {
        setFacelet(position.ordinal(), value.ordinal());
    }

    public FaceletEnum find(FaceletEnum searched) {
        for (FaceletEnum position : FaceletEnum.values()) {
            if (getFacelet(position) == searched) {
                return position;
            }
        }
        throw new IllegalStateException("Missing " + searched);
    }

    public void apply(RotationEnum rotation, boolean fromView) {
        if (fromView) {
            rotation = RotationEnum.fromOtherSide(rotation, view);
        }
        moveList.add(rotation);
        byte[] newPosition = RotationMoves.NEW_POSITIONS[rotation.ordinal()];
        byte[] oldFacelets = new byte[48];
        System.arraycopy(facelets, 0, oldFacelets, 0, 48);
        for (byte i = 0; i < 48; i++) {
            this.facelets[newPosition[i]] = oldFacelets[i];
        }
    }

    public String getNotation() {
        return getMoveList()
                .stream()
                .map(RotationEnum::getNotation)
                .collect(Collectors.joining(" "));
    }

    private byte getFacelet(int position) {
        return facelets[getRealPosition(position)];
    }

    private void setFacelet(int position, int value) {
        facelets[getRealPosition(position)] = (byte) value;
    }

    private int getRealPosition(int position) {
        return RotatedCubes.getRealPosition(view, position);
    }

    public boolean isSolved() {
        for (byte i = 0; i < 48; i++) {
            if (this.facelets[i] != i) {
                return false;
            }
        }
        return true;
    }
}

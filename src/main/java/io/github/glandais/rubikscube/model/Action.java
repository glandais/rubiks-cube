package io.github.glandais.rubikscube.model;

import io.github.glandais.rubikscube.model.rotation.RotationEnum;
import io.github.glandais.rubikscube.model.view.CubeVisibleOrientation;
import io.github.glandais.rubikscube.model.view.ViewEnum;

import java.util.ArrayList;
import java.util.List;

public interface Action {

    static List<Action> parse(String moves, CubeVisibleOrientation cubeVisibleOrientation) {
        List<Action> result = new ArrayList<>();
        for (String move : moves.split(" ")) {
            if (move.length() == 3) {
                result.add(ViewEnum.forNotation(move));
            } else if (!move.isEmpty()) {
                RotationEnum rotation = RotationEnum.forNotation(move);
                if (cubeVisibleOrientation != null) {
                    rotation = RotationEnum.fromOtherSide(rotation, cubeVisibleOrientation);
                }
                result.add(rotation);
            }
        }
        return result;
    }

    String getNotation();

    Action reverse();
}

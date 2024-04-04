package io.github.glandais.rubikscube.model;

import io.github.glandais.rubikscube.model.rotation.RotationEnum;
import io.github.glandais.rubikscube.model.view.CubeVisibleOrientation;
import io.github.glandais.rubikscube.model.view.ViewEnum;

import java.util.ArrayList;
import java.util.List;

public interface Action {

    static List<Action> parse(String moves, CubeVisibleOrientation cubeVisibleOrientation) {
        List<Action> result = new ArrayList<>();
        boolean inComment = false;
        StringBuilder cur = new StringBuilder();
        for (int i = 0; i < moves.length(); i++) {
            char c = moves.charAt(i);
            if (inComment) {
                if (c == ')') {
                    result.add(new Comment(cur.toString()));
                    inComment = false;
                    cur = new StringBuilder();
                } else {
                    cur.append(c);
                }
                continue;
            } else if (c == '(') {
                inComment = true;
                cur = new StringBuilder();
                continue;
            }
            if (c == ' ') {
                if (!cur.isEmpty()) {
                    result.add(getAction(cur.toString(), cubeVisibleOrientation));
                }
                cur = new StringBuilder();
            } else {
                cur.append(c);
            }
        }
        if (!cur.isEmpty()) {
            result.add(getAction(cur.toString(), cubeVisibleOrientation));
        }
        return result;
    }

    static Action getAction(String move, CubeVisibleOrientation cubeVisibleOrientation) {
        if (move.length() == 3) {
            return ViewEnum.forNotation(move);
        } else {
            RotationEnum rotation = RotationEnum.forNotation(move);
            if (cubeVisibleOrientation != null) {
                rotation = RotationEnum.fromOtherSide(rotation, cubeVisibleOrientation);
            }
            return rotation;
        }
    }

    String getNotation();

    Action reverse();
}

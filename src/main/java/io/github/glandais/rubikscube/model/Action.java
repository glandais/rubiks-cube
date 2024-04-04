package io.github.glandais.rubikscube.model;

import io.github.glandais.rubikscube.model.rotation.RotationEnum;
import io.github.glandais.rubikscube.model.rotation.RotationModifierEnum;
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
        return simplify(result);
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

    static List<Action> simplify(List<Action> actions) {
        boolean modified = true;
        while (modified) {
            List<Action> newActions = new ArrayList<>();
            for (int i = 0; i < actions.size(); i++) {
                Action action = actions.get(i);
                boolean add = true;
                if (i != actions.size() - 1) {
                    Action next = actions.get(i + 1);
                    if (action instanceof ViewEnum && next instanceof ViewEnum) {
                        add = false;
                    } else if (action instanceof RotationEnum r1 && next instanceof RotationEnum r2) {
                        if (r1.getSide() == r2.getSide()) {
                            add = false;
                            RotationModifierEnum modifier = switch (r1.getModifier()) {
                                case NORMAL -> switch (r2.getModifier()) {
                                    case NORMAL -> RotationModifierEnum.DOUBLE;
                                    case REVERSE -> null;
                                    case DOUBLE -> RotationModifierEnum.REVERSE;
                                };
                                case REVERSE -> switch (r2.getModifier()) {
                                    case NORMAL -> null;
                                    case REVERSE -> RotationModifierEnum.DOUBLE;
                                    case DOUBLE -> RotationModifierEnum.NORMAL;
                                };
                                case DOUBLE -> switch (r2.getModifier()) {
                                    case NORMAL -> RotationModifierEnum.REVERSE;
                                    case REVERSE -> RotationModifierEnum.NORMAL;
                                    case DOUBLE -> null;
                                };
                            };
                            if (modifier != null) {
                                RotationEnum rotationEnum = RotationEnum.getRotationEnum(r1.getSide(), modifier);
                                newActions.add(rotationEnum);
                            }
                            i++;
                        }
                    }
                }
                if (add) {
                    newActions.add(action);
                }
            }

            modified = newActions.size() != actions.size();
            actions = newActions;
        }
        return actions;
    }

    String getNotation();

    Action reverse();
}

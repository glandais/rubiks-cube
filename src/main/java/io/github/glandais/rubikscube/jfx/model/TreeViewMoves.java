package io.github.glandais.rubikscube.jfx.model;

import io.github.glandais.rubikscube.model.Action;
import io.github.glandais.rubikscube.model.rotation.RotationEnum;

import java.util.List;

public class TreeViewMoves extends TreeViewItem {
    private final String desc;

    public TreeViewMoves(String desc, List<Action> actions) {
        super(actions);
        this.desc = desc;
    }

    @Override
    public String toString() {
        return desc + " (" + actions.size() + ")";
    }
}

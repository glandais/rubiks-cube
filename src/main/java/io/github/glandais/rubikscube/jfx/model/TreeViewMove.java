package io.github.glandais.rubikscube.jfx.model;

import io.github.glandais.rubikscube.model.Action;

import java.util.List;

public class TreeViewMove extends TreeViewItem {
    public TreeViewMove(Action action) {
        super(List.of(action));
    }

    @Override
    public String toString() {
        return actions.getFirst().toString();
    }
}

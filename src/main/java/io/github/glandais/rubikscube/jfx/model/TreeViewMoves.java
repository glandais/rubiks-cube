package io.github.glandais.rubikscube.jfx.model;

import javafx.scene.control.TreeItem;
import lombok.Setter;

import java.util.List;

public class TreeViewMoves extends TreeViewItem {
    private final String desc;
    @Setter
    private TreeItem<TreeViewItem> treeItem;

    public TreeViewMoves(String desc) {
        super(List.of());
        this.desc = desc;
    }

    @Override
    public String toString() {
        return desc + " (" + getMoveSize() + " moves)";
    }

    @Override
    protected long getMoveSize() {
        return super.getMoveSize() + treeItem.getChildren()
                .stream()
                .mapToLong(t -> t.getValue().getMoveSize())
                .sum();
    }
}

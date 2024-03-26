package io.github.glandais.rubikscube.jfx.model;

import io.github.glandais.rubikscube.model.Action;
import io.github.glandais.rubikscube.model.rotation.RotationEnum;
import javafx.scene.control.TreeItem;
import lombok.Getter;
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
        return desc + " (" + treeItem.getChildren().size() + " moves)";
    }

}

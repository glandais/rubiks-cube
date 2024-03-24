package io.github.glandais.rubikscube.jfx;

import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;

public class LabelTooltip extends Label {

    public LabelTooltip(String label, String tooltip) {
        super(label);
        setTooltip(new Tooltip(tooltip));
    }
}

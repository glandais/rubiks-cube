package io.github.glandais.rubikscube.jfx;

import javafx.beans.property.BooleanProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;

public class ActionButton extends Button {
    public ActionButton(BooleanProperty disable, String label, String tooltip, Runnable r) {
        super(label);
        setTooltip(new Tooltip(tooltip));
        setOnAction(e -> r.run());
        if (disable != null) {
            disable.bindBidirectional(disableProperty());
        }
    }
}

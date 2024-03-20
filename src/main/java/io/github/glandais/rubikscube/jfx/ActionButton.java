package io.github.glandais.rubikscube.jfx;

import javafx.scene.control.Button;

public class ActionButton extends Button {
    public ActionButton(String label, Runnable r) {
        super(label);
        setOnAction(e -> r.run());
    }
}

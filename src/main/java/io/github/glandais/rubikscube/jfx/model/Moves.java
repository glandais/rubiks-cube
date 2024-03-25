package io.github.glandais.rubikscube.jfx.model;

import io.github.glandais.rubikscube.model.Action;

import java.util.List;

public record Moves(String desc, List<Action> actions) {
}

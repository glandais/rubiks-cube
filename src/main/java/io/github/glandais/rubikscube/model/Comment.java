package io.github.glandais.rubikscube.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Comment implements Action {

    final String comment;

    @Override
    public String getNotation() {
        return "(" + comment + ")";
    }

    @Override
    public Action reverse() {
        return this;
    }

    @Override
    public String toString() {
        return comment;
    }
}

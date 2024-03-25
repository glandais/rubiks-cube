package io.github.glandais.rubikscube.jfx.model;

import io.github.glandais.rubikscube.model.Action;
import io.github.glandais.rubikscube.model.rotation.RotationEnum;
import lombok.Getter;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Getter
public abstract class TreeViewItem {

    private static final AtomicLong COUNTER = new AtomicLong(0);

    protected final List<Action> actions;

    private final long i;

    public TreeViewItem(List<Action> actions) {
        this.actions = actions;
        this.i = COUNTER.incrementAndGet();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TreeViewItem that = (TreeViewItem) o;

        return i == that.i;
    }

    @Override
    public int hashCode() {
        return (int) (i ^ (i >>> 32));
    }
}

package io.github.glandais.rubikscube.jfx.scene;

import io.github.glandais.rubikscube.model.view.ViewEnum;
import javafx.scene.transform.Rotate;
import lombok.Synchronized;

public class ViewModel extends ActionModel {

    private final Rotate rotateX;
    private final Rotate rotateY;
    private final int duration90;
    private final double endX;
    private final double endY;
    private long rotationStart;
    private double startX;
    private double startY;
    private long duration;

    public ViewModel(Rotate rotateX, Rotate rotateY, ViewEnum viewEnum, int duration90) {
        super();
        this.rotateX = rotateX;
        this.rotateY = rotateY;
        this.duration90 = duration90;
        this.rotationStart = -1;
        this.endX = viewEnum.getRotateX();
        this.endY = viewEnum.getRotateY();
    }

    @Override
    public void cancel() {
        rotateX.setAngle(startX);
        rotateY.setAngle(startY);
    }

    @Override
    public boolean tick() {
        if (rotationStart == -1) {
            rotationStart = System.currentTimeMillis();
            startX = rotateX.getAngle();
            startY = rotateY.getAngle();
            double atot = Math.hypot(startX - endX, startY - endY);
            this.duration = Math.round(this.duration90 * atot / 90.0);
        }
        long rotationElapsed = System.currentTimeMillis() - rotationStart;
        double ratio;
        if (duration <= 0) {
            ratio = 1.0;
        } else {
            ratio = 1.0 * rotationElapsed / duration;
        }
        if (ratio >= 1.0) {
            applyRotation();
            return true;
        } else {
            rotate(ratio);
            return false;
        }
    }

    private void applyRotation() {
        rotateX.setAngle(endX);
        rotateY.setAngle(endY);
    }

    @Synchronized
    public void rotate(double ratio) {
        double x = this.startX + ratio * (this.endX - this.startX);
        rotateX.setAngle(x);
        double y = this.startY + ratio * (this.endY - this.startY);
        rotateY.setAngle(y);
    }
}

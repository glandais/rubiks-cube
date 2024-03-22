package io.github.glandais.rubikscube;

import io.github.glandais.rubikscube.jfx.RubiksCubeApplication;
import io.github.glandais.rubikscube.jfx.RubiksCubeInteract;

public class Main {

    public static void main(String[] args) {
        RubiksCubeApplication.launchFromElseWhere(new RubiksCubeInteract());
    }

}

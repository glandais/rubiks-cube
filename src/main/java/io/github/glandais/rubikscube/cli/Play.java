package io.github.glandais.rubikscube.cli;

import io.github.glandais.rubikscube.jfx.RubiksCubeApplication;
import io.github.glandais.rubikscube.jfx.RubiksCubeInteract;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "play", mixinStandardHelpOptions = true)
public class Play implements Callable<Integer> {
    @Override
    public Integer call() {
        RubiksCubeApplication.launchFromElseWhere(new RubiksCubeInteract());
        return 0;
    }
}

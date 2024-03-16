package io.github.glandais.rubikscube.cli;

import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "play", mixinStandardHelpOptions = true)
public class Play implements Callable<Integer> {
    @Override
    public Integer call() {
        return 0;
    }
}

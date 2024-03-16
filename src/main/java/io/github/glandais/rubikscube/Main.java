package io.github.glandais.rubikscube;

import io.github.glandais.rubikscube.cli.Play;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "rubiks-cube", mixinStandardHelpOptions = true, subcommands = Play.class)
public class Main implements Callable<Integer> {

    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;

    @Override
    public Integer call() {
        throw new CommandLine.ParameterException(spec.commandLine(), "Missing required subcommand");
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }

}

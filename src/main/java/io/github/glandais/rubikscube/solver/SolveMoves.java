package io.github.glandais.rubikscube.solver;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SolveMoves {
    String phase;
    StringBuilder moves;
}

package io.github.glandais.rubikscube.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

import static io.github.glandais.rubikscube.model.FaceletEnum.B6;
import static io.github.glandais.rubikscube.model.FaceletEnum.B8;
import static io.github.glandais.rubikscube.model.FaceletEnum.D0;
import static io.github.glandais.rubikscube.model.FaceletEnum.D2;
import static io.github.glandais.rubikscube.model.FaceletEnum.D6;
import static io.github.glandais.rubikscube.model.FaceletEnum.D8;
import static io.github.glandais.rubikscube.model.FaceletEnum.F6;
import static io.github.glandais.rubikscube.model.FaceletEnum.F8;
import static io.github.glandais.rubikscube.model.FaceletEnum.L6;
import static io.github.glandais.rubikscube.model.FaceletEnum.L8;
import static io.github.glandais.rubikscube.model.FaceletEnum.R6;
import static io.github.glandais.rubikscube.model.FaceletEnum.R8;

@RequiredArgsConstructor
@Getter
public enum CornerEnum {
    DLF(D0, L8, F6),
    DFR(D2, F8, R6),
    DRB(D8, R8, B6),
    DBL(D6, B8, L6);

    final FaceletEnum d;
    final FaceletEnum l;
    final FaceletEnum r;

    final Set<FaceletEnum> facelets;

    CornerEnum(FaceletEnum d, FaceletEnum l, FaceletEnum r) {
        this.d = d;
        this.l = l;
        this.r = r;
        this.facelets = Set.of(d, l, r);
    }
}

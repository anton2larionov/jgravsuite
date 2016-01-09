package by.geo.grav;

import by.geo.ref.Ellipsoid;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Optional;

/**
 * Провайдер глобальных моделей гравитационного поля.
 */
public enum GfmRepo {
    EGM08 {
        @Override
        @NotNull
        public Optional<? extends GravFieldModel>
        get(@NotNull final String GFC, @NotNull final Ellipsoid ell) {
            Optional<EGM08> opt;
            try {
                opt = Optional.of(new EGM08(GFC, ell));
            } catch (IOException e) {
                opt = Optional.empty();
            }
            return opt;
        }
    },

    EIGEN6C4 {
        @Override
        @NotNull
        public Optional<? extends GravFieldModel>
        get(@NotNull final String GFC, @NotNull final Ellipsoid ell) {
            Optional<EIGEN6C4> opt;
            try {
                opt = Optional.of(new EIGEN6C4(GFC, ell));
            } catch (IOException e) {
                opt = Optional.empty();
            }
            return opt;
        }
    };

    @NotNull
    public abstract Optional<? extends GravFieldModel>
    get(@NotNull final String GFC, final @NotNull Ellipsoid ell);

}

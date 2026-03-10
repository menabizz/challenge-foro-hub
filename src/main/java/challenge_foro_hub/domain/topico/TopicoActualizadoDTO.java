package challenge_foro_hub.domain.topico;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record TopicoActualizadoDTO(
        @NotNull Long id,
        String titulo,
        String mensaje,
        Status status,
        @NotNull Long usuario_Id,
        String curso,
        LocalDateTime fecha
) {
}

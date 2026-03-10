package challenge_foro_hub.domain.topico;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record TopicoDTO(
        @NotNull(message = "El título no se puede repetir.")
        String titulo,

        @NotNull(message = "Utilice un lenguaje apropiado en el mensaje que no supere los 700 caracteres de longitud.")
        String mensaje,

        @NotNull(message = "Seleccione uno de los Estados ´ACTIVO´ o ´INACTIVO´")
        Status status,

        @NotNull(message = "Utilice su ID de autor de usuario_Id")
        Long usuario_Id,

        @NotNull(message = "Recuerda utilizar el curso apropiado para tu publicación.")
        String curso,

        LocalDateTime fecha
) {
}

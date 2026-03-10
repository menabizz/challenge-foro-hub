package challenge_foro_hub.domain.topico;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ListarTopicosDTO(
        Long id,
        String titulo,
        String mensaje,
        Status status,
        Long usuario_Id,
        String curso,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime fecha
) {

    public ListarTopicosDTO (Topico topico){
        this(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getStatus(),
                topico.getAuthor().getId(),
                topico.getCurso(),
                topico.getFecha());

    }
}
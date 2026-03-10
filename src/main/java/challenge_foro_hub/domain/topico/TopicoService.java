package challenge_foro_hub.domain.topico;

import challenge_foro_hub.domain.usuario.UsuarioRepository;
import challenge_foro_hub.infra.exceptions.ValidacionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TopicoService {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public ListarTopicosDTO topicoCreado(TopicoDTO topicoDTO){
        if (!usuarioRepository.findById(topicoDTO.usuario_Id()).isPresent()){
            throw new ValidacionException("Este ID de usuario no está registrado en la base de datos.");
        }
        var title= topicoDTO.titulo();
        if (title != null && topicoRepository.existsByTituloIgnoreCase(title)){
            throw new ValidacionException("Este título ya está presente en la base de datos. Por favor revise el tópico existente.");
        }
        String message = topicoDTO.mensaje();
        if (message != null && topicoRepository.existsByMensajeIgnoreCase(message)){
            throw new ValidacionException("Este mensaje ya está presente en la base de datos. Por favor revise el tópico existente.");
        }
        var usuario = usuarioRepository.findById(topicoDTO.usuario_Id()).get();
        var topicoId= new Topico(null,title,message,topicoDTO.fecha(),topicoDTO.status(),usuario,topicoDTO.curso());
        topicoRepository.save(topicoId);
        return new ListarTopicosDTO(topicoId);
    }
}

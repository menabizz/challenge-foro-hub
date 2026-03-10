package challenge_foro_hub.controller;

import challenge_foro_hub.domain.usuario.RegistroUsuarioDTO;
import challenge_foro_hub.domain.usuario.RespuestaUsuarioDTO;
import challenge_foro_hub.domain.usuario.UsuarioRepository;
import challenge_foro_hub.domain.usuario.UsuarioService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;

@RestController
@RequestMapping("/registro")
public class RegistroController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    @Transactional
    public ResponseEntity<RespuestaUsuarioDTO> registrarUsuario(
            @RequestBody @Valid RegistroUsuarioDTO registroUsuarioDTO,
            UriComponentsBuilder uriBuilder) {

        var usuario = usuarioService.registrarUsuario(registroUsuarioDTO);

        var respuesta = new RespuestaUsuarioDTO(
                usuario.getId(),
                usuario.getNombre()
        );

        var uri = uriBuilder
                .path("/usuarios/{id}")
                .buildAndExpand(usuario.getId())
                .toUri();

        return ResponseEntity.created(uri).body(respuesta);
    }
}
package challenge_foro_hub.controller;

import challenge_foro_hub.domain.topico.*;
import challenge_foro_hub.infra.exceptions.ValidacionException;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@ResponseBody
@RequestMapping("/topicos")
@SecurityRequirement(name="bearer-key")
public class TopicoController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private TopicoService topicoService;



    @PostMapping
    @Transactional
    public ResponseEntity registrarTopico(@RequestBody @Valid TopicoDTO topicoDTO) throws ValidacionException {
        var topicoRegistrado = topicoService.topicoCreado(topicoDTO);
        return ResponseEntity.ok(topicoRegistrado);
    }


    @GetMapping
    public ResponseEntity<Page<ListarTopicosDTO>>  listarTopicos(@PageableDefault(size = 10) Pageable paged){
        return ResponseEntity.ok(topicoRepository.findByActiveTrue(paged).map(ListarTopicosDTO::new));
    }


    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<ListarTopicosDTO> actualizarTopico(@RequestBody @Valid TopicoActualizadoDTO topicoActualizadoDTO){
        Topico topico = topicoRepository.getReferenceById(topicoActualizadoDTO.id());
        topico.topicoActualizado(topicoActualizadoDTO);

        return ResponseEntity.ok(new ListarTopicosDTO(topico));
    }


    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity eliminarTopico(@PathVariable Long id){
        Topico topico= topicoRepository.getReferenceById(id);
        topico.desactivarTopico();
        return ResponseEntity.noContent().build();
    }


// OBTENER TOPICO A TRAVÉS DEL ID
    @GetMapping("/{id}")
    public ResponseEntity<ListarTopicosDTO> respuestaTopico(@PathVariable Long id){
        Topico topico = topicoRepository.getReferenceById(id);
        return ResponseEntity.ok(new ListarTopicosDTO(topico));
    }
}

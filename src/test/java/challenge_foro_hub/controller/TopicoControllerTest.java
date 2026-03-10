package challenge_foro_hub.controller;

import challenge_foro_hub.domain.topico.*;
import challenge_foro_hub.domain.usuario.Usuario;
import challenge_foro_hub.domain.usuario.UsuarioRepository;
import challenge_foro_hub.infra.exceptions.GlobalExceptionHandler;
import challenge_foro_hub.infra.exceptions.ValidacionException;
import challenge_foro_hub.infra.security.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TopicoController.class)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class TopicoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TopicoRepository topicoRepository;

    @MockBean
    private TopicoService topicoService;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @MockBean
    private TokenService tokenService;

    @Test
    void deberiaRegistrarTopico() throws Exception {
        TopicoDTO dto = new TopicoDTO(
                "Titulo test",
                "Mensaje test",
                Status.ACTIVO,
                1L,
                "Spring",
                LocalDateTime.now()
        );

        ListarTopicosDTO respuesta =
                new ListarTopicosDTO(
                        1L,
                        "Titulo test",
                        "Mensaje test",
                        Status.ACTIVO,
                        1L,
                        "Spring",
                        LocalDateTime.now()
                );

        when(topicoService.topicoCreado(any())).thenReturn(respuesta);

        mockMvc.perform(post("/topicos")
                        .with(user("usuario"))   // 👈 ESTA ES LA CLAVE
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void deberiaFallarSiUsuarioNoExiste() throws Exception {
        TopicoDTO dto = new TopicoDTO(
                "Titulo test",
                "Mensaje test",
                Status.ACTIVO,
                999L,  // usuario inexistente
                "Spring",
                LocalDateTime.now()
        );

        // ⚡ Simular que topicoService lanzará la excepción
        when(topicoService.topicoCreado(any()))
                .thenThrow(new ValidacionException("Este ID de usuario no está registrado en la base de datos."));

        mockMvc.perform(post("/topicos")
                        .with(user("usuario"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof ValidacionException))
                .andExpect(result ->
                        assertEquals("Este ID de usuario no está registrado en la base de datos.",
                                result.getResolvedException().getMessage()));
    }

    @Test
    void deberiaFallarSiTituloDuplicado() throws Exception {
        TopicoDTO dto = new TopicoDTO(
                "Titulo duplicado",
                "Mensaje único",
                Status.ACTIVO,
                1L,
                "Spring",
                LocalDateTime.now()
        );

        when(topicoService.topicoCreado(any()))
                .thenThrow(new ValidacionException("Este título ya está presente en la base de datos. Por favor revise el tópico existente."));

        mockMvc.perform(post("/topicos")
                        .with(user("usuario"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof ValidacionException))
                .andExpect(result ->
                        assertEquals("Este título ya está presente en la base de datos. Por favor revise el tópico existente.",
                                result.getResolvedException().getMessage()));
    }

    @Test
    void deberiaFallarSiMensajeDuplicado() throws Exception {
        TopicoDTO dto = new TopicoDTO(
                "Titulo único",
                "Mensaje duplicado",
                Status.ACTIVO,
                1L,
                "Spring",
                LocalDateTime.now()
        );

        when(topicoService.topicoCreado(any()))
                .thenThrow(new ValidacionException("Este mensaje ya está presente en la base de datos. Por favor revise el tópico existente."));

        mockMvc.perform(post("/topicos")
                        .with(user("usuario"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof ValidacionException))
                .andExpect(result ->
                        assertEquals("Este mensaje ya está presente en la base de datos. Por favor revise el tópico existente.",
                                result.getResolvedException().getMessage()));
    }

    @Test
    void deberiaFallarSiCamposNulos() throws Exception {
        TopicoDTO dto = new TopicoDTO(
                null, // título nulo
                null, // mensaje nulo
                null, // status nulo
                null, // usuario nulo
                null, // curso nulo
                null
        );

        mockMvc.perform(post("/topicos")
                        .with(user("usuario"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest()); // Spring valida @NotNull automáticamente
    }




    @Test
    void deberiaListarTopicos() throws Exception {

        Page<Topico> page = new PageImpl<>(List.of());

        when(topicoRepository.findByActiveTrue(any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/topicos")
                        .with(user("usuario")))
                .andExpect(status().isOk());
    }

    @Test
    void deberiaRetornarTopicoPorId() throws Exception {

        Usuario usuario = new Usuario(1L, "Nombre", "email@email.com", "123456", true);

        Topico topico = new Topico(
                1L,
                "Titulo test",
                "Mensaje test",
                LocalDateTime.now(),
                Status.ACTIVO,
                usuario,
                "Spring"
        );

        when(topicoRepository.getReferenceById(1L)).thenReturn(topico);

        mockMvc.perform(get("/topicos/1")
                        .with(user("usuario")))
                .andExpect(status().isOk());
    }

    @Test
    void deberiaActualizarTopico() throws Exception {

        Usuario usuario = new Usuario(1L, "Nombre", "email@email.com", "123456", true);

        Topico topico = new Topico(
                1L,
                "Titulo viejo",
                "Mensaje viejo",
                LocalDateTime.now(),
                Status.ACTIVO,
                usuario,
                "Spring"
        );

        when(topicoRepository.getReferenceById(1L)).thenReturn(topico);

        String json = """
            {
              "id":1,
              "titulo":"Titulo actualizado",
              "mensaje":"Mensaje actualizado",
              "status":"ACTIVO",
              "usuario_Id":1,
              "curso":"Spring",
              "fecha":"2026-03-10T18:00:00"
            }
            """;

        mockMvc.perform(put("/topicos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(csrf())
                        .with(user("usuario")))
                .andExpect(status().isOk());
    }

    @Test
    void deberiaEliminarTopico() throws Exception {

        Usuario usuario = new Usuario(1L, "Nombre", "email@email.com", "123456", true);

        Topico topico = new Topico(
                1L,
                "Titulo test",
                "Mensaje test",
                LocalDateTime.now(),
                Status.ACTIVO,
                usuario,
                "Spring"
        );

        when(topicoRepository.getReferenceById(1L)).thenReturn(topico);

        mockMvc.perform(delete("/topicos/1")
                        .with(csrf())
                        .with(user("usuario")))
                .andExpect(status().isNoContent());
    }

    @Test
    void deberiaFallarSiTopicoNoExisteAlEliminar() throws Exception {

        // Simulamos que el tópico con ID 999 no existe
        when(topicoRepository.getReferenceById(999L))
                .thenThrow(new ValidacionException("Tópico no encontrado"));

        mockMvc.perform(delete("/topicos/999")
                        .with(csrf())
                        .with(user("usuario")))
                .andExpect(status().isBadRequest()) // ⚡ 400 porque ValidacionException se maneja así
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof ValidacionException))
                .andExpect(result ->
                        assertEquals("Tópico no encontrado", result.getResolvedException().getMessage()));
    }






}
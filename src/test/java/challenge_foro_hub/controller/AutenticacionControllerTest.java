package challenge_foro_hub.controller;

import challenge_foro_hub.domain.usuario.Usuario;
import challenge_foro_hub.domain.usuario.UsuarioRepository;
import challenge_foro_hub.infra.security.TokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AutenticacionController.class)
@AutoConfigureMockMvc(addFilters = false)
class AutenticacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @Test
    void deberiaRetornarTokenJWT() throws Exception {

        String json = """
        {
            "email":"usuario@email.com",
            "password":"123456"
        }
        """;

        Usuario usuario = new Usuario(
                1L,
                "Juan",
                "usuario@email.com",
                "123456",
                true
        );

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());

        when(authenticationManager.authenticate(any()))
                .thenReturn(authentication);

        when(tokenService.generarToken(any()))
                .thenReturn("token-falso");

        mockMvc.perform(post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token-falso"));
    }


    @Test
    void deberiaRetornar401CuandoCredencialesSonInvalidas() throws Exception {

        String json = """
    {
        "email":"usuario@email.com",
        "password":"incorrecta"
    }
    """;

        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Credenciales inválidas"));

        mockMvc.perform(post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deberiaRetornar400CuandoDatosSonInvalidos() throws Exception {

        String json = """
    {
        "email":"",
        "password":""
    }
    """;

        mockMvc.perform(post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }



}
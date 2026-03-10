package challenge_foro_hub.infra.security;

import challenge_foro_hub.domain.usuario.Usuario;
import challenge_foro_hub.domain.usuario.UsuarioRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String generarToken(Usuario usuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.create()
                    .withIssuer("challenge-foro-hub")
                    .withSubject(usuario.getUsername())
                    .withExpiresAt(expirationdate())
                    .sign(algorithm);

        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error al generar token", exception);
        }
    }

    public String getSubject(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            DecodedJWT verifier = JWT.require(algorithm)
                    .withIssuer("challenge-foro-hub")
                    .build()
                    .verify(token);

            return verifier.getSubject();

        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Token inválido o expirado");
        }
    }

    private Instant expirationdate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
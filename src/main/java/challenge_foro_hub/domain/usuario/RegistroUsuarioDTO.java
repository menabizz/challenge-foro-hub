package challenge_foro_hub.domain.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RegistroUsuarioDTO(
        Long id,

        @NotBlank
        String nombre,

        @NotBlank
        @Email
        String email,

        @NotBlank(message = "Debe tener entre 6 y 10 caracteres.") @Pattern(regexp = "\\d{6,10}")
        String password

) {
    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

}

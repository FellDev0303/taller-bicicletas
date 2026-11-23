package co.edu.uao.tallerbicicletas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AutorizarTrabajoRequestDTO {
    @NotBlank(message = "El usuario que decide es obligatorio")
    private String usuarioId;

    @NotBlank(message = "La decisión es obligatoria")
    @Pattern(regexp = "autorizado|rechazado", message = "La decisión debe ser autorizado o rechazado")
    private String decision;

    private String comentarios;
}
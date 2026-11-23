package co.edu.uao.tallerbicicletas.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CancelarCitaRequestDTO {
    @NotBlank(message = "El usuario que cancela es obligatorio")
    private String usuarioId;

    @NotBlank(message = "El motivo de la cancelación es obligatorio")
    private String observacion;
}
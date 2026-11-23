package co.edu.uao.tallerbicicletas.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReprogramarCitaRequestDTO {
    @NotBlank(message = "El coordinador es obligatorio")
    private String coordinadorId;

    @NotNull(message = "La nueva fecha y hora son obligatorias")
    @Future(message = "La nueva fecha y hora deben ser futuras")
    private LocalDateTime nuevaFechaHora;

    @NotBlank(message = "La observación del cambio es obligatoria")
    private String observacion;
}
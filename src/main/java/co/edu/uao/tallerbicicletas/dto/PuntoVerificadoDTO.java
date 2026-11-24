package co.edu.uao.tallerbicicletas.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PuntoVerificadoDTO {
    @NotBlank(message = "El punto verificado es obligatorio")
    private String punto;

    @NotBlank(message = "El resultado de la verificación es obligatorio")
    private String resultado;
}
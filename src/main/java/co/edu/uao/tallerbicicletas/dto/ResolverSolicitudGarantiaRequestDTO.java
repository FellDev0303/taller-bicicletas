package co.edu.uao.tallerbicicletas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResolverSolicitudGarantiaRequestDTO {
    @NotBlank(message = "El estado es obligatorio")
    @Pattern(regexp = "registrada|en_revision|resuelta", message = "El estado debe ser registrada, en_revision o resuelta")
    private String estado;

    private Boolean procede;
    private String solucionAplicada;
}
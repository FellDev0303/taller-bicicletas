package co.edu.uao.tallerbicicletas.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RevisionEntregaRequestDTO {
    @NotBlank(message = "El usuarioId es obligatorio")
    private String usuarioId;

    @Valid
    @NotEmpty(message = "Debe registrar al menos un punto verificado")
    private List<PuntoVerificadoDTO> puntosVerificados;

    @NotNull(message = "Debe indicar si la revisión fue aprobada o no")
    private Boolean aprobada;

    private String recomendaciones;
}
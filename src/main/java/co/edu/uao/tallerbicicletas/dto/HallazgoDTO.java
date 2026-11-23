package co.edu.uao.tallerbicicletas.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HallazgoDTO {
    @NotBlank(message = "El técnico es obligatorio")
    private String tecnicoId;

    @NotBlank(message = "El componente es obligatorio")
    private String componente;

    @NotBlank(message = "La condición es obligatoria")
    private String condicion;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    @Pattern(regexp = "alta|media|baja", message = "La prioridad debe ser alta, media o baja")
    private String prioridad;

    private String observaciones;

    private String accionesRecomendadas;

    @Valid
    private List<AdjuntoDTO> evidencias;
}
package co.edu.uao.tallerbicicletas.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServicioRequestDTO {
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "La categoría es obligatoria")
    @Pattern(regexp = "mantenimiento_preventivo|frenos|transmision|ruedas|suspension|componentes_electricos",
             message = "La categoría debe ser mantenimiento_preventivo, frenos, transmision, ruedas, suspension o componentes_electricos")
    private String categoria;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    @NotNull(message = "La duración estimada es obligatoria")
    @Positive(message = "La duración estimada debe ser mayor que cero")
    private Double duracionEstimada;

    @NotNull(message = "El precio de referencia es obligatorio")
    @PositiveOrZero(message = "El precio de referencia no puede ser negativo")
    private Integer precioReferencia;

    // Opcional; si viene, se valida cada tarea
    @Valid
    private List<TareaHabitualDTO> tareasHabituales;

    // Opcional
    private String recomendacionesGenerales;
}
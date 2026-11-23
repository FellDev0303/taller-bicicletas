package co.edu.uao.tallerbicicletas.dto;

import java.time.LocalDateTime;
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
public class EjecutarTrabajoRequestDTO {
    @NotBlank(message = "El técnico que ejecuta es obligatorio")
    private String tecnicoId;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDateTime fechaInicio;

    private LocalDateTime fechaFinalizacion;

    @NotBlank(message = "El estado de ejecución es obligatorio")
    @Pattern(regexp = "en_curso|finalizado", message = "El estado de ejecución debe ser en_curso o finalizado")
    private String estado;

    @NotBlank(message = "La descripción de lo realizado es obligatoria")
    private String descripcionRealizada;

    @Valid
    private List<RepuestoUtilizadoDTO> repuestosUtilizados;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RepuestoUtilizadoDTO {
        @NotBlank(message = "El id del repuesto es obligatorio")
        private String repuestoId;
        private String nombre;

        @NotNull(message = "La cantidad utilizada es obligatoria")
        @Positive(message = "La cantidad utilizada debe ser mayor que cero")
        private Integer cantidad;

        @PositiveOrZero(message = "El precio unitario aplicado no puede ser negativo")
        private Integer precioUnitarioAplicado;
    }
}
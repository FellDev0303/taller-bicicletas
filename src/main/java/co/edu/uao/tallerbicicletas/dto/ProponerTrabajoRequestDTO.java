package co.edu.uao.tallerbicicletas.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProponerTrabajoRequestDTO {
    @NotBlank(message = "La orden es obligatoria")
    private String ordenId;

    @NotBlank(message = "El técnico es obligatorio")
    private String tecnicoId;

    // Opcional si se describe la intervención directamente, pero si viene se valida
    @Valid
    private ServicioTrabajoDTO servicio;

    private String descripcionIntervencion;

    @NotBlank(message = "El componente es obligatorio")
    private String componente;

    @NotNull(message = "La mano de obra estimada es obligatoria")
    @PositiveOrZero(message = "La mano de obra estimada no puede ser negativa")
    private Integer manoObraEstimada;

    @Valid
    private List<RepuestoPrevistoDTO> repuestosPrevistos;

    private String observaciones;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ServicioTrabajoDTO {
        @NotBlank(message = "El id del servicio es obligatorio")
        private String servicioId;
        private String nombre;
        private String descripcion;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RepuestoPrevistoDTO {
        @NotBlank(message = "El id del repuesto es obligatorio")
        private String repuestoId;
        private String nombre;
        private String especificaciones;

        @NotNull(message = "La cantidad del repuesto es obligatoria")
        @Positive(message = "La cantidad del repuesto debe ser mayor que cero")
        private Integer cantidad;

        @PositiveOrZero(message = "El precio unitario estimado no puede ser negativo")
        private Integer precioUnitarioEstimado;
    }
}
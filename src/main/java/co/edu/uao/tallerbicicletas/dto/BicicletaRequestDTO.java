package co.edu.uao.tallerbicicletas.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BicicletaRequestDTO {

    @NotBlank(message = "El propietario es obligatorio")
    @Pattern(regexp = "^[0-9a-fA-F]{24}$", message = "El propietario debe ser un identificador válido")
    private String propietarioId;

    @NotBlank(message = "La marca es obligatoria")
    private String marca;

    @NotBlank(message = "El modelo es obligatorio")
    private String modelo;

    @NotBlank(message = "El tipo es obligatorio")
    @Pattern(regexp = "urbana|ruta|montana|electrica",
             message = "El tipo debe ser urbana, ruta, montana o electrica")
    private String tipo;

    @NotBlank(message = "El color es obligatorio")
    private String color;

    private String numeroSerie;

    @NotNull(message = "Las características son obligatorias")
    @Valid
    private CaracteristicasDTO caracteristicas;

    @Data @AllArgsConstructor @NoArgsConstructor
    public static class CaracteristicasDTO {
        @NotBlank(message = "El tamaño del marco es obligatorio")
        private String tamanoMarco;
        @NotBlank(message = "El material es obligatorio")
        private String material;
        @NotBlank(message = "El tamaño de rueda es obligatorio")
        private String tamanoRueda;
        @NotBlank(message = "El tipo de frenos es obligatorio")
        private String tipoFrenos;
        private String transmision;
        @Valid private SuspensionDTO suspension;
        @Valid private BateriaDTO bateria;
        @Valid private MotorDTO motor;
    }

    @Data @AllArgsConstructor @NoArgsConstructor
    public static class SuspensionDTO {
        @NotBlank(message = "La información de la suspensión es obligatoria")
        private String informacion;
        @NotNull(message = "El recorrido de la suspensión es obligatorio")
        private Integer recorrido;
    }

    @Data @AllArgsConstructor @NoArgsConstructor
    public static class BateriaDTO {
        @NotNull(message = "La capacidad de la batería es obligatoria")
        private Double capacidadWh;
        @NotNull(message = "El voltaje de la batería es obligatorio")
        private Double voltajeV;
    }

    @Data @AllArgsConstructor @NoArgsConstructor
    public static class MotorDTO {
        @NotBlank(message = "El tipo de motor es obligatorio")
        private String tipo;
        @NotNull(message = "La potencia del motor es obligatoria")
        private Integer potenciaW;
    }
}
package co.edu.uao.tallerbicicletas.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CrearSolicitudGarantiaRequestDTO {
    @NotBlank(message = "El id del trabajo es obligatorio")
    private String trabajoId;

    @NotBlank(message = "La descripción del problema es obligatoria")
    private String descripcion;

    @NotEmpty(message = "Debe adjuntar al menos una evidencia")
    @Valid
    private List<EvidenciaGarantiaDTO> evidencias;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EvidenciaGarantiaDTO {
        @NotBlank(message = "El nombre del archivo es obligatorio")
        private String nombreArchivo;

        @NotBlank(message = "La descripción de la evidencia es obligatoria")
        private String descripcion;

        @NotBlank(message = "La ubicación del archivo es obligatoria")
        private String ubicacion;
    }
}

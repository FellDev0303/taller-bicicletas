package co.edu.uao.tallerbicicletas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServicioSolicitadoEstadisticaDTO {
    private String servicioId;
    private String nombreServicio;
    private Long cantidadSolicitudes;
    private Long cantidadAutorizados;
}
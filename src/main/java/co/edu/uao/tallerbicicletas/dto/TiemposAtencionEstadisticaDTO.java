package co.edu.uao.tallerbicicletas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TiemposAtencionEstadisticaDTO {
    private Long totalOrdenesEntregadas;
    private Double promedioHorasAtencion;
    private Double promedioDiasAtencion;
    private Long minimoHorasAtencion;
    private Long maximoHorasAtencion;
}
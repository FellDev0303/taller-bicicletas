package co.edu.uao.tallerbicicletas.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValoracionesEstadisticaDTO {
    private Long totalValoraciones;
    private Double promedioCalificacion;
    private Integer calificacionMinima;
    private Integer calificacionMaxima;
    private List<DistribucionCalificacionDTO> distribucion;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DistribucionCalificacionDTO {
        private Integer calificacion;
        private Long cantidad;
    }
}
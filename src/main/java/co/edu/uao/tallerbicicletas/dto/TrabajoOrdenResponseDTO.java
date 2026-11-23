package co.edu.uao.tallerbicicletas.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrabajoOrdenResponseDTO {
    private String id;
    private String ordenId;
    private String tecnicoId;
    private ProponerTrabajoRequestDTO.ServicioTrabajoDTO servicio;
    private String descripcionIntervencion;
    private String componente;
    private Integer manoObraEstimada;
    private List<ProponerTrabajoRequestDTO.RepuestoPrevistoDTO> repuestosPrevistos;
    private Integer valorEstimadoPropuesta;
    private String observaciones;
    private AutorizacionDTO autorizacion;
    private EjecucionDTO ejecucion;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AutorizacionDTO {
        private String decision;
        private LocalDateTime fecha;
        private String comentarios;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EjecucionDTO {
        private LocalDateTime fechaInicio;
        private LocalDateTime fechaFinalizacion;
        private String estado;
        private String descripcionRealizada;
        private List<EjecutarTrabajoRequestDTO.RepuestoUtilizadoDTO> repuestosUtilizados;
        private Integer valorTotalRepuestosAplicados;
    }
}
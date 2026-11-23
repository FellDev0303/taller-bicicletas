package co.edu.uao.tallerbicicletas.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdenResponseDTO {
    private String id;
    private String clienteId;
    private BicicletaOrdenDTO bicicleta;
    private String citaId;
    private LocalDateTime fechaRecepcion;
    private LocalDateTime fechaEstimadaEntrega;
    private String coordinadorId;
    private List<String> tecnicosAsignados;
    private String estado;
    private RecepcionDTO recepcion;
    private List<HallazgoDTO> hallazgos;
    private List<HistorialEstadoDTO> historialEstados;
}

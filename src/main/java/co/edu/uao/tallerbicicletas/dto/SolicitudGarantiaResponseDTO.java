package co.edu.uao.tallerbicicletas.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SolicitudGarantiaResponseDTO {
    private String id;
    private String trabajoId;
    private String descripcion;
    private LocalDateTime fecha;
    private List<CrearSolicitudGarantiaRequestDTO.EvidenciaGarantiaDTO> evidencias;
    private String estado;
    private Boolean procede;
    private String solucionAplicada;
}
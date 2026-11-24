package co.edu.uao.tallerbicicletas.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RevisionEntregaResponseDTO {
    private LocalDateTime fecha;
    private List<PuntoVerificadoDTO> puntosVerificados;
    private Boolean aprobada;
    private String recomendaciones;
}
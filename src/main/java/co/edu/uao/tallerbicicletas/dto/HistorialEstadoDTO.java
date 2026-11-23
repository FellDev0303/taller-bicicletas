package co.edu.uao.tallerbicicletas.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistorialEstadoDTO {
    private String estado;
    private LocalDateTime fecha;
    private String usuarioId;
    private String observacion;
}

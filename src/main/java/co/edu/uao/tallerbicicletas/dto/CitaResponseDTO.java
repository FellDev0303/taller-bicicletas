package co.edu.uao.tallerbicicletas.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CitaResponseDTO {
    private String id;
    private String clienteId;
    private String bicicletaId;
    private LocalDateTime fechaHora;
    private String motivo;
    private String estado;
    private String problemasObservados;
    private List<String> serviciosInteres;
    private String observacionCambio;
}

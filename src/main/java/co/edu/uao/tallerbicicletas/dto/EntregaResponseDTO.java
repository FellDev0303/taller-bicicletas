package co.edu.uao.tallerbicicletas.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntregaResponseDTO {
    private LocalDateTime fecha;
    private Boolean confirmacionRecepcionCliente;
}
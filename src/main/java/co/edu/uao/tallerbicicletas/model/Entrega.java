package co.edu.uao.tallerbicicletas.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Se llena en el flujo de revision y entrega (issue #7)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Entrega {
    private LocalDateTime fecha;
    private Boolean confirmacionRecepcionCliente;
}
package co.edu.uao.tallerbicicletas.model;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Se llena en el flujo de revision y entrega (issue #7)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RevisionEntrega {
    private LocalDateTime fecha;
    private List<PuntoVerificado> puntosVerificados;
    private Boolean aprobada;
    private String recomendaciones;
}

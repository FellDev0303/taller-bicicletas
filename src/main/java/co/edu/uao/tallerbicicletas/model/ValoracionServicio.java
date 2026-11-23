package co.edu.uao.tallerbicicletas.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Se llena en el flujo de valoracion (issue #8)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValoracionServicio {
    private Integer calificacion;
    private String comentarios;
    private String sugerencias;
    private LocalDateTime fecha;
}

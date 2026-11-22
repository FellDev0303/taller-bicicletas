package co.edu.uao.tallerbicicletas.dto;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorRespuestaDTO {
    private String fecha;
    private int status;
    private String error;
    private String mensaje;
    private List<String> detalles;
}
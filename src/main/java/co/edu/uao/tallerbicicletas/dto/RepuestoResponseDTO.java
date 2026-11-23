package co.edu.uao.tallerbicicletas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RepuestoResponseDTO {
    private String id;
    private String nombre;
    private String especificaciones;
    private Integer precio;
}

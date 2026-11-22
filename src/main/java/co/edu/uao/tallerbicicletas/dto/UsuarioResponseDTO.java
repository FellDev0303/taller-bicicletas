package co.edu.uao.tallerbicicletas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioResponseDTO {
    private String id;
    private String nombreCompleto;
    private String documento;
    private String correo;
    private String telefono;
    private String rol;
    private String direccion;
}
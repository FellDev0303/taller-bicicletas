package co.edu.uao.tallerbicicletas.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Archivo adjunto (fotografia o evidencia): se guarda su nombre, ubicacion y descripcion
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Adjunto {
    private String nombreArchivo;
    private String ubicacion;
    private String descripcion;
}

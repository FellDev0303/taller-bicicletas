package co.edu.uao.tallerbicicletas.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Documento embebido dentro de ServiciosModel (no lleva @Document)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TareaHabitual {
    private String nombre;
    private String descripcion;
}
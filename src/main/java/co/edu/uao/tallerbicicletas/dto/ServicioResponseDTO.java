package co.edu.uao.tallerbicicletas.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServicioResponseDTO {
    private String id;
    private String nombre;
    private String categoria;
    private String descripcion;
    private Double duracionEstimada;
    private Integer precioReferencia;
    private List<TareaHabitualDTO> tareasHabituales;
    private String recomendacionesGenerales;
}

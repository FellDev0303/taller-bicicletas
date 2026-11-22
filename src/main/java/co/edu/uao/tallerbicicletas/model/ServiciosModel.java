package co.edu.uao.tallerbicicletas.model;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("servicios")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiciosModel {
    @Id
    private ObjectId id;
    private String nombre;
    private String categoria;
    private String descripcion;
    private Double duracionEstimada;
    private Integer precioReferencia;
    private List<TareaHabitual> tareasHabituales;
    private String recomendacionesGenerales;
}
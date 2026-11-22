package co.edu.uao.tallerbicicletas.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("bicicletas")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BicicletasModel {
    @Id
    private ObjectId id;
    private ObjectId propietarioId;
    private String marca;
    private String modelo;
    private String tipo;
    private String color;
    private String numeroSerie;
    private Caracteristicas caracteristicas;

    @Data @AllArgsConstructor @NoArgsConstructor
    public static class Caracteristicas {
        private String tamanoMarco;
        private String material;
        private String tamanoRueda;
        private String tipoFrenos;
        private String transmision;
        private Suspension suspension;
        private Bateria bateria;
        private Motor motor;
    }

    @Data @AllArgsConstructor @NoArgsConstructor
    public static class Suspension {
        private String informacion;
        private Integer recorrido;
    }

    @Data @AllArgsConstructor @NoArgsConstructor
    public static class Bateria {
        private Double capacidadWh;
        private Double voltajeV;
    }

    @Data @AllArgsConstructor @NoArgsConstructor
    public static class Motor {
        private String tipo;
        private Integer potenciaW;
    }
}
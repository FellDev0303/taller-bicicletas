package co.edu.uao.tallerbicicletas.model;

import java.time.LocalDateTime;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("trabajosOrden")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrabajosOrdenModel {
    @Id
    private ObjectId id;
    private ObjectId ordenId;
    private ObjectId tecnicoId;
    private ServicioTrabajo servicio;
    private String descripcionIntervencion;
    private String componente;
    private Integer manoObraEstimada;
    private List<RepuestoPrevisto> repuestosPrevistos;
    private String observaciones;
    private Autorizacion autorizacion;
    private Ejecucion ejecucion;
    private Garantia garantia;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ServicioTrabajo {
        private ObjectId servicioId;
        private String nombre;
        private String descripcion;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RepuestoPrevisto {
        private ObjectId repuestoId;
        private String nombre;
        private String especificaciones;
        private Integer cantidad;
        private Integer precioUnitarioEstimado;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Autorizacion {
        private String decision; // "autorizado" | "rechazado"
        private LocalDateTime fecha;
        private String comentarios;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Ejecucion {
        private LocalDateTime fechaInicio;
        private LocalDateTime fechaFinalizacion;
        private String estado; // "en_curso" | "finalizado"
        private String descripcionRealizada;
        private List<RepuestoUtilizado> repuestosUtilizados;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RepuestoUtilizado {
        private ObjectId repuestoId;
        private String nombre;
        private Integer cantidad;
        private Integer precioUnitarioAplicado;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Garantia {
        private Integer duracion;
        private LocalDateTime fechaInicio;
        private String condiciones;
        private String exclusiones;
    }
}
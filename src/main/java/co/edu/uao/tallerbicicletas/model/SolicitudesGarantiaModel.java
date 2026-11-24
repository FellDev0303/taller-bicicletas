package co.edu.uao.tallerbicicletas.model;

import java.time.LocalDateTime;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("solicitudesGarantia")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SolicitudesGarantiaModel {
    @Id
    private ObjectId id;
    private ObjectId trabajoId;
    private String descripcion;
    private LocalDateTime fecha;
    private List<EvidenciaGarantia> evidencias;
    private String estado; // "registrada" | "en_revision" | "resuelta"
    private Boolean procede;
    private String solucionAplicada;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EvidenciaGarantia {
        private String nombreArchivo;
        private String descripcion;
        private String ubicacion;
    }
}
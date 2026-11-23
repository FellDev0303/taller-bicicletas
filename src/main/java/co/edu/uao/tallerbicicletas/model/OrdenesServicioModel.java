package co.edu.uao.tallerbicicletas.model;

import java.time.LocalDateTime;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("ordenesServicio")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdenesServicioModel {
    @Id
    private ObjectId id;
    private ObjectId clienteId;
    private BicicletaOrden bicicleta;
    private ObjectId citaId;
    private LocalDateTime fechaRecepcion;
    private LocalDateTime fechaEstimadaEntrega;
    private ObjectId coordinadorId;
    private List<ObjectId> tecnicosAsignados;
    private String estado;
    private Recepcion recepcion;
    private Diagnostico diagnostico;
    private Entrega entrega;
    private ValoracionServicio valoracionServicio;
    private List<HistorialEstado> historialEstados;
    private RevisionEntrega revisionEntrega;
}

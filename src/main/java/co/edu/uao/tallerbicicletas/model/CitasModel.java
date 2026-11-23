package co.edu.uao.tallerbicicletas.model;

import java.time.LocalDateTime;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("citas")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CitasModel {
    @Id
    private ObjectId id;
    private ObjectId clienteId;
    private ObjectId bicicletaId;
    private LocalDateTime fechaHora;
    private String motivo;
    private String estado;
    private String problemasObservados;
    private List<ObjectId> serviciosInteres;
    private String observacionCambio;
}
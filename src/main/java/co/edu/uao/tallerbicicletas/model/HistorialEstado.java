package co.edu.uao.tallerbicicletas.model;

import java.time.LocalDateTime;

import org.bson.types.ObjectId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistorialEstado {
    private String estado;
    private LocalDateTime fecha;
    private ObjectId usuarioId;
    private String observacion;
}

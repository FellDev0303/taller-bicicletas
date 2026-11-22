package co.edu.uao.tallerbicicletas.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("usuarios")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuariosModel {
    @Id
    private ObjectId id;
    private String nombreCompleto;
    private String documento;
    private String correo;
    private String telefono;
    private String rol;
    private String direccion;
}

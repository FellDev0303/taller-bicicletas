package co.edu.uao.tallerbicicletas.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import co.edu.uao.tallerbicicletas.model.UsuariosModel;

public interface IUsuariosRepository extends MongoRepository<UsuariosModel, ObjectId> {
    
    boolean existsByDocumento(String documento);
    boolean existsByCorreo(String correo);
}

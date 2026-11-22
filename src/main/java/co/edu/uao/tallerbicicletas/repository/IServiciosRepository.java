package co.edu.uao.tallerbicicletas.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import co.edu.uao.tallerbicicletas.model.ServiciosModel;

public interface IServiciosRepository extends MongoRepository<ServiciosModel, ObjectId> {
    //consultas nativas
    List<ServiciosModel> findByCategoria(String categoria);
}
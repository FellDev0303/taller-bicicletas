package co.edu.uao.tallerbicicletas.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import co.edu.uao.tallerbicicletas.model.RepuestosModel;

public interface IRepuestosRepository extends MongoRepository<RepuestosModel, ObjectId> {
    //consultas nativas
}
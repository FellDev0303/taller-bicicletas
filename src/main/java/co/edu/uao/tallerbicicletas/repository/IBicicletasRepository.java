package co.edu.uao.tallerbicicletas.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import co.edu.uao.tallerbicicletas.model.BicicletasModel;

public interface IBicicletasRepository extends MongoRepository<BicicletasModel, ObjectId> {

    List<BicicletasModel> findByPropietarioId(ObjectId propietarioId);
}
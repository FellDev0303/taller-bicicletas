package co.edu.uao.tallerbicicletas.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import co.edu.uao.tallerbicicletas.model.TrabajosOrdenModel;

public interface ITrabajosOrdenRepository extends MongoRepository<TrabajosOrdenModel, ObjectId> {
    List<TrabajosOrdenModel> findByOrdenId(ObjectId ordenId);
    List<TrabajosOrdenModel> findByOrdenIdAndAutorizacion_Decision(ObjectId ordenId, String decision);
}
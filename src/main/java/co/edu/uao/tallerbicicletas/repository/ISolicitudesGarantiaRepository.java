package co.edu.uao.tallerbicicletas.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import co.edu.uao.tallerbicicletas.model.SolicitudesGarantiaModel;

public interface ISolicitudesGarantiaRepository extends MongoRepository<SolicitudesGarantiaModel, ObjectId> {
    List<SolicitudesGarantiaModel> findByTrabajoId(ObjectId trabajoId);
    List<SolicitudesGarantiaModel> findByEstado(String estado);
}

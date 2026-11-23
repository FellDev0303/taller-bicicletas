package co.edu.uao.tallerbicicletas.repository;

import java.util.Collection;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import co.edu.uao.tallerbicicletas.model.OrdenesServicioModel;

public interface IOrdenesServicioRepository extends MongoRepository<OrdenesServicioModel, ObjectId> {
    //consultas nativas
    boolean existsByBicicleta_BicicletaIdAndEstadoIn(ObjectId bicicletaId, Collection<String> estados);
    List<OrdenesServicioModel> findByBicicleta_BicicletaId(ObjectId bicicletaId);
    List<OrdenesServicioModel> findByClienteId(ObjectId clienteId);
    List<OrdenesServicioModel> findByEstado(String estado);
}
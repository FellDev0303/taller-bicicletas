package co.edu.uao.tallerbicicletas.repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import co.edu.uao.tallerbicicletas.model.CitasModel;

public interface ICitasRepository extends MongoRepository<CitasModel, ObjectId> {
    //consultas nativas
    boolean existsByBicicletaIdAndFechaHoraAndEstadoIn(ObjectId bicicletaId, LocalDateTime fechaHora, Collection<String> estados);
    List<CitasModel> findByClienteId(ObjectId clienteId);
    List<CitasModel> findByBicicletaId(ObjectId bicicletaId);
    List<CitasModel> findByEstado(String estado);
}
package co.edu.uao.tallerbicicletas.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uao.tallerbicicletas.dto.RepuestoRequestDTO;
import co.edu.uao.tallerbicicletas.dto.RepuestoResponseDTO;
import co.edu.uao.tallerbicicletas.exception.RecursoNoEncontradoException;
import co.edu.uao.tallerbicicletas.exception.SolicitudInvalidaException;
import co.edu.uao.tallerbicicletas.model.RepuestosModel;
import co.edu.uao.tallerbicicletas.repository.IRepuestosRepository;

@Service
public class RepuestosServiceImp implements IRepuestoService {
    @Autowired IRepuestosRepository repuestosRepository;//inyección de dependencia

    @Override
    public RepuestoResponseDTO crearRepuesto(RepuestoRequestDTO dto) {
        // DTO -> Model (el id lo genera MongoDB)
        RepuestosModel repuesto = new RepuestosModel(null, dto.getNombre(), dto.getEspecificaciones(), dto.getPrecio());
        return convertirADTO(repuestosRepository.save(repuesto));
    }

    @Override
    public List<RepuestoResponseDTO> listarRepuestos() {
        return repuestosRepository.findAll().stream().map(this::convertirADTO).toList();
    }

    @Override
    public RepuestoResponseDTO buscarRepuestoPorId(String idRepuesto) {
        return convertirADTO(buscarModelo(idRepuesto));
    }

    @Override
    public RepuestoResponseDTO actualizarRepuesto(String idRepuesto, RepuestoRequestDTO dto) {
        RepuestosModel repuesto = buscarModelo(idRepuesto);
        repuesto.setNombre(dto.getNombre());
        repuesto.setEspecificaciones(dto.getEspecificaciones());
        repuesto.setPrecio(dto.getPrecio());
        return convertirADTO(repuestosRepository.save(repuesto));
    }

    // Valida el formato del id y busca el documento (404 si no existe)
    private RepuestosModel buscarModelo(String idRepuesto) {
        if (!ObjectId.isValid(idRepuesto)) {
            throw new SolicitudInvalidaException("El id '" + idRepuesto + "' no tiene un formato válido");
        }
        return repuestosRepository.findById(new ObjectId(idRepuesto))
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un repuesto con el id " + idRepuesto));
    }

    // Model -> DTO (el ObjectId se devuelve como texto)
    private RepuestoResponseDTO convertirADTO(RepuestosModel repuesto) {
        return new RepuestoResponseDTO(repuesto.getId().toHexString(), repuesto.getNombre(),
                repuesto.getEspecificaciones(), repuesto.getPrecio());
    }
}
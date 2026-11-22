package co.edu.uao.tallerbicicletas.service;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uao.tallerbicicletas.dto.ServicioRequestDTO;
import co.edu.uao.tallerbicicletas.dto.ServicioResponseDTO;
import co.edu.uao.tallerbicicletas.dto.TareaHabitualDTO;
import co.edu.uao.tallerbicicletas.exception.RecursoNoEncontradoException;
import co.edu.uao.tallerbicicletas.exception.SolicitudInvalidaException;
import co.edu.uao.tallerbicicletas.model.ServiciosModel;
import co.edu.uao.tallerbicicletas.model.TareaHabitual;
import co.edu.uao.tallerbicicletas.repository.IServiciosRepository;

@Service
public class ServiciosServiceImp implements IServicioService {
    @Autowired IServiciosRepository serviciosRepository;//inyección de dependencia

    @Override
    public ServicioResponseDTO crearServicio(ServicioRequestDTO dto) {
        // DTO -> Model (el id lo genera MongoDB)
        ServiciosModel servicio = new ServiciosModel(null, dto.getNombre(), dto.getCategoria(),
                dto.getDescripcion(), dto.getDuracionEstimada(), dto.getPrecioReferencia(),
                convertirTareasAModelo(dto.getTareasHabituales()), dto.getRecomendacionesGenerales());
        return convertirADTO(serviciosRepository.save(servicio));
    }

    @Override
    public List<ServicioResponseDTO> listarServicios() {
        return serviciosRepository.findAll().stream().map(this::convertirADTO).toList();
    }

    @Override
    public List<ServicioResponseDTO> listarServiciosPorCategoria(String categoria) {
        return serviciosRepository.findByCategoria(categoria).stream().map(this::convertirADTO).toList();
    }

    @Override
    public ServicioResponseDTO buscarServicioPorId(String idServicio) {
        return convertirADTO(buscarModelo(idServicio));
    }

    @Override
    public ServicioResponseDTO actualizarServicio(String idServicio, ServicioRequestDTO dto) {
        ServiciosModel servicio = buscarModelo(idServicio);
        servicio.setNombre(dto.getNombre());
        servicio.setCategoria(dto.getCategoria());
        servicio.setDescripcion(dto.getDescripcion());
        servicio.setDuracionEstimada(dto.getDuracionEstimada());
        servicio.setPrecioReferencia(dto.getPrecioReferencia());
        servicio.setTareasHabituales(convertirTareasAModelo(dto.getTareasHabituales()));
        servicio.setRecomendacionesGenerales(dto.getRecomendacionesGenerales());
        return convertirADTO(serviciosRepository.save(servicio));
    }

    // Valida el formato del id y busca el documento (404 si no existe)
    private ServiciosModel buscarModelo(String idServicio) {
        if (!ObjectId.isValid(idServicio)) {
            throw new SolicitudInvalidaException("El id '" + idServicio + "' no tiene un formato válido");
        }
        return serviciosRepository.findById(new ObjectId(idServicio))
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un servicio con el id " + idServicio));
    }

    // Lista de DTO -> lista de documentos embebidos
    private List<TareaHabitual> convertirTareasAModelo(List<TareaHabitualDTO> tareasDTO) {
        List<TareaHabitual> tareas = new ArrayList<>();
        if (tareasDTO != null) {
            for (TareaHabitualDTO t : tareasDTO) {
                tareas.add(new TareaHabitual(t.getNombre(), t.getDescripcion()));
            }
        }
        return tareas;
    }

    // Model -> DTO (el ObjectId se devuelve como texto)
    private ServicioResponseDTO convertirADTO(ServiciosModel servicio) {
        List<TareaHabitualDTO> tareasDTO = new ArrayList<>();
        if (servicio.getTareasHabituales() != null) {
            for (TareaHabitual t : servicio.getTareasHabituales()) {
                tareasDTO.add(new TareaHabitualDTO(t.getNombre(), t.getDescripcion()));
            }
        }
        return new ServicioResponseDTO(servicio.getId().toHexString(), servicio.getNombre(),
                servicio.getCategoria(), servicio.getDescripcion(), servicio.getDuracionEstimada(),
                servicio.getPrecioReferencia(), tareasDTO, servicio.getRecomendacionesGenerales());
    }
}
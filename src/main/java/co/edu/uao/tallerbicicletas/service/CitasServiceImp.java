package co.edu.uao.tallerbicicletas.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uao.tallerbicicletas.dto.CancelarCitaRequestDTO;
import co.edu.uao.tallerbicicletas.dto.CitaRequestDTO;
import co.edu.uao.tallerbicicletas.dto.CitaResponseDTO;
import co.edu.uao.tallerbicicletas.dto.ConfirmarCitaRequestDTO;
import co.edu.uao.tallerbicicletas.dto.ReprogramarCitaRequestDTO;
import co.edu.uao.tallerbicicletas.exception.ConflictoException;
import co.edu.uao.tallerbicicletas.exception.RecursoNoEncontradoException;
import co.edu.uao.tallerbicicletas.exception.SolicitudInvalidaException;
import co.edu.uao.tallerbicicletas.model.BicicletasModel;
import co.edu.uao.tallerbicicletas.model.CitasModel;
import co.edu.uao.tallerbicicletas.model.UsuariosModel;
import co.edu.uao.tallerbicicletas.repository.IBicicletasRepository;
import co.edu.uao.tallerbicicletas.repository.ICitasRepository;
import co.edu.uao.tallerbicicletas.repository.IServiciosRepository;
import co.edu.uao.tallerbicicletas.repository.IUsuariosRepository;

@Service
public class CitasServiceImp implements ICitaService {
    @Autowired ICitasRepository citasRepository;//inyección de dependencia
    @Autowired IUsuariosRepository usuariosRepository;
    @Autowired IBicicletasRepository bicicletasRepository;
    @Autowired IServiciosRepository serviciosRepository;

    // Estados en los que una cita se considera activa
    private static final List<String> ESTADOS_ACTIVOS = List.of("solicitada", "confirmada");

    @Override
    public CitaResponseDTO solicitarCita(CitaRequestDTO dto) {
        // 1. El cliente debe existir y tener rol cliente
        UsuariosModel cliente = buscarUsuarioConRol(dto.getClienteId(), "cliente", "el cliente");

        // 2. La bicicleta debe existir y pertenecer al cliente
        ObjectId idBicicleta = convertirAObjectId(dto.getBicicletaId(), "bicicletaId");
        BicicletasModel bicicleta = bicicletasRepository.findById(idBicicleta)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe una bicicleta con el id " + dto.getBicicletaId()));
        if (!cliente.getId().toHexString().equals(String.valueOf(bicicleta.getPropietarioId()))) {
            throw new SolicitudInvalidaException("La bicicleta no pertenece al cliente indicado");
        }

        // 3. Los servicios de interes (opcionales) deben existir en el catalogo
        List<ObjectId> servicios = new ArrayList<>();
        if (dto.getServiciosInteres() != null) {
            for (String idServicio : dto.getServiciosInteres()) {
                ObjectId id = convertirAObjectId(idServicio, "serviciosInteres");
                if (!serviciosRepository.existsById(id)) {
                    throw new RecursoNoEncontradoException("No existe un servicio con el id " + idServicio);
                }
                servicios.add(id);
            }
        }

        // 4. Regla del PDF: no puede haber citas activas duplicadas para la misma bicicleta en el mismo horario
        LocalDateTime fechaHora = dto.getFechaHora().truncatedTo(ChronoUnit.MINUTES);
        validarHorarioLibre(idBicicleta, fechaHora);

        // 5. DTO -> Model (la cita nace en estado solicitada)
        CitasModel cita = new CitasModel();
        cita.setClienteId(cliente.getId());
        cita.setBicicletaId(idBicicleta);
        cita.setFechaHora(fechaHora);
        cita.setMotivo(dto.getMotivo());
        cita.setEstado("solicitada");
        cita.setProblemasObservados(dto.getProblemasObservados());
        cita.setServiciosInteres(servicios);
        return convertirADTO(citasRepository.save(cita));
    }

    @Override
    public List<CitaResponseDTO> listarCitas() {
        return citasRepository.findAll().stream().map(this::convertirADTO).toList();
    }

    @Override
    public List<CitaResponseDTO> listarCitasPorCliente(String idCliente) {
        ObjectId id = convertirAObjectId(idCliente, "clienteId");
        return citasRepository.findByClienteId(id).stream().map(this::convertirADTO).toList();
    }

    @Override
    public List<CitaResponseDTO> listarCitasPorBicicleta(String idBicicleta) {
        ObjectId id = convertirAObjectId(idBicicleta, "bicicletaId");
        return citasRepository.findByBicicletaId(id).stream().map(this::convertirADTO).toList();
    }

    @Override
    public List<CitaResponseDTO> listarCitasPorEstado(String estado) {
        return citasRepository.findByEstado(estado).stream().map(this::convertirADTO).toList();
    }

    @Override
    public CitaResponseDTO buscarCitaPorId(String idCita) {
        return convertirADTO(buscarModelo(idCita));
    }

    @Override
    public CitaResponseDTO confirmarCita(String idCita, ConfirmarCitaRequestDTO confirmacion) {
        CitasModel cita = buscarModelo(idCita);
        buscarUsuarioConRol(confirmacion.getCoordinadorId(), "coordinador", "el coordinador");

        if (!"solicitada".equals(cita.getEstado())) {
            throw new ConflictoException("Solo se puede confirmar una cita en estado 'solicitada'");
        }
        cita.setEstado("confirmada");
        return convertirADTO(citasRepository.save(cita));
    }

    @Override
    public CitaResponseDTO reprogramarCita(String idCita, ReprogramarCitaRequestDTO cambio) {
        CitasModel cita = buscarModelo(idCita);
        buscarUsuarioConRol(cambio.getCoordinadorId(), "coordinador", "el coordinador");

        if (!ESTADOS_ACTIVOS.contains(cita.getEstado())) {
            throw new ConflictoException("Solo se puede reprogramar una cita solicitada o confirmada");
        }

        LocalDateTime nuevaFechaHora = cambio.getNuevaFechaHora().truncatedTo(ChronoUnit.MINUTES);
        if (nuevaFechaHora.equals(cita.getFechaHora())) {
            throw new SolicitudInvalidaException("La nueva fecha y hora es igual a la actual");
        }
        validarHorarioLibre(cita.getBicicletaId(), nuevaFechaHora);

        // El estado no cambia: solo se modifica el horario y se deja la observacion
        cita.setFechaHora(nuevaFechaHora);
        cita.setObservacionCambio(cambio.getObservacion());
        return convertirADTO(citasRepository.save(cita));
    }

    @Override
    public CitaResponseDTO cancelarCita(String idCita, CancelarCitaRequestDTO cancelacion) {
        CitasModel cita = buscarModelo(idCita);

        ObjectId idUsuario = convertirAObjectId(cancelacion.getUsuarioId(), "usuarioId");
        UsuariosModel usuario = usuariosRepository.findById(idUsuario)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un usuario con el id " + cancelacion.getUsuarioId()));

        // Solo el cliente de la cita o un coordinador pueden cancelarla
        boolean esCoordinador = "coordinador".equals(usuario.getRol());
        boolean esElCliente = usuario.getId().equals(cita.getClienteId());
        if (!esCoordinador && !esElCliente) {
            throw new ConflictoException("Solo el cliente de la cita o un coordinador pueden cancelarla");
        }
        if (!ESTADOS_ACTIVOS.contains(cita.getEstado())) {
            throw new ConflictoException("Solo se puede cancelar una cita solicitada o confirmada");
        }

        cita.setEstado("cancelada");
        cita.setObservacionCambio(cancelacion.getObservacion());
        return convertirADTO(citasRepository.save(cita));
    }

    

    // Regla de negocio: la bicicleta no puede tener otra cita activa en el mismo horario (409)
    private void validarHorarioLibre(ObjectId idBicicleta, LocalDateTime fechaHora) {
        if (citasRepository.existsByBicicletaIdAndFechaHoraAndEstadoIn(idBicicleta, fechaHora, ESTADOS_ACTIVOS)) {
            throw new ConflictoException("La bicicleta ya tiene una cita activa en ese horario");
        }
    }

    // Valida el formato del texto y lo convierte en ObjectId
    private ObjectId convertirAObjectId(String valor, String campo) {
        if (valor == null || !ObjectId.isValid(valor)) {
            throw new SolicitudInvalidaException("El valor de '" + campo + "' no tiene un formato de id válido");
        }
        return new ObjectId(valor);
    }

    // Busca la cita (400 si el id es invalido, 404 si no existe)
    private CitasModel buscarModelo(String idCita) {
        ObjectId id = convertirAObjectId(idCita, "id");
        return citasRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe una cita con el id " + idCita));
    }

    // Busca un usuario y verifica que tenga el rol esperado
    private UsuariosModel buscarUsuarioConRol(String idUsuario, String rol, String etiqueta) {
        ObjectId id = convertirAObjectId(idUsuario, "id de " + etiqueta);
        UsuariosModel usuario = usuariosRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe " + etiqueta + " con el id " + idUsuario));
        if (!rol.equals(usuario.getRol())) {
            throw new SolicitudInvalidaException("El usuario " + idUsuario + " no tiene el rol " + rol);
        }
        return usuario;
    }

    // Model -> DTO (los ObjectId se devuelven como texto)
    private CitaResponseDTO convertirADTO(CitasModel cita) {
        List<String> servicios = new ArrayList<>();
        if (cita.getServiciosInteres() != null) {
            for (ObjectId s : cita.getServiciosInteres()) {
                servicios.add(s.toHexString());
            }
        }
        return new CitaResponseDTO(cita.getId().toHexString(), cita.getClienteId().toHexString(),
                cita.getBicicletaId().toHexString(), cita.getFechaHora(), cita.getMotivo(), cita.getEstado(),
                cita.getProblemasObservados(), servicios, cita.getObservacionCambio());
    }
}

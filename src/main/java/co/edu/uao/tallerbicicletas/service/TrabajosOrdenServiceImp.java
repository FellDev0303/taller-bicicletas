package co.edu.uao.tallerbicicletas.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uao.tallerbicicletas.dto.AutorizarTrabajoRequestDTO;
import co.edu.uao.tallerbicicletas.dto.EjecutarTrabajoRequestDTO;
import co.edu.uao.tallerbicicletas.dto.ProponerTrabajoRequestDTO;
import co.edu.uao.tallerbicicletas.dto.TrabajoOrdenResponseDTO;
import co.edu.uao.tallerbicicletas.exception.ConflictoException;
import co.edu.uao.tallerbicicletas.exception.RecursoNoEncontradoException;
import co.edu.uao.tallerbicicletas.exception.SolicitudInvalidaException;
import co.edu.uao.tallerbicicletas.model.OrdenesServicioModel;
import co.edu.uao.tallerbicicletas.model.RepuestosModel;
import co.edu.uao.tallerbicicletas.model.ServiciosModel;
import co.edu.uao.tallerbicicletas.model.TrabajosOrdenModel;
import co.edu.uao.tallerbicicletas.model.UsuariosModel;
import co.edu.uao.tallerbicicletas.repository.IOrdenesServicioRepository;
import co.edu.uao.tallerbicicletas.repository.IRepuestosRepository;
import co.edu.uao.tallerbicicletas.repository.IServiciosRepository;
import co.edu.uao.tallerbicicletas.repository.ITrabajosOrdenRepository;
import co.edu.uao.tallerbicicletas.repository.IUsuariosRepository;

@Service
public class TrabajosOrdenServiceImp implements ITrabajoOrdenService {

    @Autowired
    private ITrabajosOrdenRepository trabajosOrdenRepository;

    @Autowired
    private IOrdenesServicioRepository ordenesRepository;

    @Autowired
    private IUsuariosRepository usuariosRepository;

    @Autowired
    private IServiciosRepository serviciosRepository;

    @Autowired
    private IRepuestosRepository repuestosRepository;

    @Override
    public TrabajoOrdenResponseDTO proponerTrabajo(ProponerTrabajoRequestDTO dto) {
        OrdenesServicioModel orden = buscarOrden(dto.getOrdenId());
        if (orden.getDiagnostico() == null
                || orden.getDiagnostico().getHallazgos() == null
                || orden.getDiagnostico().getHallazgos().isEmpty()) {
            throw new ConflictoException("No se pueden proponer trabajos porque la orden aún no tiene diagnóstico registrado");
        }
        if (!"pendiente_autorizacion".equals(orden.getEstado()) && !"en_ejecucion".equals(orden.getEstado())) {
            throw new ConflictoException("Solo se pueden proponer trabajos cuando la orden está en estado 'pendiente_autorizacion' o 'en_ejecucion'");
        }

        UsuariosModel tecnico = buscarUsuarioConRol(dto.getTecnicoId(), "tecnico", "el técnico");
        if (orden.getTecnicosAsignados() == null || !orden.getTecnicosAsignados().contains(tecnico.getId())) {
            throw new ConflictoException("El técnico no está asignado a esta orden");
        }

        boolean tieneServicio = dto.getServicio() != null;
        boolean tieneDescripcion = dto.getDescripcionIntervencion() != null && !dto.getDescripcionIntervencion().isBlank();
        if (!tieneServicio && !tieneDescripcion) {
            throw new SolicitudInvalidaException("Debe indicar un servicio o una descripción de la intervención");
        }

        TrabajosOrdenModel.ServicioTrabajo servicioModelo = null;
        if (tieneServicio) {
            ObjectId servicioId = convertirAObjectId(dto.getServicio().getServicioId(), "servicio.servicioId");
            ServiciosModel servicioCatalogo = serviciosRepository.findById(servicioId)
                    .orElseThrow(() -> new RecursoNoEncontradoException("No existe un servicio con el id " + dto.getServicio().getServicioId()));

            String nombreServicio = (dto.getServicio().getNombre() != null && !dto.getServicio().getNombre().isBlank())
                    ? dto.getServicio().getNombre()
                    : servicioCatalogo.getNombre();
            String descServicio = (dto.getServicio().getDescripcion() != null && !dto.getServicio().getDescripcion().isBlank())
                    ? dto.getServicio().getDescripcion()
                    : servicioCatalogo.getDescripcion();

            servicioModelo = new TrabajosOrdenModel.ServicioTrabajo(servicioId, nombreServicio, descServicio);
        }

        List<TrabajosOrdenModel.RepuestoPrevisto> repuestosModelo = new ArrayList<>();
        if (dto.getRepuestosPrevistos() != null) {
            for (ProponerTrabajoRequestDTO.RepuestoPrevistoDTO rDto : dto.getRepuestosPrevistos()) {
                ObjectId repuestoId = convertirAObjectId(rDto.getRepuestoId(), "repuestosPrevistos.repuestoId");
                RepuestosModel repCatalogo = repuestosRepository.findById(repuestoId)
                        .orElseThrow(() -> new RecursoNoEncontradoException("No existe un repuesto con el id " + rDto.getRepuestoId()));

                String nombreRep = (rDto.getNombre() != null && !rDto.getNombre().isBlank())
                        ? rDto.getNombre()
                        : repCatalogo.getNombre();
                String espRep = (rDto.getEspecificaciones() != null && !rDto.getEspecificaciones().isBlank())
                        ? rDto.getEspecificaciones()
                        : repCatalogo.getEspecificaciones();
                Integer precioEstObj = rDto.getPrecioUnitarioEstimado();
                Integer precioEst = (precioEstObj != null)
                        ? precioEstObj
                        : repCatalogo.getPrecio();

                repuestosModelo.add(new TrabajosOrdenModel.RepuestoPrevisto(
                        repuestoId, nombreRep, espRep, rDto.getCantidad(), precioEst));
            }
        }

        TrabajosOrdenModel trabajo = new TrabajosOrdenModel();
        trabajo.setOrdenId(orden.getId());
        trabajo.setTecnicoId(tecnico.getId());
        trabajo.setServicio(servicioModelo);
        trabajo.setDescripcionIntervencion(dto.getDescripcionIntervencion());
        trabajo.setComponente(dto.getComponente());
        trabajo.setManoObraEstimada(dto.getManoObraEstimada());
        trabajo.setRepuestosPrevistos(repuestosModelo);
        trabajo.setObservaciones(dto.getObservaciones());
        trabajo.setAutorizacion(null);
        trabajo.setEjecucion(null);

        return convertirADTO(trabajosOrdenRepository.save(trabajo));
    }

    @Override
    public List<TrabajoOrdenResponseDTO> listarTrabajosPorOrden(String ordenId) {
        OrdenesServicioModel orden = buscarOrden(ordenId);
        return trabajosOrdenRepository.findByOrdenId(orden.getId()).stream()
                .map(this::convertirADTO)
                .toList();
    }

    @Override
    public TrabajoOrdenResponseDTO buscarTrabajoPorId(String id) {
        return convertirADTO(buscarTrabajo(id));
    }

    @Override
    public TrabajoOrdenResponseDTO decidirAutorizacion(String idTrabajo, AutorizarTrabajoRequestDTO dto) {
        TrabajosOrdenModel trabajo = buscarTrabajo(idTrabajo);
        OrdenesServicioModel orden = ordenesRepository.findById(trabajo.getOrdenId())
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la orden asociada al trabajo"));

        if (!"pendiente_autorizacion".equals(orden.getEstado()) && !"en_ejecucion".equals(orden.getEstado())) {
            throw new ConflictoException("Solo se pueden autorizar o rechazar trabajos cuando la orden está en estado 'pendiente_autorizacion' o 'en_ejecucion'");
        }

        ObjectId usuarioId = convertirAObjectId(dto.getUsuarioId(), "usuarioId");
        UsuariosModel usuario = usuariosRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un usuario con el id " + dto.getUsuarioId()));

        boolean esClienteDeLaOrden = "cliente".equals(usuario.getRol()) && usuario.getId().equals(orden.getClienteId());
        if (!esClienteDeLaOrden) {
            throw new ConflictoException("Solo el cliente propietario de la orden puede autorizar o rechazar trabajos");
        }

        if (trabajo.getAutorizacion() != null) {
            throw new ConflictoException("Este trabajo ya tiene una decisión de autorización registrada ("
                    + trabajo.getAutorizacion().getDecision() + ")");
        }

        TrabajosOrdenModel.Autorizacion autorizacion = new TrabajosOrdenModel.Autorizacion(
                dto.getDecision(),
                LocalDateTime.now(),
                dto.getComentarios());
        trabajo.setAutorizacion(autorizacion);

        return convertirADTO(trabajosOrdenRepository.save(trabajo));
    }

    @Override
    public TrabajoOrdenResponseDTO registrarEjecucion(String idTrabajo, EjecutarTrabajoRequestDTO dto) {
        TrabajosOrdenModel trabajo = buscarTrabajo(idTrabajo);
        OrdenesServicioModel orden = ordenesRepository.findById(trabajo.getOrdenId())
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la orden asociada al trabajo"));

        if (trabajo.getAutorizacion() == null || !"autorizado".equals(trabajo.getAutorizacion().getDecision())) {
            throw new ConflictoException("Solo los trabajos autorizados pueden ejecutarse");
        }

        if (!"en_ejecucion".equals(orden.getEstado())) {
            throw new ConflictoException("Solo se pueden ejecutar trabajos cuando la orden está en estado 'en_ejecucion'");
        }

        UsuariosModel tecnico = buscarUsuarioConRol(dto.getTecnicoId(), "tecnico", "el técnico");
        if (orden.getTecnicosAsignados() == null || !orden.getTecnicosAsignados().contains(tecnico.getId())) {
            throw new ConflictoException("El técnico no está asignado a esta orden");
        }

        if ("finalizado".equals(dto.getEstado()) && dto.getFechaFinalizacion() == null) {
            throw new SolicitudInvalidaException("La fecha de finalización es obligatoria cuando el trabajo está finalizado");
        }
        if (dto.getFechaFinalizacion() != null && dto.getFechaFinalizacion().isBefore(dto.getFechaInicio())) {
            throw new SolicitudInvalidaException("La fecha de finalización no puede ser anterior a la fecha de inicio");
        }

        List<TrabajosOrdenModel.RepuestoUtilizado> utilizadosModelo = new ArrayList<>();
        if (dto.getRepuestosUtilizados() != null) {
            for (EjecutarTrabajoRequestDTO.RepuestoUtilizadoDTO rDto : dto.getRepuestosUtilizados()) {
                ObjectId repuestoId = convertirAObjectId(rDto.getRepuestoId(), "repuestosUtilizados.repuestoId");
                RepuestosModel repCatalogo = repuestosRepository.findById(repuestoId)
                        .orElseThrow(() -> new RecursoNoEncontradoException("No existe un repuesto con el id " + rDto.getRepuestoId()));

                String nombreRep = (rDto.getNombre() != null && !rDto.getNombre().isBlank())
                        ? rDto.getNombre()
                        : repCatalogo.getNombre();
                Integer precioAplicadoObj = rDto.getPrecioUnitarioAplicado();
                Integer precioAplicado = (precioAplicadoObj != null)
                        ? precioAplicadoObj
                        : repCatalogo.getPrecio();

                utilizadosModelo.add(new TrabajosOrdenModel.RepuestoUtilizado(
                        repuestoId, nombreRep, rDto.getCantidad(), precioAplicado));
            }
        }

        trabajo.setTecnicoId(tecnico.getId());
        trabajo.setEjecucion(new TrabajosOrdenModel.Ejecucion(
                dto.getFechaInicio(),
                dto.getFechaFinalizacion(),
                dto.getEstado(),
                dto.getDescripcionRealizada(),
                utilizadosModelo));

        return convertirADTO(trabajosOrdenRepository.save(trabajo));
    }

    // ---------- Métodos de apoyo ----------

    private OrdenesServicioModel buscarOrden(String ordenId) {
        ObjectId id = convertirAObjectId(ordenId, "ordenId");
        return ordenesRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe una orden de servicio con el id " + ordenId));
    }

    private TrabajosOrdenModel buscarTrabajo(String idTrabajo) {
        ObjectId id = convertirAObjectId(idTrabajo, "id");
        return trabajosOrdenRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un trabajo con el id " + idTrabajo));
    }

    private UsuariosModel buscarUsuarioConRol(String idUsuario, String rol, String etiqueta) {
        ObjectId id = convertirAObjectId(idUsuario, "id de " + etiqueta);
        UsuariosModel usuario = usuariosRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe " + etiqueta + " con el id " + idUsuario));
        if (!rol.equals(usuario.getRol())) {
            throw new SolicitudInvalidaException("El usuario " + idUsuario + " no tiene el rol " + rol);
        }
        return usuario;
    }

    private ObjectId convertirAObjectId(String valor, String campo) {
        if (valor == null || !ObjectId.isValid(valor)) {
            throw new SolicitudInvalidaException("El valor de '" + campo + "' no tiene un formato de id válido");
        }
        return new ObjectId(valor);
    }

    private Integer calcularValorEstimado(TrabajosOrdenModel t) {
        Integer manoObraObj = t.getManoObraEstimada();
        int manoObra = manoObraObj != null ? manoObraObj : 0;
        int repuestos = 0;
        if (t.getRepuestosPrevistos() != null) {
            for (TrabajosOrdenModel.RepuestoPrevisto r : t.getRepuestosPrevistos()) {
                Integer cantObj = r.getCantidad();
                Integer precioObj = r.getPrecioUnitarioEstimado();
                int cant = cantObj != null ? cantObj : 0;
                int precio = precioObj != null ? precioObj : 0;
                repuestos += cant * precio;
            }
        }
        return manoObra + repuestos;
    }

    private TrabajoOrdenResponseDTO convertirADTO(TrabajosOrdenModel t) {
        ProponerTrabajoRequestDTO.ServicioTrabajoDTO servicioDTO = null;
        if (t.getServicio() != null) {
            servicioDTO = new ProponerTrabajoRequestDTO.ServicioTrabajoDTO(
                    t.getServicio().getServicioId() != null ? t.getServicio().getServicioId().toHexString() : null,
                    t.getServicio().getNombre(),
                    t.getServicio().getDescripcion());
        }

        List<ProponerTrabajoRequestDTO.RepuestoPrevistoDTO> previstosDTO = new ArrayList<>();
        if (t.getRepuestosPrevistos() != null) {
            for (TrabajosOrdenModel.RepuestoPrevisto r : t.getRepuestosPrevistos()) {
                previstosDTO.add(new ProponerTrabajoRequestDTO.RepuestoPrevistoDTO(
                        r.getRepuestoId().toHexString(),
                        r.getNombre(),
                        r.getEspecificaciones(),
                        r.getCantidad(),
                        r.getPrecioUnitarioEstimado()));
            }
        }

        TrabajoOrdenResponseDTO.AutorizacionDTO autDTO = null;
        if (t.getAutorizacion() != null) {
            autDTO = new TrabajoOrdenResponseDTO.AutorizacionDTO(
                    t.getAutorizacion().getDecision(),
                    t.getAutorizacion().getFecha(),
                    t.getAutorizacion().getComentarios());
        }

        TrabajoOrdenResponseDTO.EjecucionDTO ejeDTO = null;
        if (t.getEjecucion() != null) {
            List<EjecutarTrabajoRequestDTO.RepuestoUtilizadoDTO> usadosDTO = new ArrayList<>();
            int totalRepuestos = 0;
            if (t.getEjecucion().getRepuestosUtilizados() != null) {
                for (TrabajosOrdenModel.RepuestoUtilizado ru : t.getEjecucion().getRepuestosUtilizados()) {
                    usadosDTO.add(new EjecutarTrabajoRequestDTO.RepuestoUtilizadoDTO(
                            ru.getRepuestoId().toHexString(),
                            ru.getNombre(),
                            ru.getCantidad(),
                            ru.getPrecioUnitarioAplicado()));
                    Integer cantObj = ru.getCantidad();
                    Integer precioObj = ru.getPrecioUnitarioAplicado();
                    int cant = cantObj != null ? cantObj : 0;
                    int precio = precioObj != null ? precioObj : 0;
                    totalRepuestos += cant * precio;
                }
            }
            ejeDTO = new TrabajoOrdenResponseDTO.EjecucionDTO(
                    t.getEjecucion().getFechaInicio(),
                    t.getEjecucion().getFechaFinalizacion(),
                    t.getEjecucion().getEstado(),
                    t.getEjecucion().getDescripcionRealizada(),
                    usadosDTO,
                    totalRepuestos);
        }

        return new TrabajoOrdenResponseDTO(
                t.getId().toHexString(),
                t.getOrdenId().toHexString(),
                t.getTecnicoId().toHexString(),
                servicioDTO,
                t.getDescripcionIntervencion(),
                t.getComponente(),
                t.getManoObraEstimada(),
                previstosDTO,
                calcularValorEstimado(t),
                t.getObservaciones(),
                autDTO,
                ejeDTO);
    }
}
package co.edu.uao.tallerbicicletas.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uao.tallerbicicletas.dto.AdjuntoDTO;
import co.edu.uao.tallerbicicletas.dto.BicicletaOrdenDTO;
import co.edu.uao.tallerbicicletas.dto.CambioEstadoRequestDTO;
import co.edu.uao.tallerbicicletas.dto.EntregaRequestDTO;
import co.edu.uao.tallerbicicletas.dto.EntregaResponseDTO;
import co.edu.uao.tallerbicicletas.dto.HallazgoDTO;
import co.edu.uao.tallerbicicletas.dto.HistorialEstadoDTO;
import co.edu.uao.tallerbicicletas.dto.OrdenRequestDTO;
import co.edu.uao.tallerbicicletas.dto.OrdenResponseDTO;
import co.edu.uao.tallerbicicletas.dto.PuntoVerificadoDTO;
import co.edu.uao.tallerbicicletas.dto.RecepcionDTO;
import co.edu.uao.tallerbicicletas.dto.RevisionEntregaRequestDTO;
import co.edu.uao.tallerbicicletas.dto.RevisionEntregaResponseDTO;
import co.edu.uao.tallerbicicletas.dto.ValoracionRequestDTO;
import co.edu.uao.tallerbicicletas.dto.ValoracionResponseDTO;
import co.edu.uao.tallerbicicletas.exception.ConflictoException;
import co.edu.uao.tallerbicicletas.exception.RecursoNoEncontradoException;
import co.edu.uao.tallerbicicletas.exception.SolicitudInvalidaException;
import co.edu.uao.tallerbicicletas.model.Adjunto;
import co.edu.uao.tallerbicicletas.model.BicicletaOrden;
import co.edu.uao.tallerbicicletas.model.BicicletasModel;
import co.edu.uao.tallerbicicletas.model.CitasModel;
import co.edu.uao.tallerbicicletas.model.Diagnostico;
import co.edu.uao.tallerbicicletas.model.Entrega;
import co.edu.uao.tallerbicicletas.model.Hallazgo;
import co.edu.uao.tallerbicicletas.model.HistorialEstado;
import co.edu.uao.tallerbicicletas.model.OrdenesServicioModel;
import co.edu.uao.tallerbicicletas.model.PuntoVerificado;
import co.edu.uao.tallerbicicletas.model.Recepcion;
import co.edu.uao.tallerbicicletas.model.RevisionEntrega;
import co.edu.uao.tallerbicicletas.model.TrabajosOrdenModel;
import co.edu.uao.tallerbicicletas.model.UsuariosModel;
import co.edu.uao.tallerbicicletas.model.ValoracionServicio;
import co.edu.uao.tallerbicicletas.repository.IBicicletasRepository;
import co.edu.uao.tallerbicicletas.repository.ICitasRepository;
import co.edu.uao.tallerbicicletas.repository.IOrdenesServicioRepository;
import co.edu.uao.tallerbicicletas.repository.ITrabajosOrdenRepository;
import co.edu.uao.tallerbicicletas.repository.IUsuariosRepository;

@Service
public class OrdenesServiceImp implements IOrdenService {
    @Autowired IOrdenesServicioRepository ordenesRepository;
    @Autowired IUsuariosRepository usuariosRepository;
    @Autowired IBicicletasRepository bicicletasRepository;
    @Autowired ICitasRepository citasRepository;
    @Autowired ITrabajosOrdenRepository trabajosOrdenRepository;

    // Estados en los que una orden se considera activa (la bicicleta no puede tener otra)
    private static final List<String> ESTADOS_ACTIVOS = List.of(
            "recibida", "en_diagnostico", "pendiente_autorizacion", "en_ejecucion", "lista_entrega");

    // Cambios de estado permitidos desde cada estado.
    // lista_entrega y entregada NO se hacen aqui: se realizan en el flujo de revision y entrega.
    private static final Map<String, List<String>> TRANSICIONES = Map.of(
            "recibida", List.of("en_diagnostico", "cancelada"),
            "en_diagnostico", List.of("pendiente_autorizacion", "cancelada"),
            "pendiente_autorizacion", List.of("en_ejecucion", "cancelada"),
            "en_ejecucion", List.of("cancelada"),
            "lista_entrega", List.of("cancelada"),
            "entregada", List.of(),
            "cancelada", List.of());

    @Override
    public OrdenResponseDTO crearOrden(OrdenRequestDTO dto) {
        // 1. Las personas involucradas deben existir y tener el rol correcto
        UsuariosModel cliente = buscarUsuarioConRol(dto.getClienteId(), "cliente", "el cliente");
        UsuariosModel coordinador = buscarUsuarioConRol(dto.getCoordinadorId(), "coordinador", "el coordinador");

        Set<ObjectId> tecnicosUnicos = new HashSet<>();
        List<ObjectId> tecnicos = new ArrayList<>();
        for (String idTecnico : dto.getTecnicosAsignados()) {
            ObjectId tecnicoObjId = buscarUsuarioConRol(idTecnico, "tecnico", "el técnico").getId();
            if (!tecnicosUnicos.add(tecnicoObjId)) {
                throw new SolicitudInvalidaException("No se puede asignar el mismo técnico más de una vez en la orden");
            }
            tecnicos.add(tecnicoObjId);
        }

        // 2. La bicicleta debe existir y pertenecer al cliente
        ObjectId idBicicleta = convertirAObjectId(dto.getBicicletaId(), "bicicletaId");
        BicicletasModel bicicleta = bicicletasRepository.findById(idBicicleta)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe una bicicleta con el id " + dto.getBicicletaId()));
        if (!cliente.getId().equals(bicicleta.getPropietarioId())) {
            throw new SolicitudInvalidaException("La bicicleta no pertenece al cliente indicado");
        }

        // 3. Una bicicleta no puede tener más de una orden activa simultáneamente
        if (ordenesRepository.existsByBicicleta_BicicletaIdAndEstadoIn(idBicicleta, ESTADOS_ACTIVOS)) {
            throw new ConflictoException("La bicicleta ya tiene una orden de servicio activa");
        }

        // 4. La cita es opcional (también se reciben bicicletas sin cita previa)
        ObjectId idCita = null;
        CitasModel cita = null;
        if (dto.getCitaId() != null && !dto.getCitaId().isBlank()) {
            idCita = convertirAObjectId(dto.getCitaId(), "citaId");
            cita = citasRepository.findById(idCita)
                    .orElseThrow(() -> new RecursoNoEncontradoException("No existe una cita con el id " + dto.getCitaId()));

            if (!cita.getClienteId().equals(cliente.getId()) || !cita.getBicicletaId().equals(idBicicleta)) {
                throw new SolicitudInvalidaException("La cita indicada no corresponde al cliente o a la bicicleta de la orden");
            }
            if (!"confirmada".equals(cita.getEstado())) {
                throw new ConflictoException("Solo se puede crear una orden desde una cita en estado 'confirmada'");
            }
        }

        // 5. DTO -> Model
        LocalDateTime ahora = LocalDateTime.now();
        OrdenesServicioModel orden = new OrdenesServicioModel();
        orden.setClienteId(cliente.getId());
        orden.setBicicleta(new BicicletaOrden(idBicicleta, bicicleta.getMarca(), bicicleta.getModelo()));
        orden.setCitaId(idCita);
        orden.setFechaRecepcion(ahora);
        orden.setFechaEstimadaEntrega(dto.getFechaEstimadaEntrega());
        orden.setCoordinadorId(coordinador.getId());
        orden.setTecnicosAsignados(tecnicos);
        orden.setEstado("recibida");
        orden.setRecepcion(convertirRecepcionAModelo(dto.getRecepcion()));

        List<HistorialEstado> historial = new ArrayList<>();
        historial.add(new HistorialEstado("recibida", ahora, coordinador.getId(), "Orden creada al recibir la bicicleta"));
        orden.setHistorialEstados(historial);

        OrdenesServicioModel guardada = ordenesRepository.save(orden);

        // Si venía de una cita confirmada, marcarla como atendida
        if (cita != null) {
            cita.setEstado("atendida");
            citasRepository.save(cita);
        }

        return convertirADTO(guardada);
    }

    @Override
    public List<OrdenResponseDTO> listarOrdenes() {
        return ordenesRepository.findAll().stream().map(this::convertirADTO).toList();
    }

    @Override
    public List<OrdenResponseDTO> listarOrdenesPorBicicleta(String idBicicleta) {
        ObjectId id = convertirAObjectId(idBicicleta, "bicicletaId");
        return ordenesRepository.findByBicicleta_BicicletaId(id).stream().map(this::convertirADTO).toList();
    }

    @Override
    public List<OrdenResponseDTO> listarOrdenesPorCliente(String idCliente) {
        ObjectId id = convertirAObjectId(idCliente, "clienteId");
        return ordenesRepository.findByClienteId(id).stream().map(this::convertirADTO).toList();
    }

    @Override
    public List<OrdenResponseDTO> listarOrdenesPorEstado(String estado) {
        return ordenesRepository.findByEstado(estado).stream().map(this::convertirADTO).toList();
    }

    @Override
    public OrdenResponseDTO buscarOrdenPorId(String idOrden) {
        return convertirADTO(buscarModelo(idOrden));
    }

    @Override
    public OrdenResponseDTO cambiarEstado(String idOrden, CambioEstadoRequestDTO cambio) {
        OrdenesServicioModel orden = buscarModelo(idOrden);

        ObjectId idUsuario = convertirAObjectId(cambio.getUsuarioId(), "usuarioId");
        UsuariosModel usuario = usuariosRepository.findById(idUsuario)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un usuario con el id " + cambio.getUsuarioId()));

        // Solo un coordinador o un técnico asignado a esta orden puede cambiar su estado
        boolean esCoordinador = "coordinador".equals(usuario.getRol());
        boolean esTecnicoAsignado = "tecnico".equals(usuario.getRol())
                && orden.getTecnicosAsignados() != null
                && orden.getTecnicosAsignados().contains(usuario.getId());
        if (!esCoordinador && !esTecnicoAsignado) {
            throw new ConflictoException("Solo un coordinador o un técnico asignado a la orden puede cambiar su estado");
        }

        String nuevoEstado = cambio.getEstado();
        if (nuevoEstado.equals("lista_entrega") || nuevoEstado.equals("entregada")) {
            throw new ConflictoException("El paso a '" + nuevoEstado + "' se realiza desde el flujo de revisión y entrega de la orden");
        }
        if (!TRANSICIONES.get(orden.getEstado()).contains(nuevoEstado)) {
            throw new ConflictoException("No se puede pasar una orden de '" + orden.getEstado() + "' a '" + nuevoEstado + "'");
        }
        if (nuevoEstado.equals("pendiente_autorizacion") && !tieneHallazgos(orden)) {
            throw new ConflictoException("No se puede pasar a pendiente de autorización sin hallazgos registrados en el diagnóstico");
        }

        orden.setEstado(nuevoEstado);
        if (orden.getHistorialEstados() == null) {
            orden.setHistorialEstados(new ArrayList<>());
        }
        orden.getHistorialEstados().add(new HistorialEstado(nuevoEstado, LocalDateTime.now(), idUsuario, cambio.getObservacion()));
        return convertirADTO(ordenesRepository.save(orden));
    }

    @Override
    public OrdenResponseDTO agregarHallazgo(String idOrden, HallazgoDTO dto) {
        OrdenesServicioModel orden = buscarModelo(idOrden);

        if (!"en_diagnostico".equals(orden.getEstado())) {
            throw new ConflictoException("Solo se pueden registrar hallazgos mientras la orden está en diagnóstico");
        }

        // El técnico debe existir, tener rol tecnico y estar asignado a esta orden
        UsuariosModel tecnico = buscarUsuarioConRol(dto.getTecnicoId(), "tecnico", "el técnico");
        if (!orden.getTecnicosAsignados().contains(tecnico.getId())) {
            throw new ConflictoException("El técnico no está asignado a esta orden");
        }

        Hallazgo hallazgo = new Hallazgo(tecnico.getId(), dto.getComponente(), dto.getCondicion(),
                dto.getDescripcion(), dto.getPrioridad(), dto.getObservaciones(),
                dto.getAccionesRecomendadas(), convertirAdjuntosAModelo(dto.getEvidencias()));

        if (orden.getDiagnostico() == null) {
            orden.setDiagnostico(new Diagnostico(new ArrayList<>()));
        }
        if (orden.getDiagnostico().getHallazgos() == null) {
            orden.getDiagnostico().setHallazgos(new ArrayList<>());
        }
        orden.getDiagnostico().getHallazgos().add(hallazgo);
        return convertirADTO(ordenesRepository.save(orden));
    }

    @Override
    public OrdenResponseDTO registrarRevisionEntrega(String idOrden, RevisionEntregaRequestDTO dto) {
        OrdenesServicioModel orden = buscarModelo(idOrden);

        if (!"en_ejecucion".equals(orden.getEstado())) {
            throw new ConflictoException("Solo se puede registrar la revisión de entrega cuando la orden está en estado 'en_ejecucion'");
        }

        ObjectId idUsuario = convertirAObjectId(dto.getUsuarioId(), "usuarioId");
        UsuariosModel usuario = usuariosRepository.findById(idUsuario)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un usuario con el id " + dto.getUsuarioId()));

        boolean esCoordinador = "coordinador".equals(usuario.getRol());
        boolean esTecnicoAsignado = "tecnico".equals(usuario.getRol())
                && orden.getTecnicosAsignados() != null
                && orden.getTecnicosAsignados().contains(usuario.getId());
        if (!esCoordinador && !esTecnicoAsignado) {
            throw new ConflictoException("Solo un coordinador o un técnico asignado a la orden puede registrar la revisión de entrega");
        }

        // Validar trabajos asociados a la orden:
        // 1. No deben existir trabajos pendientes de decisión del cliente (autorizacion == null)
        // 2. Todos los trabajos autorizados deben estar en ejecucion.estado == "finalizado"
        List<TrabajosOrdenModel> trabajos = trabajosOrdenRepository.findByOrdenId(orden.getId());
        boolean hayPendientesAutorizacion = trabajos.stream()
                .anyMatch(t -> t.getAutorizacion() == null);
        if (hayPendientesAutorizacion) {
            throw new ConflictoException("No se puede realizar la revisión de entrega porque existen trabajos pendientes de autorización por el cliente");
        }

        boolean hayAutorizadosSinFinalizar = trabajos.stream()
                .filter(t -> "autorizado".equals(t.getAutorizacion().getDecision()))
                .anyMatch(t -> t.getEjecucion() == null || !"finalizado".equals(t.getEjecucion().getEstado()));
        if (hayAutorizadosSinFinalizar) {
            throw new ConflictoException("No se puede realizar la revisión de entrega porque existen trabajos autorizados sin finalizar");
        }

        List<PuntoVerificado> puntos = new ArrayList<>();
        for (PuntoVerificadoDTO p : dto.getPuntosVerificados()) {
            puntos.add(new PuntoVerificado(p.getPunto(), p.getResultado()));
        }

        LocalDateTime ahora = LocalDateTime.now();
        orden.setRevisionEntrega(new RevisionEntrega(ahora, puntos, dto.getAprobada(), dto.getRecomendaciones()));

        // Solo si la revisión es aprobada, la orden pasa a 'lista_entrega'
        if (Boolean.TRUE.equals(dto.getAprobada())) {
            orden.setEstado("lista_entrega");
            if (orden.getHistorialEstados() == null) {
                orden.setHistorialEstados(new ArrayList<>());
            }
            String obs = (dto.getRecomendaciones() != null && !dto.getRecomendaciones().isBlank())
                    ? dto.getRecomendaciones()
                    : "Revisión de entrega aprobada";
            orden.getHistorialEstados().add(new HistorialEstado("lista_entrega", ahora, idUsuario, obs));
        }

        return convertirADTO(ordenesRepository.save(orden));
    }

    @Override
    public OrdenResponseDTO registrarEntrega(String idOrden, EntregaRequestDTO dto) {
        OrdenesServicioModel orden = buscarModelo(idOrden);

        if (!"lista_entrega".equals(orden.getEstado())) {
            throw new ConflictoException("Solo se puede registrar la entrega cuando la orden está en estado 'lista_entrega'");
        }

        if (orden.getRevisionEntrega() == null || !Boolean.TRUE.equals(orden.getRevisionEntrega().getAprobada())) {
            throw new ConflictoException("La orden no cuenta con una revisión de entrega aprobada");
        }

        if (!Boolean.TRUE.equals(dto.getConfirmacionRecepcionCliente())) {
            throw new SolicitudInvalidaException("Se requiere la confirmación de recepción del cliente para completar la entrega");
        }

        ObjectId idUsuario = convertirAObjectId(dto.getUsuarioId(), "usuarioId");
        UsuariosModel usuario = usuariosRepository.findById(idUsuario)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un usuario con el id " + dto.getUsuarioId()));

        if (!"coordinador".equals(usuario.getRol())) {
            throw new ConflictoException("Solo un coordinador puede registrar la entrega final de la bicicleta");
        }

        LocalDateTime ahora = LocalDateTime.now();
        orden.setEntrega(new Entrega(ahora, dto.getConfirmacionRecepcionCliente()));
        orden.setEstado("entregada");

        if (orden.getHistorialEstados() == null) {
            orden.setHistorialEstados(new ArrayList<>());
        }
        orden.getHistorialEstados().add(new HistorialEstado("entregada", ahora, idUsuario, "Bicicleta entregada al cliente"));

        return convertirADTO(ordenesRepository.save(orden));
    }

    @Override
    public OrdenResponseDTO registrarValoracion(String idOrden, ValoracionRequestDTO dto) {
        OrdenesServicioModel orden = buscarModelo(idOrden);

        if (!"entregada".equals(orden.getEstado())) {
            throw new ConflictoException("Solo se pueden valorar órdenes de servicio en estado 'entregada'");
        }

        if (orden.getValoracionServicio() != null) {
            throw new ConflictoException("Esta orden de servicio ya cuenta con una valoración registrada");
        }

        UsuariosModel cliente = buscarUsuarioConRol(dto.getUsuarioId(), "cliente", "el cliente");
        if (!orden.getClienteId().equals(cliente.getId())) {
            throw new ConflictoException("Solo el cliente propietario de la orden puede registrar su valoración");
        }

        ValoracionServicio valoracion = new ValoracionServicio();
        valoracion.setCalificacion(dto.getCalificacion());
        valoracion.setComentarios(dto.getComentarios());
        valoracion.setSugerencias(dto.getSugerencias());
        valoracion.setFecha(LocalDateTime.now());

        orden.setValoracionServicio(valoracion);
        return convertirADTO(ordenesRepository.save(orden));
    }

    // ---------- Métodos de apoyo ----------

    private boolean tieneHallazgos(OrdenesServicioModel orden) {
        return orden.getDiagnostico() != null
                && orden.getDiagnostico().getHallazgos() != null
                && !orden.getDiagnostico().getHallazgos().isEmpty();
    }

    private ObjectId convertirAObjectId(String valor, String campo) {
        if (valor == null || !ObjectId.isValid(valor)) {
            throw new SolicitudInvalidaException("El valor de '" + campo + "' no tiene un formato de id válido");
        }
        return new ObjectId(valor);
    }

    private OrdenesServicioModel buscarModelo(String idOrden) {
        ObjectId id = convertirAObjectId(idOrden, "id");
        return ordenesRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe una orden de servicio con el id " + idOrden));
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

    // ---------- Conversiones DTO <-> Model ----------

    private Recepcion convertirRecepcionAModelo(RecepcionDTO dto) {
        List<String> accesorios = new ArrayList<>();
        if (dto.getAccesorios() != null) {
            accesorios.addAll(dto.getAccesorios());
        }
        return new Recepcion(dto.getProblemaReportado(), dto.getCondicionesObservadas(),
                accesorios, convertirAdjuntosAModelo(dto.getFotografias()));
    }

    private List<Adjunto> convertirAdjuntosAModelo(List<AdjuntoDTO> adjuntosDTO) {
        List<Adjunto> adjuntos = new ArrayList<>();
        if (adjuntosDTO != null) {
            for (AdjuntoDTO a : adjuntosDTO) {
                adjuntos.add(new Adjunto(a.getNombreArchivo(), a.getUbicacion(), a.getDescripcion()));
            }
        }
        return adjuntos;
    }

    private List<AdjuntoDTO> convertirAdjuntosADTO(List<Adjunto> adjuntos) {
        List<AdjuntoDTO> adjuntosDTO = new ArrayList<>();
        if (adjuntos != null) {
            for (Adjunto a : adjuntos) {
                adjuntosDTO.add(new AdjuntoDTO(a.getNombreArchivo(), a.getUbicacion(), a.getDescripcion()));
            }
        }
        return adjuntosDTO;
    }

    private OrdenResponseDTO convertirADTO(OrdenesServicioModel orden) {
        OrdenResponseDTO dto = new OrdenResponseDTO();
        dto.setId(orden.getId().toHexString());
        dto.setClienteId(orden.getClienteId().toHexString());
        dto.setBicicleta(new BicicletaOrdenDTO(orden.getBicicleta().getBicicletaId().toHexString(),
                orden.getBicicleta().getMarca(), orden.getBicicleta().getModelo()));
        dto.setCitaId(orden.getCitaId() != null ? orden.getCitaId().toHexString() : null);
        dto.setFechaRecepcion(orden.getFechaRecepcion());
        dto.setFechaEstimadaEntrega(orden.getFechaEstimadaEntrega());
        dto.setCoordinadorId(orden.getCoordinadorId().toHexString());
        dto.setEstado(orden.getEstado());

        List<String> tecnicos = new ArrayList<>();
        if (orden.getTecnicosAsignados() != null) {
            for (ObjectId t : orden.getTecnicosAsignados()) {
                tecnicos.add(t.toHexString());
            }
        }
        dto.setTecnicosAsignados(tecnicos);

        if (orden.getRecepcion() != null) {
            Recepcion r = orden.getRecepcion();
            dto.setRecepcion(new RecepcionDTO(r.getProblemaReportado(), r.getCondicionesObservadas(),
                    r.getAccesorios(), convertirAdjuntosADTO(r.getFotografias())));
        }

        List<HallazgoDTO> hallazgos = new ArrayList<>();
        if (tieneHallazgos(orden)) {
            for (Hallazgo h : orden.getDiagnostico().getHallazgos()) {
                hallazgos.add(new HallazgoDTO(h.getTecnicoId().toHexString(), h.getComponente(), h.getCondicion(),
                        h.getDescripcion(), h.getPrioridad(), h.getObservaciones(), h.getAccionesRecomendadas(),
                        convertirAdjuntosADTO(h.getEvidencias())));
            }
        }
        dto.setHallazgos(hallazgos);

        List<HistorialEstadoDTO> historial = new ArrayList<>();
        if (orden.getHistorialEstados() != null) {
            for (HistorialEstado e : orden.getHistorialEstados()) {
                historial.add(new HistorialEstadoDTO(e.getEstado(), e.getFecha(), e.getUsuarioId().toHexString(), e.getObservacion()));
            }
        }
        dto.setHistorialEstados(historial);

        if (orden.getRevisionEntrega() != null) {
            RevisionEntrega rev = orden.getRevisionEntrega();
            List<PuntoVerificadoDTO> puntosDTO = new ArrayList<>();
            if (rev.getPuntosVerificados() != null) {
                for (PuntoVerificado p : rev.getPuntosVerificados()) {
                    puntosDTO.add(new PuntoVerificadoDTO(p.getPunto(), p.getResultado()));
                }
            }
            dto.setRevisionEntrega(new RevisionEntregaResponseDTO(
                    rev.getFecha(), puntosDTO, rev.getAprobada(), rev.getRecomendaciones()));
        }

        if (orden.getEntrega() != null) {
            Entrega ent = orden.getEntrega();
            dto.setEntrega(new EntregaResponseDTO(ent.getFecha(), ent.getConfirmacionRecepcionCliente()));
        }

        if (orden.getValoracionServicio() != null) {
            ValoracionServicio val = orden.getValoracionServicio();
            dto.setValoracionServicio(new ValoracionResponseDTO(
                    val.getCalificacion(), val.getComentarios(), val.getSugerencias(), val.getFecha()));
        }

        return dto;
    }
}
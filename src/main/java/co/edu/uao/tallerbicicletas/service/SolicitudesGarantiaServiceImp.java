package co.edu.uao.tallerbicicletas.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uao.tallerbicicletas.dto.CrearSolicitudGarantiaRequestDTO;
import co.edu.uao.tallerbicicletas.dto.ResolverSolicitudGarantiaRequestDTO;
import co.edu.uao.tallerbicicletas.dto.SolicitudGarantiaResponseDTO;
import co.edu.uao.tallerbicicletas.exception.ConflictoException;
import co.edu.uao.tallerbicicletas.exception.RecursoNoEncontradoException;
import co.edu.uao.tallerbicicletas.exception.SolicitudInvalidaException;
import co.edu.uao.tallerbicicletas.model.SolicitudesGarantiaModel;
import co.edu.uao.tallerbicicletas.model.TrabajosOrdenModel;
import co.edu.uao.tallerbicicletas.repository.ISolicitudesGarantiaRepository;
import co.edu.uao.tallerbicicletas.repository.ITrabajosOrdenRepository;

@Service
public class SolicitudesGarantiaServiceImp implements ISolicitudGarantiaService {

    @Autowired
    private ISolicitudesGarantiaRepository garantiaRepository;

    @Autowired
    private ITrabajosOrdenRepository trabajosOrdenRepository;

    @Override
    public SolicitudGarantiaResponseDTO crearSolicitud(CrearSolicitudGarantiaRequestDTO dto) {
        ObjectId trabajoId = convertirAObjectId(dto.getTrabajoId(), "trabajoId");
        TrabajosOrdenModel trabajo = trabajosOrdenRepository.findById(trabajoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un trabajo con el id " + dto.getTrabajoId()));

        if (trabajo.getEjecucion() == null || !"finalizado".equals(trabajo.getEjecucion().getEstado())) {
            throw new ConflictoException("Solo se puede solicitar garantía sobre trabajos finalizados");
        }

        if (trabajo.getGarantia() != null
                && trabajo.getGarantia().getFechaInicio() != null
                && trabajo.getGarantia().getDuracion() != null) {
            LocalDateTime fechaVencimiento = trabajo.getGarantia().getFechaInicio()
                    .plusDays(trabajo.getGarantia().getDuracion());
            if (LocalDateTime.now().isAfter(fechaVencimiento)) {
                throw new ConflictoException("La garantía de este trabajo ya expiró el " + fechaVencimiento);
            }
        }

        List<SolicitudesGarantiaModel.EvidenciaGarantia> evidenciasModelo = new ArrayList<>();
        for (CrearSolicitudGarantiaRequestDTO.EvidenciaGarantiaDTO eDto : dto.getEvidencias()) {
            evidenciasModelo.add(new SolicitudesGarantiaModel.EvidenciaGarantia(
                    eDto.getNombreArchivo(),
                    eDto.getDescripcion(),
                    eDto.getUbicacion()));
        }

        SolicitudesGarantiaModel solicitud = new SolicitudesGarantiaModel();
        solicitud.setTrabajoId(trabajo.getId());
        solicitud.setDescripcion(dto.getDescripcion());
        solicitud.setFecha(LocalDateTime.now());
        solicitud.setEvidencias(evidenciasModelo);
        solicitud.setEstado("registrada");

        return convertirADTO(garantiaRepository.save(solicitud));
    }

    @Override
    public List<SolicitudGarantiaResponseDTO> listarTodas() {
        return garantiaRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    @Override
    public SolicitudGarantiaResponseDTO buscarPorId(String id) {
        return convertirADTO(buscarSolicitud(id));
    }

    @Override
    public List<SolicitudGarantiaResponseDTO> listarPorTrabajo(String trabajoId) {
        ObjectId id = convertirAObjectId(trabajoId, "trabajoId");
        return garantiaRepository.findByTrabajoId(id).stream()
                .map(this::convertirADTO)
                .toList();
    }

    @Override
    public SolicitudGarantiaResponseDTO actualizarResolucion(String id, ResolverSolicitudGarantiaRequestDTO dto) {
        SolicitudesGarantiaModel solicitud = buscarSolicitud(id);

        if ("resuelta".equals(dto.getEstado())) {
            if (dto.getProcede() == null) {
                throw new SolicitudInvalidaException("Debe indicar si la garantía procede (true/false) al resolverla");
            }
            if (Boolean.TRUE.equals(dto.getProcede())
                    && (dto.getSolucionAplicada() == null || dto.getSolucionAplicada().isBlank())) {
                throw new SolicitudInvalidaException("Debe indicar la solución aplicada cuando la garantía procede");
            }
        }

        solicitud.setEstado(dto.getEstado());
        if (dto.getProcede() != null) {
            solicitud.setProcede(dto.getProcede());
        }
        if (dto.getSolucionAplicada() != null) {
            solicitud.setSolucionAplicada(dto.getSolucionAplicada());
        }

        return convertirADTO(garantiaRepository.save(solicitud));
    }

    // ---------- Métodos de apoyo ----------

    private SolicitudesGarantiaModel buscarSolicitud(String id) {
        ObjectId objId = convertirAObjectId(id, "id");
        return garantiaRepository.findById(objId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe una solicitud de garantía con el id " + id));
    }

    private ObjectId convertirAObjectId(String valor, String campo) {
        if (valor == null || !ObjectId.isValid(valor)) {
            throw new SolicitudInvalidaException("El valor de '" + campo + "' no tiene un formato de id válido");
        }
        return new ObjectId(valor);
    }

    private SolicitudGarantiaResponseDTO convertirADTO(SolicitudesGarantiaModel s) {
        List<CrearSolicitudGarantiaRequestDTO.EvidenciaGarantiaDTO> evidenciasDTO = new ArrayList<>();
        if (s.getEvidencias() != null) {
            for (SolicitudesGarantiaModel.EvidenciaGarantia e : s.getEvidencias()) {
                evidenciasDTO.add(new CrearSolicitudGarantiaRequestDTO.EvidenciaGarantiaDTO(
                        e.getNombreArchivo(),
                        e.getDescripcion(),
                        e.getUbicacion()));
            }
        }

        return new SolicitudGarantiaResponseDTO(
                s.getId().toHexString(),
                s.getTrabajoId().toHexString(),
                s.getDescripcion(),
                s.getFecha(),
                evidenciasDTO,
                s.getEstado(),
                s.getProcede(),
                s.getSolucionAplicada());
    }
}
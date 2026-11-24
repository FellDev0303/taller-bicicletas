package co.edu.uao.tallerbicicletas.service;

import java.util.List;

import co.edu.uao.tallerbicicletas.dto.CrearSolicitudGarantiaRequestDTO;
import co.edu.uao.tallerbicicletas.dto.ResolverSolicitudGarantiaRequestDTO;
import co.edu.uao.tallerbicicletas.dto.SolicitudGarantiaResponseDTO;

public interface ISolicitudGarantiaService {
    SolicitudGarantiaResponseDTO crearSolicitud(CrearSolicitudGarantiaRequestDTO dto);
    List<SolicitudGarantiaResponseDTO> listarTodas();
    SolicitudGarantiaResponseDTO buscarPorId(String id);
    List<SolicitudGarantiaResponseDTO> listarPorTrabajo(String trabajoId);
    SolicitudGarantiaResponseDTO actualizarResolucion(String id, ResolverSolicitudGarantiaRequestDTO dto);
}
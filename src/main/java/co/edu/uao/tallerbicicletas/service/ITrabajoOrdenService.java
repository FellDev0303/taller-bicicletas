package co.edu.uao.tallerbicicletas.service;

import java.util.List;

import co.edu.uao.tallerbicicletas.dto.AutorizarTrabajoRequestDTO;
import co.edu.uao.tallerbicicletas.dto.EjecutarTrabajoRequestDTO;
import co.edu.uao.tallerbicicletas.dto.ProponerTrabajoRequestDTO;
import co.edu.uao.tallerbicicletas.dto.TrabajoOrdenResponseDTO;

public interface ITrabajoOrdenService {
    TrabajoOrdenResponseDTO proponerTrabajo(ProponerTrabajoRequestDTO dto);
    List<TrabajoOrdenResponseDTO> listarTrabajosPorOrden(String ordenId);
    TrabajoOrdenResponseDTO buscarTrabajoPorId(String id);
    TrabajoOrdenResponseDTO decidirAutorizacion(String idTrabajo, AutorizarTrabajoRequestDTO dto);
    TrabajoOrdenResponseDTO registrarEjecucion(String idTrabajo, EjecutarTrabajoRequestDTO dto);
}
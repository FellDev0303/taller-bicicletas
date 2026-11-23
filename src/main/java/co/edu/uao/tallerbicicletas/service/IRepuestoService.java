package co.edu.uao.tallerbicicletas.service;

import java.util.List;

import co.edu.uao.tallerbicicletas.dto.RepuestoRequestDTO;
import co.edu.uao.tallerbicicletas.dto.RepuestoResponseDTO;

public interface IRepuestoService {
    public RepuestoResponseDTO crearRepuesto(RepuestoRequestDTO repuesto);
    public List<RepuestoResponseDTO> listarRepuestos();
    public RepuestoResponseDTO buscarRepuestoPorId(String idRepuesto);
    public RepuestoResponseDTO actualizarRepuesto(String idRepuesto, RepuestoRequestDTO repuesto);
}
package co.edu.uao.tallerbicicletas.service;

import java.util.List;

import co.edu.uao.tallerbicicletas.dto.CancelarCitaRequestDTO;
import co.edu.uao.tallerbicicletas.dto.CitaRequestDTO;
import co.edu.uao.tallerbicicletas.dto.CitaResponseDTO;
import co.edu.uao.tallerbicicletas.dto.ConfirmarCitaRequestDTO;
import co.edu.uao.tallerbicicletas.dto.ReprogramarCitaRequestDTO;

public interface ICitaService {
    public CitaResponseDTO solicitarCita(CitaRequestDTO cita);
    public List<CitaResponseDTO> listarCitas();
    public List<CitaResponseDTO> listarCitasPorCliente(String idCliente);
    public List<CitaResponseDTO> listarCitasPorBicicleta(String idBicicleta);
    public List<CitaResponseDTO> listarCitasPorEstado(String estado);
    public CitaResponseDTO buscarCitaPorId(String idCita);
    public CitaResponseDTO confirmarCita(String idCita, ConfirmarCitaRequestDTO confirmacion);
    public CitaResponseDTO reprogramarCita(String idCita, ReprogramarCitaRequestDTO cambio);
    public CitaResponseDTO cancelarCita(String idCita, CancelarCitaRequestDTO cancelacion);
}

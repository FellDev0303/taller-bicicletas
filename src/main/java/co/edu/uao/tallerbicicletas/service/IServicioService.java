package co.edu.uao.tallerbicicletas.service;

import java.util.List;

import co.edu.uao.tallerbicicletas.dto.ServicioRequestDTO;
import co.edu.uao.tallerbicicletas.dto.ServicioResponseDTO;

public interface IServicioService {
    public ServicioResponseDTO crearServicio(ServicioRequestDTO servicio);
    public List<ServicioResponseDTO> listarServicios();
    public List<ServicioResponseDTO> listarServiciosPorCategoria(String categoria);
    public ServicioResponseDTO buscarServicioPorId(String idServicio);
    public ServicioResponseDTO actualizarServicio(String idServicio, ServicioRequestDTO servicio);
}

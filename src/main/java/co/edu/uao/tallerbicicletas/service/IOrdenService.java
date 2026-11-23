package co.edu.uao.tallerbicicletas.service;

import java.util.List;

import co.edu.uao.tallerbicicletas.dto.CambioEstadoRequestDTO;
import co.edu.uao.tallerbicicletas.dto.HallazgoDTO;
import co.edu.uao.tallerbicicletas.dto.OrdenRequestDTO;
import co.edu.uao.tallerbicicletas.dto.OrdenResponseDTO;

public interface IOrdenService {
    public OrdenResponseDTO crearOrden(OrdenRequestDTO orden);
    public List<OrdenResponseDTO> listarOrdenes();
    public List<OrdenResponseDTO> listarOrdenesPorBicicleta(String idBicicleta);
    public List<OrdenResponseDTO> listarOrdenesPorCliente(String idCliente);
    public List<OrdenResponseDTO> listarOrdenesPorEstado(String estado);
    public OrdenResponseDTO buscarOrdenPorId(String idOrden);
    public OrdenResponseDTO cambiarEstado(String idOrden, CambioEstadoRequestDTO cambio);
    public OrdenResponseDTO agregarHallazgo(String idOrden, HallazgoDTO hallazgo);
}

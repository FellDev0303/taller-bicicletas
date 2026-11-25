package co.edu.uao.tallerbicicletas.service;

import java.util.List;

import co.edu.uao.tallerbicicletas.dto.OrdenesPorEstadoEstadisticaDTO;
import co.edu.uao.tallerbicicletas.dto.ServicioSolicitadoEstadisticaDTO;
import co.edu.uao.tallerbicicletas.dto.TiemposAtencionEstadisticaDTO;
import co.edu.uao.tallerbicicletas.dto.ValoracionesEstadisticaDTO;

public interface IEstadisticasService {
    List<ServicioSolicitadoEstadisticaDTO> obtenerServiciosMasSolicitados();
    List<OrdenesPorEstadoEstadisticaDTO> obtenerOrdenesPorEstado();
    TiemposAtencionEstadisticaDTO obtenerTiemposAtencion();
    ValoracionesEstadisticaDTO obtenerEstadisticasValoraciones();
}
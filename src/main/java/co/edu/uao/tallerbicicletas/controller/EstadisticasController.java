package co.edu.uao.tallerbicicletas.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uao.tallerbicicletas.dto.OrdenesPorEstadoEstadisticaDTO;
import co.edu.uao.tallerbicicletas.dto.ServicioSolicitadoEstadisticaDTO;
import co.edu.uao.tallerbicicletas.dto.TiemposAtencionEstadisticaDTO;
import co.edu.uao.tallerbicicletas.dto.ValoracionesEstadisticaDTO;
import co.edu.uao.tallerbicicletas.service.IEstadisticasService;

@RestController
@RequestMapping("/UAO/apirest/estadisticas")
public class EstadisticasController {

    @Autowired
    private IEstadisticasService estadisticasService;

    @GetMapping("/serviciosMasSolicitados")
    public ResponseEntity<List<ServicioSolicitadoEstadisticaDTO>> obtenerServiciosMasSolicitados() {
        return new ResponseEntity<>(estadisticasService.obtenerServiciosMasSolicitados(), HttpStatus.OK);
    }

    @GetMapping("/ordenesPorEstado")
    public ResponseEntity<List<OrdenesPorEstadoEstadisticaDTO>> obtenerOrdenesPorEstado() {
        return new ResponseEntity<>(estadisticasService.obtenerOrdenesPorEstado(), HttpStatus.OK);
    }

    @GetMapping("/tiemposAtencion")
    public ResponseEntity<TiemposAtencionEstadisticaDTO> obtenerTiemposAtencion() {
        return new ResponseEntity<>(estadisticasService.obtenerTiemposAtencion(), HttpStatus.OK);
    }

    @GetMapping("/valoraciones")
    public ResponseEntity<ValoracionesEstadisticaDTO> obtenerEstadisticasValoraciones() {
        return new ResponseEntity<>(estadisticasService.obtenerEstadisticasValoraciones(), HttpStatus.OK);
    }
}
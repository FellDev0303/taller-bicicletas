package co.edu.uao.tallerbicicletas.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uao.tallerbicicletas.dto.CrearSolicitudGarantiaRequestDTO;
import co.edu.uao.tallerbicicletas.dto.ResolverSolicitudGarantiaRequestDTO;
import co.edu.uao.tallerbicicletas.dto.SolicitudGarantiaResponseDTO;
import co.edu.uao.tallerbicicletas.service.ISolicitudGarantiaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/UAO/apirest/solicitudesGarantia")
public class SolicitudesGarantiaController {

    @Autowired
    private ISolicitudGarantiaService solicitudGarantiaService;

    @PostMapping("/")
    public ResponseEntity<SolicitudGarantiaResponseDTO> crearSolicitud(
            @Valid @RequestBody CrearSolicitudGarantiaRequestDTO dto) {
        return new ResponseEntity<>(solicitudGarantiaService.crearSolicitud(dto), HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<SolicitudGarantiaResponseDTO>> listar(
            @RequestParam(required = false) String trabajoId) {
        if (trabajoId != null && !trabajoId.isBlank()) {
            return new ResponseEntity<>(solicitudGarantiaService.listarPorTrabajo(trabajoId), HttpStatus.OK);
        }
        return new ResponseEntity<>(solicitudGarantiaService.listarTodas(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitudGarantiaResponseDTO> buscarPorId(@PathVariable String id) {
        return new ResponseEntity<>(solicitudGarantiaService.buscarPorId(id), HttpStatus.OK);
    }

    @PutMapping("/{id}/resolucion")
    public ResponseEntity<SolicitudGarantiaResponseDTO> actualizarResolucion(
            @PathVariable String id,
            @Valid @RequestBody ResolverSolicitudGarantiaRequestDTO dto) {
        return new ResponseEntity<>(solicitudGarantiaService.actualizarResolucion(id, dto), HttpStatus.OK);
    }
}
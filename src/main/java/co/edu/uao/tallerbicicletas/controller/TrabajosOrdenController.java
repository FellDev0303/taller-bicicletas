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

import co.edu.uao.tallerbicicletas.dto.AutorizarTrabajoRequestDTO;
import co.edu.uao.tallerbicicletas.dto.EjecutarTrabajoRequestDTO;
import co.edu.uao.tallerbicicletas.dto.ProponerTrabajoRequestDTO;
import co.edu.uao.tallerbicicletas.dto.TrabajoOrdenResponseDTO;
import co.edu.uao.tallerbicicletas.service.ITrabajoOrdenService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/UAO/apirest/trabajosOrden")
public class TrabajosOrdenController {

    @Autowired
    private ITrabajoOrdenService trabajoOrdenService;

    @PostMapping("/")
    public ResponseEntity<TrabajoOrdenResponseDTO> proponerTrabajo(@Valid @RequestBody ProponerTrabajoRequestDTO dto) {
        return new ResponseEntity<TrabajoOrdenResponseDTO>(trabajoOrdenService.proponerTrabajo(dto), HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<TrabajoOrdenResponseDTO>> listarTrabajosPorOrden(@RequestParam String ordenId) {
        return new ResponseEntity<List<TrabajoOrdenResponseDTO>>(trabajoOrdenService.listarTrabajosPorOrden(ordenId), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrabajoOrdenResponseDTO> buscarTrabajoPorId(@PathVariable String id) {
        return new ResponseEntity<TrabajoOrdenResponseDTO>(trabajoOrdenService.buscarTrabajoPorId(id), HttpStatus.OK);
    }

    @PutMapping("/{id}/autorizacion")
    public ResponseEntity<TrabajoOrdenResponseDTO> decidirAutorizacion(
            @PathVariable String id,
            @Valid @RequestBody AutorizarTrabajoRequestDTO dto) {
        return new ResponseEntity<TrabajoOrdenResponseDTO>(trabajoOrdenService.decidirAutorizacion(id, dto), HttpStatus.OK);
    }

    @PutMapping("/{id}/ejecucion")
    public ResponseEntity<TrabajoOrdenResponseDTO> registrarEjecucion(
            @PathVariable String id,
            @Valid @RequestBody EjecutarTrabajoRequestDTO dto) {
        return new ResponseEntity<TrabajoOrdenResponseDTO>(trabajoOrdenService.registrarEjecucion(id, dto), HttpStatus.OK);
    }
}
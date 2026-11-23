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

import co.edu.uao.tallerbicicletas.dto.CancelarCitaRequestDTO;
import co.edu.uao.tallerbicicletas.dto.CitaRequestDTO;
import co.edu.uao.tallerbicicletas.dto.CitaResponseDTO;
import co.edu.uao.tallerbicicletas.dto.ConfirmarCitaRequestDTO;
import co.edu.uao.tallerbicicletas.dto.ReprogramarCitaRequestDTO;
import co.edu.uao.tallerbicicletas.service.ICitaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping ("/UAO/apirest/citas")
public class CitasController {
    @Autowired ICitaService citaService;

    @PostMapping ("/")
    public ResponseEntity<CitaResponseDTO> solicitarCita(@Valid @RequestBody CitaRequestDTO cita){
        return new ResponseEntity<CitaResponseDTO>(citaService.solicitarCita(cita), HttpStatus.CREATED);
    }

    // Sin parametros lista todas; con ?clienteId=, ?bicicletaId= o ?estado= filtra
    @GetMapping ("/")
    public ResponseEntity<List<CitaResponseDTO>> listarCitas(
            @RequestParam(required = false) String clienteId,
            @RequestParam(required = false) String bicicletaId,
            @RequestParam(required = false) String estado){
        List<CitaResponseDTO> citas;
        if (clienteId != null) {
            citas = citaService.listarCitasPorCliente(clienteId);
        } else if (bicicletaId != null) {
            citas = citaService.listarCitasPorBicicleta(bicicletaId);
        } else if (estado != null) {
            citas = citaService.listarCitasPorEstado(estado);
        } else {
            citas = citaService.listarCitas();
        }
        return new ResponseEntity<List<CitaResponseDTO>>(citas, HttpStatus.OK);
    }

    @GetMapping ("/{id}")
    public ResponseEntity<CitaResponseDTO> buscarCitaPorId(@PathVariable String id){
        return new ResponseEntity<CitaResponseDTO>(citaService.buscarCitaPorId(id), HttpStatus.OK);
    }

    @PutMapping ("/{id}/confirmar")
    public ResponseEntity<CitaResponseDTO> confirmarCita(@PathVariable String id, @Valid @RequestBody ConfirmarCitaRequestDTO confirmacion){
        return new ResponseEntity<CitaResponseDTO>(citaService.confirmarCita(id, confirmacion), HttpStatus.OK);
    }

    @PutMapping ("/{id}/reprogramar")
    public ResponseEntity<CitaResponseDTO> reprogramarCita(@PathVariable String id, @Valid @RequestBody ReprogramarCitaRequestDTO cambio){
        return new ResponseEntity<CitaResponseDTO>(citaService.reprogramarCita(id, cambio), HttpStatus.OK);
    }

    @PutMapping ("/{id}/cancelar")
    public ResponseEntity<CitaResponseDTO> cancelarCita(@PathVariable String id, @Valid @RequestBody CancelarCitaRequestDTO cancelacion){
        return new ResponseEntity<CitaResponseDTO>(citaService.cancelarCita(id, cancelacion), HttpStatus.OK);
    }
}
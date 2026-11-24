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

import co.edu.uao.tallerbicicletas.dto.CambioEstadoRequestDTO;
import co.edu.uao.tallerbicicletas.dto.EntregaRequestDTO;
import co.edu.uao.tallerbicicletas.dto.HallazgoDTO;
import co.edu.uao.tallerbicicletas.dto.OrdenRequestDTO;
import co.edu.uao.tallerbicicletas.dto.OrdenResponseDTO;
import co.edu.uao.tallerbicicletas.dto.RevisionEntregaRequestDTO;
import co.edu.uao.tallerbicicletas.dto.ValoracionRequestDTO;
import co.edu.uao.tallerbicicletas.service.IOrdenService;

import jakarta.validation.Valid;

@RestController
@RequestMapping ("/UAO/apirest/ordenes")
public class OrdenesController {
    @Autowired IOrdenService ordenService;

    @PostMapping ("/")
    public ResponseEntity<OrdenResponseDTO> crearOrden(@Valid @RequestBody OrdenRequestDTO orden){
        return new ResponseEntity<OrdenResponseDTO>(ordenService.crearOrden(orden), HttpStatus.CREATED);
    }

    // Sin parametros lista todas; con ?bicicletaId=, ?clienteId= o ?estado= filtra (el historial de una bicicleta se consulta asi)
    @GetMapping ("/")
    public ResponseEntity<List<OrdenResponseDTO>> listarOrdenes(
            @RequestParam(required = false) String bicicletaId,
            @RequestParam(required = false) String clienteId,
            @RequestParam(required = false) String estado){
        List<OrdenResponseDTO> ordenes;
        if (bicicletaId != null) {
            ordenes = ordenService.listarOrdenesPorBicicleta(bicicletaId);
        } else if (clienteId != null) {
            ordenes = ordenService.listarOrdenesPorCliente(clienteId);
        } else if (estado != null) {
            ordenes = ordenService.listarOrdenesPorEstado(estado);
        } else {
            ordenes = ordenService.listarOrdenes();
        }
        return new ResponseEntity<List<OrdenResponseDTO>>(ordenes, HttpStatus.OK);
    }

    @GetMapping ("/{id}")
    public ResponseEntity<OrdenResponseDTO> buscarOrdenPorId(@PathVariable String id){
        return new ResponseEntity<OrdenResponseDTO>(ordenService.buscarOrdenPorId(id), HttpStatus.OK);
    }

    @PutMapping ("/{id}/estado")
    public ResponseEntity<OrdenResponseDTO> cambiarEstado(@PathVariable String id, @Valid @RequestBody CambioEstadoRequestDTO cambio){
        return new ResponseEntity<OrdenResponseDTO>(ordenService.cambiarEstado(id, cambio), HttpStatus.OK);
    }

    @PostMapping ("/{id}/hallazgos")
    public ResponseEntity<OrdenResponseDTO> agregarHallazgo(@PathVariable String id, @Valid @RequestBody HallazgoDTO hallazgo){
        return new ResponseEntity<OrdenResponseDTO>(ordenService.agregarHallazgo(id, hallazgo), HttpStatus.CREATED);
    }

    @PutMapping ("/{id}/revisionEntrega")
    public ResponseEntity<OrdenResponseDTO> registrarRevisionEntrega(@PathVariable String id, @Valid @RequestBody RevisionEntregaRequestDTO revision){
        return new ResponseEntity<OrdenResponseDTO>(ordenService.registrarRevisionEntrega(id, revision), HttpStatus.OK);
    }

    @PutMapping ("/{id}/entrega")
    public ResponseEntity<OrdenResponseDTO> registrarEntrega(@PathVariable String id, @Valid @RequestBody EntregaRequestDTO entrega){
        return new ResponseEntity<OrdenResponseDTO>(ordenService.registrarEntrega(id, entrega), HttpStatus.OK);
    }

    @PutMapping ("/{id}/valoracion")
    public ResponseEntity<OrdenResponseDTO> registrarValoracion(@PathVariable String id, @Valid @RequestBody ValoracionRequestDTO valoracion){
        return new ResponseEntity<OrdenResponseDTO>(ordenService.registrarValoracion(id, valoracion), HttpStatus.OK);
    }
}
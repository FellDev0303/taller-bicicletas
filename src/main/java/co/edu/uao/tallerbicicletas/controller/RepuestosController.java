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
import org.springframework.web.bind.annotation.RestController;

import co.edu.uao.tallerbicicletas.dto.RepuestoRequestDTO;
import co.edu.uao.tallerbicicletas.dto.RepuestoResponseDTO;
import co.edu.uao.tallerbicicletas.service.IRepuestoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping ("/UAO/apirest/repuestos")
public class RepuestosController {
    @Autowired IRepuestoService repuestoService;

    @PostMapping ("/")
    public ResponseEntity<RepuestoResponseDTO> crearRepuesto(@Valid @RequestBody RepuestoRequestDTO repuesto){
        return new ResponseEntity<RepuestoResponseDTO>(repuestoService.crearRepuesto(repuesto), HttpStatus.CREATED);
    }

    @GetMapping ("/")
    public ResponseEntity<List<RepuestoResponseDTO>> listarRepuestos(){
        return new ResponseEntity<List<RepuestoResponseDTO>>(repuestoService.listarRepuestos(), HttpStatus.OK);
    }

    @GetMapping ("/{id}")
    public ResponseEntity<RepuestoResponseDTO> buscarRepuestoPorId(@PathVariable String id){
        return new ResponseEntity<RepuestoResponseDTO>(repuestoService.buscarRepuestoPorId(id), HttpStatus.OK);
    }

    @PutMapping ("/{id}")
    public ResponseEntity<RepuestoResponseDTO> actualizarRepuesto(@PathVariable String id, @Valid @RequestBody RepuestoRequestDTO repuesto){
        return new ResponseEntity<RepuestoResponseDTO>(repuestoService.actualizarRepuesto(id, repuesto), HttpStatus.OK);
    }
}

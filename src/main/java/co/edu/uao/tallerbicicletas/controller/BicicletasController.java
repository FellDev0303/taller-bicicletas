package co.edu.uao.tallerbicicletas.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uao.tallerbicicletas.dto.BicicletaRequestDTO;
import co.edu.uao.tallerbicicletas.dto.BicicletaResponseDTO;
import co.edu.uao.tallerbicicletas.service.IBicicletaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/UAO/apirest/bicicletas")
public class BicicletasController {
    @Autowired IBicicletaService bicicletaService;

    @PostMapping("/")
    public ResponseEntity<BicicletaResponseDTO> crearBicicleta(@Valid @RequestBody BicicletaRequestDTO bicicleta) {
        return new ResponseEntity<BicicletaResponseDTO>(bicicletaService.crearBicicleta(bicicleta), HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<BicicletaResponseDTO>> listarBicicletas(
            @RequestParam(required = false) String propietarioId) {
        return new ResponseEntity<List<BicicletaResponseDTO>>(bicicletaService.listarBicicletas(propietarioId), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BicicletaResponseDTO> buscarBicicletaPorId(@PathVariable String id) {
        return new ResponseEntity<BicicletaResponseDTO>(bicicletaService.buscarBicicletaPorId(id), HttpStatus.OK);
    }
}
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

import co.edu.uao.tallerbicicletas.dto.ServicioRequestDTO;
import co.edu.uao.tallerbicicletas.dto.ServicioResponseDTO;
import co.edu.uao.tallerbicicletas.service.IServicioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping ("/UAO/apirest/servicios")
public class ServiciosController {
    @Autowired IServicioService servicioService;

    @PostMapping ("/")
    public ResponseEntity<ServicioResponseDTO> crearServicio(@Valid @RequestBody ServicioRequestDTO servicio){
        return new ResponseEntity<ServicioResponseDTO>(servicioService.crearServicio(servicio), HttpStatus.CREATED);
    }

    // Sin parametro lista todo el catalogo; con ?categoria=frenos filtra por categoria
    @GetMapping ("/")
    public ResponseEntity<List<ServicioResponseDTO>> listarServicios(@RequestParam(required = false) String categoria){
        if (categoria == null) {
            return new ResponseEntity<List<ServicioResponseDTO>>(servicioService.listarServicios(), HttpStatus.OK);
        }
        return new ResponseEntity<List<ServicioResponseDTO>>(servicioService.listarServiciosPorCategoria(categoria), HttpStatus.OK);
    }

    @GetMapping ("/{id}")
    public ResponseEntity<ServicioResponseDTO> buscarServicioPorId(@PathVariable String id){
        return new ResponseEntity<ServicioResponseDTO>(servicioService.buscarServicioPorId(id), HttpStatus.OK);
    }

    @PutMapping ("/{id}")
    public ResponseEntity<ServicioResponseDTO> actualizarServicio(@PathVariable String id, @Valid @RequestBody ServicioRequestDTO servicio){
        return new ResponseEntity<ServicioResponseDTO>(servicioService.actualizarServicio(id, servicio), HttpStatus.OK);
    }
}
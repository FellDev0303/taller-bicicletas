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
import org.springframework.web.bind.annotation.RestController;

import co.edu.uao.tallerbicicletas.dto.UsuarioRequestDTO;
import co.edu.uao.tallerbicicletas.dto.UsuarioResponseDTO;
import co.edu.uao.tallerbicicletas.service.IUsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping ("/UAO/apirest/usuarios")
public class UsuariosController {
    @Autowired IUsuarioService usuarioService;

    @PostMapping ("/")
    public ResponseEntity<UsuarioResponseDTO> crearUsuario(@Valid @RequestBody UsuarioRequestDTO usuario){
        return new ResponseEntity<UsuarioResponseDTO>(usuarioService.crearUsuario(usuario), HttpStatus.CREATED);
    }

    @GetMapping ("/")
    public ResponseEntity<List<UsuarioResponseDTO>> listarUsuarios(){
        return new ResponseEntity<List<UsuarioResponseDTO>>(usuarioService.listarUsuarios(), HttpStatus.OK);
    }

    @GetMapping ("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarUsuarioPorId(@PathVariable String id){
        return new ResponseEntity<UsuarioResponseDTO>(usuarioService.buscarUsuarioPorId(id), HttpStatus.OK);
    }
}

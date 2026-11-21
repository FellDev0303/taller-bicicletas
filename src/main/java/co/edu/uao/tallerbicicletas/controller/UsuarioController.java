package co.edu.uao.tallerbicicletas.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uao.tallerbicicletas.model.Usuario;
import co.edu.uao.tallerbicicletas.repository.UsuarioRepository;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController 
@RequestMapping ("/api/usuarios")
public class UsuarioController {
    private  final UsuarioRepository repo;

    public UsuarioController(UsuarioRepository repo) {
        this.repo = repo;
    }
    
    @PostMapping()
    public ResponseEntity<Usuario> crear(@Valid @RequestBody Usuario usuario){

        usuario.setId(null);
        return
    ResponseEntity.status(HttpStatus.CREATED).body(repo.save(usuario));
    }


    @GetMapping()
    public List<Usuario> listar(){
        return repo.findAll();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtener(@PathVariable String id) {
        return repo.findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
    }
    
}

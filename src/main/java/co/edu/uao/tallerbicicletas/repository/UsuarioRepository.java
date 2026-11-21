package co.edu.uao.tallerbicicletas.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import co.edu.uao.tallerbicicletas.model.Usuario;

public interface UsuarioRepository extends MongoRepository<Usuario,String>{

    }

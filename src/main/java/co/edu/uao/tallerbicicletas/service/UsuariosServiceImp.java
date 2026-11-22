package co.edu.uao.tallerbicicletas.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uao.tallerbicicletas.dto.UsuarioRequestDTO;
import co.edu.uao.tallerbicicletas.dto.UsuarioResponseDTO;
import co.edu.uao.tallerbicicletas.exception.ConflictoException;
import co.edu.uao.tallerbicicletas.exception.RecursoNoEncontradoException;
import co.edu.uao.tallerbicicletas.exception.SolicitudInvalidaException;
import co.edu.uao.tallerbicicletas.model.UsuariosModel;
import co.edu.uao.tallerbicicletas.repository.IUsuariosRepository;

@Service
public class UsuariosServiceImp implements IUsuarioService {
    @Autowired IUsuariosRepository usuariosRepository;

    @Override
    public UsuarioResponseDTO crearUsuario(UsuarioRequestDTO dto) {
        
        if (usuariosRepository.existsByDocumento(dto.getDocumento())) {
            throw new ConflictoException("Ya existe un usuario con el documento " + dto.getDocumento());
        }
        if (usuariosRepository.existsByCorreo(dto.getCorreo())) {
            throw new ConflictoException("Ya existe un usuario con el correo " + dto.getCorreo());
        }
  
        UsuariosModel usuario = new UsuariosModel(null, dto.getNombreCompleto(), dto.getDocumento(),
                dto.getCorreo(), dto.getTelefono(), dto.getRol(), dto.getDireccion());
        return convertirADTO(usuariosRepository.save(usuario));
    }

    @Override
    public List<UsuarioResponseDTO> listarUsuarios() {
        return usuariosRepository.findAll().stream().map(this::convertirADTO).toList();
    }

    @Override
    public UsuarioResponseDTO buscarUsuarioPorId(String idUsuario) {
        if (!ObjectId.isValid(idUsuario)) {
            throw new SolicitudInvalidaException("El id '" + idUsuario + "' no tiene un formato válido");
        }
        UsuariosModel usuario = usuariosRepository.findById(new ObjectId(idUsuario))
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un usuario con el id " + idUsuario));
        return convertirADTO(usuario);
    }

    private UsuarioResponseDTO convertirADTO(UsuariosModel usuario) {
        return new UsuarioResponseDTO(usuario.getId().toHexString(), usuario.getNombreCompleto(),
                usuario.getDocumento(), usuario.getCorreo(), usuario.getTelefono(),
                usuario.getRol(), usuario.getDireccion());
    }
}
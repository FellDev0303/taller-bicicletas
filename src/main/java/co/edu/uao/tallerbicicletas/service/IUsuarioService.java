package co.edu.uao.tallerbicicletas.service;

import java.util.List;

import co.edu.uao.tallerbicicletas.dto.UsuarioRequestDTO;
import co.edu.uao.tallerbicicletas.dto.UsuarioResponseDTO;

public interface IUsuarioService {
    public UsuarioResponseDTO crearUsuario(UsuarioRequestDTO usuario);
    public List<UsuarioResponseDTO> listarUsuarios();
    public UsuarioResponseDTO buscarUsuarioPorId(String idUsuario);
} 
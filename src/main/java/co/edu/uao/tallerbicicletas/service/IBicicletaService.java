package co.edu.uao.tallerbicicletas.service;

import java.util.List;

import co.edu.uao.tallerbicicletas.dto.BicicletaRequestDTO;
import co.edu.uao.tallerbicicletas.dto.BicicletaResponseDTO;

public interface IBicicletaService {
    BicicletaResponseDTO crearBicicleta(BicicletaRequestDTO bicicleta);
    List<BicicletaResponseDTO> listarBicicletas(String propietarioId);
    BicicletaResponseDTO buscarBicicletaPorId(String id);
}
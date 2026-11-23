package co.edu.uao.tallerbicicletas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BicicletaResponseDTO {
    private String id;
    private String propietarioId;
    private String marca;
    private String modelo;
    private String tipo;
    private String color;
    private String numeroSerie;
    private BicicletaRequestDTO.CaracteristicasDTO caracteristicas;
}
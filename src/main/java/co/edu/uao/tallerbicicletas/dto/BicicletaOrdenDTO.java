package co.edu.uao.tallerbicicletas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BicicletaOrdenDTO {
    private String bicicletaId;
    private String marca;
    private String modelo;
}
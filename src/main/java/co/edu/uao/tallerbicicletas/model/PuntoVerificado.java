package co.edu.uao.tallerbicicletas.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PuntoVerificado {
    private String punto;
    private Boolean verificado;
    private String observacion;
}

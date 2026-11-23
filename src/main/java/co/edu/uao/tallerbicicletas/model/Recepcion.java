package co.edu.uao.tallerbicicletas.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Recepcion {
    private String problemaReportado;
    private String condicionesObservadas;
    private List<String> accesorios;
    private List<Adjunto> fotografias;
}
